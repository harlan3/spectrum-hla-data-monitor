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
import hla.rti.jlc.RtiFactory;
import hla.rti.jlc.RtiFactoryFactory;
import orbisoftware.hla13_containers.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Cont.StarHasLeftStarField_bd6b9371adddafc6_Cont;
import orbisoftware.hla13_encoding.Common.NullTerminatedArrays.NullTerminatedASCIIStringImp_Encode;
import orbisoftware.hla13_encoding.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Encode.StarHasLeftStarField_bd6b9371adddafc6_Encode;
import orbisoftware.hla_shared.Utilities;

public class PublishStarHasLeftStarField {

   private int interactionHandle;

   private RTIambassador rtiAmb;
   private CommonFederateAmbassador fedAmb;
   private Object defaultRegionSet;
   private StarHasLeftStarField_bd6b9371adddafc6_Encode starHasLeftStarField;
   
   private static PublishStarHasLeftStarField single_instance = null;

   public static synchronized PublishStarHasLeftStarField getInstance() {
      if (single_instance == null)
         single_instance = new PublishStarHasLeftStarField();

      return single_instance;
   }

   private PublishStarHasLeftStarField() {

   }

   public void performObjectPreExecution(RTIambassador rtiAmb,
         CommonFederateAmbassador fedAmb, Object defaultRegionSet) {
      
      this.rtiAmb = rtiAmb;
      this.fedAmb = fedAmb;
      this.defaultRegionSet = defaultRegionSet;

      starHasLeftStarField = new StarHasLeftStarField_bd6b9371adddafc6_Encode();
      
      try {
         starHasLeftStarField.initialize(rtiAmb);
         
         interactionHandle = rtiAmb.getInteractionClassHandle(starHasLeftStarField.getFullyQualifiedInteractionName());
         rtiAmb.publishInteractionClass(interactionHandle);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void performObjectExecution() {

      SuppliedParameters parameters;
      RtiFactory factory_ = null;

      try {
         factory_ = RtiFactoryFactory.getRtiFactory();
      } catch (Exception e) {
      }
   }

   public void sideLoadPublishSample(StarHasLeftStarField_bd6b9371adddafc6_Cont container) {

      SuppliedParameters parameters = null;
      Utilities utilities = new Utilities();
      
      byte[] tag = utilities.createTimestampTag();
      
      if (rtiAmb == null)
         return;
      
      try {
         parameters = RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      starHasLeftStarField.setStarID(container.starID);
      
      NullTerminatedASCIIStringImp_Encode nullTerminatedASCIIString = new NullTerminatedASCIIStringImp_Encode();
      nullTerminatedASCIIString.setValue(container.starName.value.getBytes());
      starHasLeftStarField.setStarName(nullTerminatedASCIIString);
 
      starHasLeftStarField.encode(parameters);

      try {
         rtiAmb.sendInteraction(interactionHandle, parameters, tag);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
