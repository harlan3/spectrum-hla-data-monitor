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
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.AttributeRegionAssociation;
import hla.rti1516e.AttributeSetRegionSetPairList;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RegionHandleSet;
import orbisoftware.hla_1516e_containers.Objects.SolarSystem_124a7dc86c25491f_Cont.SolarSystem_124a7dc86c25491f_Cont;
import orbisoftware.hla_1516e_encoding.Common.FixedArrays.PlanetTypeArray_Encode;
import orbisoftware.hla_1516e_encoding.Common.FixedRecords.PlanetType_Encode;
import orbisoftware.hla_1516e_encoding.Objects.SolarSystem_124a7dc86c25491f_Encode.SolarSystem_124a7dc86c25491f_Encode;
import orbisoftware.hla_tools.spectrum_hla_monitor.ClassInstanceReference;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLADataLogger;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLAReadEventHandler;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLASamples;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLATopic;

public class ReceiveSolarSystem {
	
	private PropertyChangeSupport readEventSample;

	private static ReceiveSolarSystem single_instance = null;
	
	private HLATopic hlaTopic = null;
	private long initialStartTime = 0;
	public boolean autoUpdateEnabled = false;
	  
   public static synchronized ReceiveSolarSystem getInstance()
   {
       if (single_instance == null)
           single_instance = new ReceiveSolarSystem();

       return single_instance;
   }
   
	public ReceiveSolarSystem() {

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

	   SolarSystem_124a7dc86c25491f_Encode solarSystem = ClassInstanceReference.getInstance().solarSystem;

		try {
		   
		   // Wait for publisher to initialize the solar system object
		   while (solarSystem.hasInitialized == false)
		      Thread.sleep(1000);
		   
	      AttributeSetRegionSetPairList attrHandleSetPairList =  rtiAmb.getAttributeSetRegionSetPairListFactory().create(1);
	      AttributeRegionAssociation attrHandleSetPair = new AttributeRegionAssociation(solarSystem.attribHandles, defaultRegionSet);
	      attrHandleSetPairList.add(attrHandleSetPair);
		   
	      rtiAmb.subscribeObjectClassAttributesWithRegions(solarSystem.objectHandle, attrHandleSetPairList);
      
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveAttributeUpdateCallback(AttributeHandleValueMap theAttributeValues, byte[] userSuppliedTag) {

	   SolarSystem_124a7dc86c25491f_Cont container = new SolarSystem_124a7dc86c25491f_Cont();
	   SolarSystem_124a7dc86c25491f_Encode solarSystem = ClassInstanceReference.getInstance().solarSystem;
	   
	   solarSystem.decode(theAttributeValues);
	   
	   PlanetTypeArray_Encode planetTypeArray = solarSystem.getPlanets();
	   
	   for (int i=0; i < planetTypeArray.cardinality; i++) {
	      
	      PlanetType_Encode planet = planetTypeArray.getPlanetType_Encode(i);
	   
	      container.planets.value[i].planetID = planet.getPlanetID();
	      container.planets.value[i].planetOrdinalValue = planet.getPlanetOrdinalValue();
	      container.planets.value[i].xpos = planet.getXpos();
	      container.planets.value[i].ypos = planet.getYpos();
	      container.planets.value[i].theta = planet.getTheta();
	      container.planets.value[i].planetSize = planet.getPlanetSize();
	      container.planets.value[i].orbitalRadius = planet.getOrbitalRadius();
	      container.planets.value[i].orbitalVelocity = planet.getOrbitalVelocity();
	   }
      
      if (autoUpdateEnabled) {

         HLASamples hlaSamples = new HLASamples();
         hlaSamples.sampleReadTime = System.currentTimeMillis() - initialStartTime;
         hlaSamples.seqHolderName = new String(hlaTopic.name);
         hlaSamples.seqHolder = container;

         readEventSample.firePropertyChange("hlaReadEvent", 0, hlaSamples);
      }
	}
}
