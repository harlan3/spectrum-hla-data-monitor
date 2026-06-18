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

import java.lang.reflect.Method;

public class HLATopicReaderWriter extends Thread {

   private HLATopic hlaTopic = null;

   private boolean initialized;

   /** Create new instance */
   public HLATopicReaderWriter(HLATopic hlaTopic) {
      
      this.hlaTopic = hlaTopic;
      initialized = false;
   }

   /** Establish new HLA Connection for topic */
   public Boolean HLAConnect() {

      boolean errorEncountered = false;

      try {
         
         Class<?> singletonClass = Class.forName(hlaTopic.receiverType);
         Method getInstanceMethod = singletonClass.getMethod("getInstance");
         Object singletonInstance = getInstanceMethod.invoke(null);

         Method targetMethod = singletonClass.getMethod("initialize",
               hlaTopic.getClass());
         Object[] methodArgs = { hlaTopic };
         targetMethod.invoke(singletonInstance, methodArgs);

      } catch (Exception e) {

         e.printStackTrace();
      }
 
      // Check for connection problems
      if (errorEncountered)
         System.err.println("ERROR: HLA Connection failed");
      else
         initialized = true;
      
      return initialized;
   }
}
