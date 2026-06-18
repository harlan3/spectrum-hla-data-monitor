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

import orbisoftware.hla13_containers.Interactions.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont;
import orbisoftware.hla13_encoding.Common.PrefixedStringLength.HLAASCIIstringImp_Encode;
import orbisoftware.hla13_encoding.Interactions.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode;
import orbisoftware.hla_shared.Utilities;

public class PublishPlanetHasCompletedAnOrbit {

   private int interactionHandle;

   private RTIambassador rtiAmb;
   private CommonFederateAmbassador fedAmb;
   private Object defaultRegionSet;
   private PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode planetHasCompletedAnOrbit;
   
   private static PublishPlanetHasCompletedAnOrbit single_instance = null;

   public static synchronized PublishPlanetHasCompletedAnOrbit getInstance() {
      if (single_instance == null)
         single_instance = new PublishPlanetHasCompletedAnOrbit();

      return single_instance;
   }

   private PublishPlanetHasCompletedAnOrbit() {

   }

   public void performObjectPreExecution(RTIambassador rtiAmb,
         CommonFederateAmbassador fedAmb, Object defaultRegionSet) {
      
      this.rtiAmb = rtiAmb;
      this.fedAmb = fedAmb;
      this.defaultRegionSet = defaultRegionSet;

      planetHasCompletedAnOrbit = new PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode();
      
      try {
         planetHasCompletedAnOrbit.initialize(rtiAmb);
         
         interactionHandle = rtiAmb.getInteractionClassHandle(planetHasCompletedAnOrbit.getFullyQualifiedInteractionName());
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

   public void sideLoadPublishSample(PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Cont container) {

      SuppliedParameters parameters = null;
      Utilities utilities = new Utilities();
      
      byte[] tag = new String(utilities.generateRandomAlphaNumeric(8)).getBytes();
      
      if (rtiAmb == null)
         return;
      
      try {
         parameters = RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      planetHasCompletedAnOrbit.setPlanetID(container.planetID);
      
      HLAASCIIstringImp_Encode hlaASCIIstringImp = new HLAASCIIstringImp_Encode();
      hlaASCIIstringImp.setString(container.planetName.value);
      planetHasCompletedAnOrbit.setPlanetName(hlaASCIIstringImp);
 
      planetHasCompletedAnOrbit.encode(parameters);

      try {
         rtiAmb.sendInteraction(interactionHandle, parameters, tag);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
