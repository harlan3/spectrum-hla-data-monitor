/*
 *  Spectrum HLA data monitor tool
 *
 *  Copyright (C) 2024 Harlan Murphy
 *  Orbis Software - orbisoftware@gmail.com
 */

package orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver;

import java.net.MalformedURLException;
import java.net.URL;

import hla.rti.*;
import hla.rti.jlc.RtiFactory;
import hla.rti.jlc.RtiFactoryFactory;
import orbisoftware.hla_tools.spectrum_hla_monitor.MainSharedData;

public class HLAFederateThread extends Thread implements Runnable {

   // Objects
   private ReceiveSolarSystem receiveSolarSystem = ReceiveSolarSystem.getInstance();
   private ReceiveStarField receiveStarField = ReceiveStarField.getInstance();

   // Interactions
   private ReceivePlanetHasCompletedAnOrbit receivePlanetHasCompletedAnOrbit = ReceivePlanetHasCompletedAnOrbit.getInstance();
   private ReceiveStarHasLeftStarField receiveStarHasLeftStarField = ReceiveStarHasLeftStarField.getInstance();

   private RtiFactory factory_;
   private RTIambassador rtiamb_;
   private CommonFederateAmbassador fedamb_;

   public boolean initialized;
   private static boolean shutdown;
   public static boolean shutdownComplete;

   HLAFederateThread() throws RTIexception, MalformedURLException {
      factory_ = RtiFactoryFactory.getRtiFactory();
      rtiamb_ = factory_.createRtiAmbassador();

      fedamb_ = new CommonFederateAmbassador();
      fedamb_.setRtiAmb(rtiamb_);

      initialized = false;
      shutdown = false;
      shutdownComplete = false;
   }

   void createDefaultRegion(Object regions) {
      // HLA 1.3 port uses the basic publish/subscribe/register APIs.
      // Region/DDM-specific 1516e calls were intentionally removed.
   }

   @Override
   public void run() {
      shutdown = false;
      try {
         threadEntryPoint();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void shutdownReq() {
      shutdown = true;
   }

   void threadEntryPoint() throws RTIexception, MalformedURLException {

      try {
         rtiamb_.createFederationExecution(
               MainSharedData.getInstance().xmlMap.get("Federation"),
               new URL("file:" + MainSharedData.getInstance().xmlMap.get("FomFile")));
      } catch (FederationExecutionAlreadyExists ex) {
      } catch (RTIexception ex) {
         System.err.println(ex.toString());
         System.exit(-1);
      }

      try {
         int federateHandle = rtiamb_.joinFederationExecution(
               MainSharedData.getInstance().xmlMap.get("Federate"),
               MainSharedData.getInstance().xmlMap.get("Federation"), fedamb_);
         MainSharedData.getInstance().federateHandle2 = federateHandle;
      } catch (RTIexception ex) {
         System.err.println(ex.toString());
         System.exit(-1);
      }

      Object regionHandleSet = null;
      createDefaultRegion(regionHandleSet);

      // Objects
      receiveSolarSystem.performObjectPreExecution(rtiamb_, fedamb_, regionHandleSet);
      receiveStarField.performObjectPreExecution(rtiamb_, fedamb_, regionHandleSet);
      
      // Interactions
      receivePlanetHasCompletedAnOrbit.performObjectPreExecution(rtiamb_, fedamb_, regionHandleSet);
      receiveStarHasLeftStarField.performObjectPreExecution(rtiamb_, fedamb_, regionHandleSet);

      initialized = true;

      while (!shutdown) {
         
         rtiamb_.tick();
         
         try {
            Thread.sleep(10);
         } catch (InterruptedException e) { }
      }

      // One last invocation for callbacks
      rtiamb_.tick();

      try {
         rtiamb_.resignFederationExecution(ResignAction.DELETE_OBJECTS_AND_RELEASE_ATTRIBUTES);
      } catch (RTIexception ex) {
         System.err.println(ex.toString());
         System.exit(-1);
      }

      try {
         rtiamb_.destroyFederationExecution(MainSharedData.getInstance().xmlMap.get("Federation"));
      } catch (FederatesCurrentlyJoined ex) {
      } catch (RTIexception ex) {
         System.err.println(ex.toString());
         System.exit(-1);
      }

      shutdownComplete = true;
   }
}
