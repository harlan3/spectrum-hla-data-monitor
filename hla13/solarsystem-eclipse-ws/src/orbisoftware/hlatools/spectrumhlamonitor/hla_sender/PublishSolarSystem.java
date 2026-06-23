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

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
import java.util.ArrayList;
import java.util.Random;

import orbisoftware.hla13_containers.Common.PrefixedStringLength.HLAASCIIstringImp_Cont;
import orbisoftware.hla13_containers.Interactions.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont;
import orbisoftware.hla13_encoding.Common.Enums.PlanetOrdinal_Encode;
import orbisoftware.hla13_encoding.Common.FixedArrays.PlanetTypeArray_Encode;
import orbisoftware.hla13_encoding.Common.FixedRecords.PlanetType_Encode;
import orbisoftware.hla13_encoding.Objects.SolarSystem_124a7dc86c25491f_Encode.SolarSystem_124a7dc86c25491f_Encode;
import orbisoftware.hla_shared.Utilities;
import orbisoftware.hlatools.spectrumhlamonitor.solarsystemdemo.PlanetModel;

public class PublishSolarSystem {
	
   private int objectInstanceHandle;
	
   private RTIambassador rtiAmb;
   private CommonFederateAmbassador fedAmb;
   private Object defaultRegionSet;
   private SolarSystem_124a7dc86c25491f_Encode solarSystem;
   
   private static PublishSolarSystem single_instance = null;
   private long updateCounter = 0;

   public static synchronized PublishSolarSystem getInstance() {
      if (single_instance == null)
         single_instance = new PublishSolarSystem();

      return single_instance;
   }
   
   public PublishSolarSystem() {

   }
   
	public void performObjectPreExecution(RTIambassador rtiAmb, CommonFederateAmbassador fedAmb, Object defaultRegionSet) {

      this.rtiAmb = rtiAmb;
      this.fedAmb = fedAmb;
      this.defaultRegionSet = defaultRegionSet;

      solarSystem = new SolarSystem_124a7dc86c25491f_Encode();
      solarSystem.initialize(rtiAmb);
      
		try {

         rtiAmb.publishObjectClass(solarSystem.objectHandle, solarSystem.attribHandles);
objectInstanceHandle = rtiAmb.registerObjectInstance(solarSystem.objectHandle);
         fedAmb.instanceHandleToClassHandleMap.put(Integer.valueOf(objectInstanceHandle).hashCode(), solarSystem.objectHandle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void performObjectExecution() {
		
	}
	
   public void sideLoadPublishSample(ArrayList<PlanetModel> planetList) {
  
      SuppliedAttributes attributes = null;
      Utilities utilities = new Utilities();
      
      byte[] tag = utilities.createTimestampTag();
      
      updateCounter++;
      
      if (rtiAmb == null)
         return;
      
      try {
         attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
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
      
      if (updateCounter % 100 == 0) {
         
         PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont container = new PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont();
         HLAASCIIstringImp_Cont planetName = new HLAASCIIstringImp_Cont();
         Random random = new Random();
         int randomNum = random.nextInt(10);
         
         if (randomNum != 0) { // Ignore the Sun
            
            container.planetID = randomNum;
            planetName.value = PlanetOrdinal_Encode.getName(randomNum);
            container.planetName = planetName;
            
            PublishPlanetHasCompletedAnOrbit instance = PublishPlanetHasCompletedAnOrbit.getInstance();
            instance.sideLoadPublishSample(container);
         }
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
