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
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package orbisoftware.hlatools.spectrumhlamonitor.hla_sender;

import java.net.MalformedURLException;
import java.net.URL;

import hla.rti.FederatesCurrentlyJoined;
import hla.rti.FederationExecutionAlreadyExists;
import hla.rti.RTIambassador;
import hla.rti.RTIexception;
import hla.rti.Region;
import hla.rti.ResignAction;
import hla.rti.jlc.RtiFactory;
import hla.rti.jlc.RtiFactoryFactory;
import orbisoftware.hlatools.spectrumhlamonitor.starfielddemo.MainSharedData;

public class HLAFederateThread extends Thread implements Runnable {

    /*
     * The HLA 1.3 FED must define this routing space and the dimensions
     * "subspace", "one", and "two". Change this value only if the converted
     * FED uses a different routing-space name.
     */
    private static final String DEFAULT_ROUTING_SPACE_NAME = "Space";

    private final PublishStarField publishStarField = PublishStarField.getInstance();
    private final PublishStarHasLeftStarField publishStarHasLeftStarField =
            PublishStarHasLeftStarField.getInstance();

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
            rtiamb_.joinFederationExecution(MainSharedData.getInstance().xmlMap.get("Federate"),
                    MainSharedData.getInstance().xmlMap.get("Federation"), fedamb_);
        } catch (RTIexception ex) {
            System.err.println(ex.toString());
            System.exit(-1);
        }

        Object regionHandleSet = null;
        createDefaultRegion(regionHandleSet);

        publishStarField.performObjectPreExecution(rtiamb_, fedamb_, (Region) regionHandleSet);
        publishStarHasLeftStarField.performObjectPreExecution(rtiamb_, fedamb_, (Region) regionHandleSet);

        while (!shutdown) {
            publishStarField.performObjectExecution();
            publishStarHasLeftStarField.performObjectExecution();

            rtiamb_.tick();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
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
            // A peer federate is still joined.
        } catch (RTIexception ex) {
            System.err.println(ex.toString());
            System.exit(-1);
        }

        System.exit(0);
    }
}
