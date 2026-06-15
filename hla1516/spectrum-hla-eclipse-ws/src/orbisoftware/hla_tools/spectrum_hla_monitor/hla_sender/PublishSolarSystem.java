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

package orbisoftware.hla_tools.spectrum_hla_monitor.hla_sender;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.AttributeRegionAssociation;
import hla.rti1516e.AttributeSetRegionSetPairList;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RegionHandleSet;
import orbisoftware.hla_1516e_containers.Objects.SolarSystem_124a7dc86c25491f_Cont.SolarSystem_124a7dc86c25491f_Cont;
import orbisoftware.hla_1516e_encoding.Common.FixedArrays.PlanetTypeArray_Encode;
import orbisoftware.hla_1516e_encoding.Common.FixedRecords.PlanetType_Encode;
import orbisoftware.hla_1516e_encoding.Objects.SolarSystem_124a7dc86c25491f_Encode.SolarSystem_124a7dc86c25491f_Encode;
import orbisoftware.hla_shared.Utilities;

public class PublishSolarSystem {
	
   private ObjectInstanceHandle objectInstanceHandle;
	
   private RTIambassador rtiAmb;
   private CommonFederateAmbassador fedAmb;
   private RegionHandleSet defaultRegionSet;
   
   private SolarSystem_124a7dc86c25491f_Encode solarSystem;
   
   private static PublishSolarSystem single_instance = null;

   public static synchronized PublishSolarSystem getInstance() {
      if (single_instance == null)
         single_instance = new PublishSolarSystem();

      return single_instance;
   }
   
   private PublishSolarSystem() {

   }
   
	public void performObjectPreExecution(RTIambassador rtiAmb, CommonFederateAmbassador fedAmb, RegionHandleSet defaultRegionSet) {

      this.rtiAmb = rtiAmb;
      this.fedAmb = fedAmb;
      this.defaultRegionSet = defaultRegionSet;
      
      solarSystem = new SolarSystem_124a7dc86c25491f_Encode();
      
		try {
		   
		   solarSystem.initialize(rtiAmb);

         rtiAmb.publishObjectClassAttributes(solarSystem.objectHandle, solarSystem.attribHandles);

         AttributeSetRegionSetPairList attrHandleSetPairList = rtiAmb.getAttributeSetRegionSetPairListFactory().create(1);
         AttributeRegionAssociation attrHandleSetPair = new AttributeRegionAssociation(solarSystem.attribHandles, defaultRegionSet);
         attrHandleSetPairList.add(attrHandleSetPair);

         objectInstanceHandle = rtiAmb.registerObjectInstanceWithRegions(solarSystem.objectHandle, attrHandleSetPairList);
         fedAmb.instanceHandleToClassHandleMap.put(objectInstanceHandle.hashCode(), solarSystem.objectHandle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void performObjectExecution() {
		
	}
	
   public void sideLoadPublishSample(SolarSystem_124a7dc86c25491f_Cont container) {

      AttributeHandleValueMap attributes = null;
      Utilities utilities = new Utilities();
      
      byte[] tag = new String(utilities.generateRandomAlphaNumeric(8)).getBytes();
      
      if (rtiAmb == null)
         return;
      
      try {
         attributes = rtiAmb.getAttributeHandleValueMapFactory().create(solarSystem.getNumberAttributes());
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      PlanetTypeArray_Encode planetTypeArray = new PlanetTypeArray_Encode();
        
      for (int i=0; i < planetTypeArray.cardinality; i++) {
         
         planetTypeArray.setPlanetType_Encode(i, new PlanetType_Encode());
      
         planetTypeArray.getPlanetType_Encode(i).setPlanetID(i);
         planetTypeArray.getPlanetType_Encode(i).setPlanetOrdinalValue(container.planets.value[i].planetOrdinalValue);
         planetTypeArray.getPlanetType_Encode(i).setXpos(container.planets.value[i].xpos);
         planetTypeArray.getPlanetType_Encode(i).setYpos(container.planets.value[i].ypos);
         planetTypeArray.getPlanetType_Encode(i).setTheta(container.planets.value[i].theta);
         planetTypeArray.getPlanetType_Encode(i).setPlanetSize(container.planets.value[i].planetSize);
         planetTypeArray.getPlanetType_Encode(i).setOrbitalRadius(container.planets.value[i].orbitalRadius);
         planetTypeArray.getPlanetType_Encode(i).setOrbitalVelocity(container.planets.value[i].orbitalVelocity);
         
      }
      
      solarSystem.setPlanets(planetTypeArray);
      
      solarSystem.encode(attributes);

      try {
         
         rtiAmb.updateAttributeValues(objectInstanceHandle, attributes, tag);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
