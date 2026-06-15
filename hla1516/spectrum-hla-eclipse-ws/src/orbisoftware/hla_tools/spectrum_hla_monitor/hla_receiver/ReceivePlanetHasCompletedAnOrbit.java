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

package orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver;

import java.beans.PropertyChangeSupport;
import java.nio.charset.Charset;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RegionHandleSet;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLADataLogger;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLAReadEventHandler;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLASamples;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLATopic;
import orbisoftware.hla_1516e_containers.Interactions.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont;
import orbisoftware.hla_1516e_encoding.Interactions.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode;

public class ReceivePlanetHasCompletedAnOrbit {

   private InteractionClassHandle interactionHandle;
	
	public PropertyChangeSupport readEventSample;
	
	private static ReceivePlanetHasCompletedAnOrbit single_instance = null;
	
	private HLATopic hlaTopic = null;
	private long initialStartTime = 0;
	public boolean autoUpdateEnabled = false;
	private PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode planetHasCompletedAnOrbit;
	
   public static synchronized ReceivePlanetHasCompletedAnOrbit getInstance()
   {
       if (single_instance == null)
           single_instance = new ReceivePlanetHasCompletedAnOrbit();

       return single_instance;
   }
   
	private ReceivePlanetHasCompletedAnOrbit() {

	}
	
	public void initialize(HLATopic hlaTopic) {
	   
      readEventSample = new PropertyChangeSupport(this);
      
      hlaTopic.readEventHandler = new HLAReadEventHandler(hlaTopic);
      readEventSample.addPropertyChangeListener(hlaTopic.readEventHandler);
      readEventSample.addPropertyChangeListener(hlaTopic.readEventHandler.receiveListGUI);
      hlaTopic.readEventHandler.start();
      
      hlaTopic.dataLogger = new HLADataLogger(hlaTopic);
      readEventSample.addPropertyChangeListener(hlaTopic.dataLogger);
      hlaTopic.dataLogger.start();
      
      initialStartTime = System.currentTimeMillis();
      
      this.hlaTopic = hlaTopic;
	}
	
	public void performObjectPreExecution(RTIambassador rtiAmb, CommonFederateAmbassador fedAmb,
			RegionHandleSet defaultRegionSet) {

	   planetHasCompletedAnOrbit = new PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode();

		try {
		   
		   planetHasCompletedAnOrbit.initialize(rtiAmb);
         
		   interactionHandle = planetHasCompletedAnOrbit.interactionHandle;
		   
			rtiAmb.subscribeInteractionClassWithRegions(interactionHandle, defaultRegionSet);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

   public void receiveInteractionCallback(
         ParameterHandleValueMap theParameterValues, byte[] userSuppliedTag) {

      PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont container = new PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont();

      planetHasCompletedAnOrbit.decode(theParameterValues);

      container.planetID = planetHasCompletedAnOrbit.getPlanetID();
      container.planetName.value = planetHasCompletedAnOrbit.getPlanetName().getString();

      if (autoUpdateEnabled) {

         HLASamples hlaSamples = new HLASamples();
         hlaSamples.sampleReadTime = System.currentTimeMillis()
               - initialStartTime;
         hlaSamples.seqHolderName = new String(hlaTopic.name);
         hlaSamples.seqHolder = container;

         readEventSample.firePropertyChange("hlaReadEvent", 0, hlaSamples);
      }
   }
}
