/*
 *  Spectrum HLA data monitor tool
 *
 *  Copyright (C) 2024 Harlan Murphy
 *  Orbis Software - orbisoftware@gmail.com
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */

package orbisoftware.hla_tools.spectrum_hla_monitor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.ecollege.gson.GsonExt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HLADataLogger extends Thread implements PropertyChangeListener {

   private BlockingQueue<HLASamples> queue = null;
   private List<HLASamples> logEventList = null;
   private HLATopic hlaTopic;
   private String dataLoggerFileName = null;
   private FileWriter fileWriter;
   private BufferedWriter dataLoggerWriter;

   public HLADataLogger(HLATopic hlaTopic) {

      try {
         TopicManager topicManager = TopicManager.getInstance();
         dataLoggerFileName = new String(topicManager.GetDataCaptureDirectory()
               + hlaTopic.name + ".xml");
         fileWriter = new FileWriter(dataLoggerFileName);
         dataLoggerWriter = new BufferedWriter(fileWriter);
         dataLoggerWriter.write("<root>");
      } catch (Exception e) {// Catch exception if any
         System.err.println("ERROR: " + e.getMessage());
      }

      queue = new LinkedBlockingQueue<HLASamples>();
      this.hlaTopic = hlaTopic;
      hlaTopic.dataLoggerShutdownComplete = false;
   }

   public void propertyChange(PropertyChangeEvent evt) {

      if (evt.getPropertyName().toString().equals("hlaReadEvent")) {

         synchronized (queue) {
            queue.add((HLASamples) evt.getNewValue());
            queue.notify();
         }
      }
   }

   public void run() {

      while (!hlaTopic.dataLoggerShutdownComplete) {

         logEventList = new LinkedList<HLASamples>();

         try {
            // block until notified that more logEvents have been received
            synchronized (queue) {
               queue.wait();
               queue.drainTo(logEventList);
            }
         } catch (InterruptedException e) {
            shutdown();
         }

         for (HLASamples hlaSample : logEventList) {

            try {

               Gson gson = new GsonBuilder().setPrettyPrinting()
                     .serializeNulls().serializeSpecialFloatingPointValues()
                     .create();
               String xml = GsonExt.toXml(gson.toJson(hlaSample));
               dataLoggerWriter.write(xml);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }
   }

   public void shutdownReq() {

      this.interrupt();
   }

   private void shutdown() {
      
      queue.clear();
      logEventList.clear();
      try {
         dataLoggerWriter.write("</root>");
         dataLoggerWriter.close();

         File tmpLoggerFile = new File(dataLoggerFileName);

         // Remove file if 0 length or if it only contains root element
         if ((tmpLoggerFile.length() == 0) || (tmpLoggerFile.length() == 13))
            tmpLoggerFile.delete();

      } catch (IOException e) {
      }
      
      hlaTopic.dataLoggerShutdownComplete = true;
   }
}
