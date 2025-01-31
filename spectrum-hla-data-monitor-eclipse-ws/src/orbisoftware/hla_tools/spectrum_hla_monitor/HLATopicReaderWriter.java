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

import orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver.ReceiveSolarSystem;
import orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver.ReceivePlanetHasCompletedAnOrbit;

public class HLATopicReaderWriter extends Thread {

   private HLATopic hlaTopic = null;

   private boolean initialized;

   /** Create new instance */
   public HLATopicReaderWriter(HLATopic hlaTopic) {
      
      this.hlaTopic = hlaTopic;
      initialized = false;
   }

   /** Establish new HLA Connection for topic */
   public Boolean HLAConnect(String partition, String topicDataType) {

      boolean errorEncountered = false;
      
      // Objects
      if (hlaTopic.name.equals("SolarSystem"))
         ReceiveSolarSystem.getInstance().initialize(hlaTopic);
      
      // Interactions
      if (hlaTopic.name.equals("PlanetHasCompletedAnOrbit"))
         ReceivePlanetHasCompletedAnOrbit.getInstance().initialize(hlaTopic);
 
      // Check for connection problems
      if (errorEncountered)
         System.err.println("ERROR: HLA Connection failed");
      else
         initialized = true;
      
      return initialized;
   }
}
