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

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
import orbisoftware.hla13_containers.Objects.StarField_db5867775c835118_Cont.StarField_db5867775c835118_Cont;
import orbisoftware.hla13_encoding.Common.FixedArrays.StarTypeArray_Encode;
import orbisoftware.hla13_encoding.Common.FixedRecords.StarType_Encode;
import orbisoftware.hla13_encoding.Objects.StarField_db5867775c835118_Encode.StarField_db5867775c835118_Encode;
import orbisoftware.hla_shared.Utilities;

public class PublishStarField {
	
   private int objectInstanceHandle;
	
   private RTIambassador rtiAmb;
   private CommonFederateAmbassador fedAmb;
   private Object defaultRegionSet;
   
   private StarField_db5867775c835118_Encode starField;
   
   private static PublishStarField single_instance = null;

   public static synchronized PublishStarField getInstance() {
      if (single_instance == null)
         single_instance = new PublishStarField();

      return single_instance;
   }
   
   private PublishStarField() {

   }
   
	public void performObjectPreExecution(RTIambassador rtiAmb, CommonFederateAmbassador fedAmb, Object defaultRegionSet) {

      this.rtiAmb = rtiAmb;
      this.fedAmb = fedAmb;
      this.defaultRegionSet = defaultRegionSet;
      
      starField = new StarField_db5867775c835118_Encode();
      
		try {
		   
		   starField.initialize(rtiAmb);

         rtiAmb.publishObjectClass(starField.objectHandle, starField.attribHandles);
         objectInstanceHandle = rtiAmb.registerObjectInstance(starField.objectHandle);
         fedAmb.instanceHandleToClassHandleMap.put(Integer.valueOf(objectInstanceHandle).hashCode(), starField.objectHandle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void performObjectExecution() {
		
	}
	
   public void sideLoadPublishSample(StarField_db5867775c835118_Cont container) {

      SuppliedAttributes attributes = null;
      Utilities utilities = new Utilities();
      
      byte[] tag = utilities.createTimestampTag();
      
      if (rtiAmb == null)
         return;
      
      try {
         attributes = RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      StarTypeArray_Encode starTypeArray = new StarTypeArray_Encode();
        
      for (int i=0; i < starTypeArray.cardinality; i++) {
         
         starTypeArray.setStarType_Encode(i, new StarType_Encode());
      
         starTypeArray.getStarType_Encode(i).setStarID(container.stars.value[i].starID);
         starTypeArray.getStarType_Encode(i).setBaseSize(container.stars.value[i].baseSize);
         starTypeArray.getStarType_Encode(i).setXpos(container.stars.value[i].xpos);
         starTypeArray.getStarType_Encode(i).setYpos(container.stars.value[i].ypos);
         starTypeArray.getStarType_Encode(i).setProjX(container.stars.value[i].projX);
         starTypeArray.getStarType_Encode(i).setProjY(container.stars.value[i].projY);
         starTypeArray.getStarType_Encode(i).setProjZ(container.stars.value[i].projZ);
         starTypeArray.getStarType_Encode(i).setSize(container.stars.value[i].size);
         starTypeArray.getStarType_Encode(i).setSpeed(container.stars.value[i].speed);
         starTypeArray.getStarType_Encode(i).setBlinkSpeed(container.stars.value[i].blinkSpeed);
         starTypeArray.getStarType_Encode(i).setBlinkPhase(container.stars.value[i].blinkPhase);
         starTypeArray.getStarType_Encode(i).setAlpha(container.stars.value[i].alpha);
         
      }
      
      starField.setStars(starTypeArray);      
      starField.encode(attributes);

      try {
         
         rtiAmb.updateAttributeValues(objectInstanceHandle, attributes, tag);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
