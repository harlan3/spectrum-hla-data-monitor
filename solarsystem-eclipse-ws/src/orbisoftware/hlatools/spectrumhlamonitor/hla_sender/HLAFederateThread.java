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

import java.net.MalformedURLException;
import java.net.URL;

import hla.rti1516e.CallbackModel;
import hla.rti1516e.DimensionHandle;
import hla.rti1516e.DimensionHandleSet;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RangeBounds;
import hla.rti1516e.RegionHandle;
import hla.rti1516e.RegionHandleSet;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.RTIexception;
import orbisoftware.hlatools.spectrumhlamonitor.solarsystemdemo.MainSharedData;

public class HLAFederateThread extends Thread implements Runnable {

   // All HLA publishers defined here
   private PublishSolarSystem publishSolarSystem = PublishSolarSystem.getInstance();
   
   private PublishPlanetHasCompletedAnOrbit publishPlanetHasCompletedAnOrbit = PublishPlanetHasCompletedAnOrbit.getInstance();

   private RtiFactory factory_;
   private RTIambassador rtiamb_;
   private CommonFederateAmbassador fedamb_;

   public boolean initialized;
   private static boolean shutdown;
   public static boolean shutdownComplete;

   HLAFederateThread() throws RTIexception, MalformedURLException {

      factory_ = RtiFactoryFactory.getRtiFactory();
      rtiamb_ = factory_.getRtiAmbassador();

      fedamb_ = new CommonFederateAmbassador();
      fedamb_.setRtiAmb(rtiamb_);

      initialized = false;
      shutdown = false;
      shutdownComplete = false;
   }

   void createDefaultRegion(RegionHandleSet regions) {

      // Set up a region to send this to
      try {
         DimensionHandle dim1 = rtiamb_.getDimensionHandle("subspace");
         DimensionHandle dim2 = rtiamb_.getDimensionHandle("one");
         DimensionHandle dim3 = rtiamb_.getDimensionHandle("two");

         DimensionHandleSet dims = rtiamb_.getDimensionHandleSetFactory().create();
         dims.add(dim1);
         dims.add(dim2);
         dims.add(dim3);
         
         RegionHandle region = rtiamb_.createRegion(dims);
         RangeBounds rb1 = new RangeBounds(0x00000000L, 0x00000001L);
         rtiamb_.setRangeBounds(region, dim1, rb1);
         RangeBounds rb2 = new RangeBounds(0x00000000L, 0x00000001L);
         rtiamb_.setRangeBounds(region, dim2, rb2);
         RangeBounds rb3 = new RangeBounds(0x00000000L, 0x00000001L);
         rtiamb_.setRangeBounds(region, dim3, rb3);
         
         regions.add(region);
         rtiamb_.commitRegionModifications(regions);

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
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
         rtiamb_.connect(fedamb_, CallbackModel.HLA_EVOKED);
      } catch (RTIexception ex) {
         System.err.println(ex.toString());
         System.exit(-1);
      }

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
         rtiamb_.joinFederationExecution(
               MainSharedData.getInstance().xmlMap.get("Federate"),
               MainSharedData.getInstance().xmlMap.get("Federation"));
      } catch (RTIexception ex) {
         System.err.println(ex.toString());
         System.exit(-1);
      }

      RegionHandleSet regionHandleSet = rtiamb_.getRegionHandleSetFactory().create();
      createDefaultRegion(regionHandleSet);

      publishSolarSystem.performObjectPreExecution(rtiamb_, fedamb_, regionHandleSet);
      publishPlanetHasCompletedAnOrbit.performObjectPreExecution(rtiamb_, fedamb_, regionHandleSet);
      
      while (!shutdown) {

         publishSolarSystem.performObjectExecution();
         publishPlanetHasCompletedAnOrbit.performObjectExecution();

         rtiamb_.evokeMultipleCallbacks(0.001, 0.001);
      }

      // One last invocation for callbacks
      rtiamb_.evokeMultipleCallbacks(0.5, 0.5);
      
      try {
         rtiamb_.resignFederationExecution(ResignAction.DELETE_OBJECTS_THEN_DIVEST);
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

      try {
         rtiamb_.disconnect();
      } catch (RTIexception ex) {
         System.err.println(ex.toString());
         System.exit(-1);
      }

      System.exit(0);
   }
}
