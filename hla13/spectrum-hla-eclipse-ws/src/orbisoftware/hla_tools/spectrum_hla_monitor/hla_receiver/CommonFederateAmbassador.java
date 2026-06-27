/*
 *  Spectrum HLA data monitor tool
 *
 *  Copyright (C) 2024 Harlan Murphy
 *  Orbis Software - orbisoftware@gmail.com
 */

package orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver;

import java.util.ArrayList;
import java.util.HashMap;

import hla.rti.*;
import hla.rti.jlc.*;

public class CommonFederateAmbassador extends NullFederateAmbassador {

   private ArrayList<Integer> objs;
   private RTIambassador rtiAmb;
   public HashMap<Integer, Integer> instanceHandleToClassHandleMap;
   private boolean debug = false;

   CommonFederateAmbassador() {
      objs = new ArrayList<Integer>();
      instanceHandleToClassHandleMap = new HashMap<Integer, Integer>();
   }

   void setRtiAmb(RTIambassador rtiAmb) {
      this.rtiAmb = rtiAmb;
   }

   @Override
   public void discoverObjectInstance(int theObject, int theObjectClass, String objectName) throws FederateInternalError {
      instanceHandleToClassHandleMap.put(Integer.valueOf(theObject).hashCode(), theObjectClass);
      objs.add(theObject);
   }

   @Override
   public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes,
         byte[] userSuppliedTag) throws FederateInternalError {
      try {
         if (debug)
            System.out.println("reflectAttributeValues");

         String className = rtiAmb.getObjectClassName(instanceHandleToClassHandleMap.get(Integer.valueOf(theObject).hashCode()));

         if (className.equals("StarField")) {
            if (debug)
               System.out.println("calling ReceiveStarfield.receiveAttributeUpdateCallback");
            ReceiveStarField.getInstance().receiveAttributeUpdateCallback(theAttributes, userSuppliedTag);
         }
         
         if (className.equals("SolarSystem")) {
            if (debug)
               System.out.println("calling ReceiveSolarSystem.receiveAttributeUpdateCallback");
            ReceiveSolarSystem.getInstance().receiveAttributeUpdateCallback(theAttributes, userSuppliedTag);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public void receiveInteraction(int interactionClass, ReceivedInteraction theParameters,
         byte[] userSuppliedTag) throws FederateInternalError {
      try {
         if (debug)
            System.out.println("receiveInteraction");

         String className = rtiAmb.getInteractionClassName(interactionClass);

         if (className.equals("StarHasLeftStarField")) {
            if (debug)
               System.out.println("calling ReceiveStarHasLeftStarfield.receiveInteractionCallback");
               ReceiveStarHasLeftStarField.getInstance().receiveInteractionCallback(theParameters, userSuppliedTag);
         }
         
         if (className.equals("PlanetHasCompletedAnOrbit")) {
            if (debug)
               System.out.println("calling ReceivePlanetHasCompletedAnOrbit.receiveInteractionCallback");
            ReceivePlanetHasCompletedAnOrbit.getInstance().receiveInteractionCallback(theParameters, userSuppliedTag);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public void removeObjectInstance(int theObject, byte[] userSuppliedTag) throws FederateInternalError {
      objs.remove(Integer.valueOf(theObject));
   }
}
