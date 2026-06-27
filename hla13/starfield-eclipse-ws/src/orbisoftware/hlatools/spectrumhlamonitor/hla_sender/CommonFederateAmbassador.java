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

import java.util.ArrayList;
import java.util.HashMap;

import hla.rti.EventRetractionHandle;
import hla.rti.FederateInternalError;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
import hla.rti.ReflectedAttributes;
import hla.rti.jlc.NullFederateAmbassador;

/**
 * HLA 1.3 federate ambassador. HLA 1.3 uses integer handles and its
 * ReflectedAttributes/ReceivedInteraction callback containers.
 */
public class CommonFederateAmbassador extends NullFederateAmbassador {

    private final ArrayList<Integer> objs;
    public final HashMap<Integer, Integer> instanceHandleToClassHandleMap;
    private boolean debug = false;

    CommonFederateAmbassador() {
        objs = new ArrayList<Integer>();
        instanceHandleToClassHandleMap = new HashMap<Integer, Integer>();
    }

    @Override
    public void discoverObjectInstance(int theObject, int theObjectClass, String objectName)
            throws FederateInternalError {
        instanceHandleToClassHandleMap.put(theObject, theObjectClass);
        objs.add(theObject);
    }

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] userSuppliedTag)
            throws FederateInternalError {
        if (debug) {
            System.out.println("reflectAttributeValues receive-order");
        }
    }

    @Override
    public void reflectAttributeValues(int theObject, ReflectedAttributes theAttributes, byte[] userSuppliedTag,
            LogicalTime theTime, EventRetractionHandle retractionHandle) throws FederateInternalError {
        if (debug) {
            System.out.println("reflectAttributeValues timestamp-order");
        }
    }

    @Override
    public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] userSuppliedTag)
            throws FederateInternalError {
        if (debug) {
            System.out.println("receiveInteraction receive-order");
        }
    }

    @Override
    public void receiveInteraction(int interactionClass, ReceivedInteraction theInteraction, byte[] userSuppliedTag,
            LogicalTime theTime, EventRetractionHandle eventRetractionHandle) throws FederateInternalError {
        if (debug) {
            System.out.println("receiveInteraction timestamp-order");
        }
    }

    @Override
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag) throws FederateInternalError {
        objs.remove(Integer.valueOf(theObject));
        instanceHandleToClassHandleMap.remove(theObject);
    }

    @Override
    public void removeObjectInstance(int theObject, byte[] userSuppliedTag, LogicalTime theTime,
            EventRetractionHandle retractionHandle) throws FederateInternalError {
        objs.remove(Integer.valueOf(theObject));
        instanceHandleToClassHandleMap.remove(theObject);
    }
}
