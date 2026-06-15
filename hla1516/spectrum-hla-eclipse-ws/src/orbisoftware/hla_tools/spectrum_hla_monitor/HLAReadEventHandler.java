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
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ecollege.gson.GsonExt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import orbisoftware.hla_tools.spectrum_hla_monitor.HLATopic.ViewMode;
import orbisoftware.hla_tools.spectrum_hla_monitor.sampleviewer.ReceiveListGUI;
import orbisoftware.oricrdsm.OricSymbolMap;

public class HLAReadEventHandler extends Thread implements
      PropertyChangeListener, ListSelectionListener {

   private BlockingQueue<HLASamples> queue = null;
   private List<HLASamples> readEventList = null;
   private HLATopic hlaTopic;
   private String OBJ_TO_SYM_MAP_METHOD = "objToSymMap";
   private Class<?> oricSymbolMapClass = null;
   public ReceiveListGUI receiveListGUI = null;
   
   public HLAReadEventHandler(HLATopic hlaTopic) {

      queue = new LinkedBlockingQueue<HLASamples>();
      this.hlaTopic = hlaTopic;
      hlaTopic.readEventHandlerShutdownComplete = false;
      receiveListGUI = new ReceiveListGUI();
      receiveListGUI.displayList.addListSelectionListener(this);
   }

   public void propertyChange(PropertyChangeEvent evt) {

      if (evt.getPropertyName().toString().equals("hlaReadEvent")) {

         synchronized (queue) {
            queue.add((HLASamples) evt.getNewValue());
            queue.notify();
         }
      }
   }
   
   public void valueChanged(ListSelectionEvent e) {

      try {

         OricSymbolMap oricSymMap = new OricSymbolMap();
         HLASamples readEvent = receiveListGUI.receiveQueue.get_index(receiveListGUI.displayList.getSelectedIndex());

         if (!e.getValueIsAdjusting()) {
            
            if (readEvent == null)
               return;
            
            if (hlaTopic.viewMode == ViewMode.TREE_TABLE) {
               
               oricSymbolMapClass = oricSymMap.getClass();
               Class<?>[] methodTypeParams = { Object.class };
               Method objToSymMap = oricSymbolMapClass
                     .getMethod(OBJ_TO_SYM_MAP_METHOD, methodTypeParams);
               Object[] methodParams = { readEvent.seqHolder };
               objToSymMap.invoke(oricSymMap, methodParams);
   
               hlaTopic.sampleViewerGUI.updateTreeTable(oricSymMap);
            }
         }

      } catch (Exception e2) {
         e2.printStackTrace();
      }
   }

   public void run() {

      Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
            .serializeSpecialFloatingPointValues().create();

      while (!hlaTopic.readEventHandlerShutdownComplete) {

         readEventList = new LinkedList<HLASamples>();

         try {
            // block until notified that more readEvents have been received
            synchronized (queue) {
               queue.wait();
               queue.drainTo(readEventList);
            }
         } catch (InterruptedException e) {
            shutdown();
         }

         while (readEventList.size() > 0) {

            try {

               OricSymbolMap oricSymMap = new OricSymbolMap();
               HLASamples readEvent = readEventList.remove(0); // Read the next sample from the linked list
               
               oricSymbolMapClass = oricSymMap.getClass();
               Class<?>[] methodTypeParams = { Object.class };
               Method objToSymMap = oricSymbolMapClass.getMethod(OBJ_TO_SYM_MAP_METHOD, methodTypeParams);
               Object[] methodParams = { readEvent.seqHolder };
               objToSymMap.invoke(oricSymMap, methodParams);

               if (hlaTopic.viewMode == ViewMode.TEXT_LIST) {

                  String xml = GsonExt.toXml(gson.toJson(readEvent.seqHolder));
                  hlaTopic.sampleViewerGUI.updateTextTable(xml);
               }
               
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
      readEventList.clear();
      hlaTopic.readEventHandlerShutdownComplete = true;
   }
}
