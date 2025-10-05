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

package orbisoftware.hlatools.spectrumhlamonitor.hla_sender;

import java.util.ArrayList;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.AttributeRegionAssociation;
import hla.rti1516e.AttributeSetRegionSetPairList;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RegionHandleSet;
import orbisoftware.hla_1516e_encoding.Common.FixedArrays.PlanetTypeArray_Encode;
import orbisoftware.hla_1516e_encoding.Common.FixedRecords.PlanetType_Encode;
import orbisoftware.hla_1516e_encoding.Objects.SolarSystem_124a7dc86c25491f_Encode.SolarSystem_124a7dc86c25491f_Encode;
import orbisoftware.hla_shared.Utilities;
import orbisoftware.hlatools.spectrumhlamonitor.ClassInstanceReference;
import orbisoftware.hlatools.spectrumhlamonitor.solarsystemdemo.PlanetModel;

public class PublishSolarSystem {
	
   private ObjectInstanceHandle objectInstanceHandle;
	
   private RTIambassador rtiAmb;
   private CommonFederateAmbassador fedAmb;
   private RegionHandleSet defaultRegionSet;
   
   private static PublishSolarSystem single_instance = null;

   public static synchronized PublishSolarSystem getInstance() {
      if (single_instance == null)
         single_instance = new PublishSolarSystem();

      return single_instance;
   }
   
   public PublishSolarSystem() {

   }
   
	public void performObjectPreExecution(RTIambassador rtiAmb, CommonFederateAmbassador fedAmb, RegionHandleSet defaultRegionSet) {

      this.rtiAmb = rtiAmb;
      this.fedAmb = fedAmb;
      this.defaultRegionSet = defaultRegionSet;
      
		try {

		   SolarSystem_124a7dc86c25491f_Encode solarSystem = ClassInstanceReference.getInstance().solarSystem;
		   
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
	
   public void sideLoadPublishSample(ArrayList<PlanetModel> planetList) {

      SolarSystem_124a7dc86c25491f_Encode solarSystem = ClassInstanceReference.getInstance().solarSystem;       
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
         
         PlanetType_Encode planetType = planetTypeArray.getPlanetType_Encode(i);
         
         planetType.setPlanetID(i);
         planetType.setPlanetOrdinalValue(i);
         planetType.setXpos(planetList.get(i).getHLAPlanet().xPos);
         planetType.setYpos(planetList.get(i).getHLAPlanet().yPos);
         planetType.setTheta(planetList.get(i).getHLAPlanet().theta);
         planetType.setPlanetSize(planetList.get(i).getHLAPlanet().planetSize);
         planetType.setOrbitalRadius(planetList.get(i).getHLAPlanet().orbitalRadius);
         planetType.setOrbitalVelocity(planetList.get(i).getHLAPlanet().orbitalVelocity);
         
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
