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

import hla.rti.RTIambassador;
import hla.rti.Region;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.RtiFactoryFactory;
import orbisoftware.hla13_containers.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Cont.StarHasLeftStarField_bd6b9371adddafc6_Cont;
import orbisoftware.hla13_encoding.Common.NullTerminatedArrays.NullTerminatedASCIIStringImp_Encode;
import orbisoftware.hla13_encoding.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Encode.StarHasLeftStarField_bd6b9371adddafc6_Encode;
import orbisoftware.hla_shared.Utilities;

public class PublishStarHasLeftStarField {

    private int interactionHandle;

    private RTIambassador rtiAmb;
    private CommonFederateAmbassador fedAmb;
    private Region defaultRegion;
    private StarHasLeftStarField_bd6b9371adddafc6_Encode starHasLeftStarField;

    private static PublishStarHasLeftStarField single_instance = null;

    public static synchronized PublishStarHasLeftStarField getInstance() {
        if (single_instance == null) {
            single_instance = new PublishStarHasLeftStarField();
        }
        return single_instance;
    }

    private PublishStarHasLeftStarField() {
    }

    public void performObjectPreExecution(RTIambassador rtiAmb, CommonFederateAmbassador fedAmb,
            Region defaultRegion) {
        this.rtiAmb = rtiAmb;
        this.fedAmb = fedAmb;
        this.defaultRegion = defaultRegion;

        starHasLeftStarField = new StarHasLeftStarField_bd6b9371adddafc6_Encode();
        starHasLeftStarField.initialize(rtiAmb);

        try {
            interactionHandle =
                    rtiAmb.getInteractionClassHandle(starHasLeftStarField.getFullyQualifiedInteractionName());
            rtiAmb.publishInteractionClass(interactionHandle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performObjectExecution() {
    }

    public void sideLoadPublishSample(Object objectRef) {
        StarHasLeftStarField_bd6b9371adddafc6_Cont container =
                (StarHasLeftStarField_bd6b9371adddafc6_Cont) objectRef;
        Utilities utilities = new Utilities();
        byte[] tag = utilities.createTimestampTag();

        if (rtiAmb == null) {
            return;
        }

        SuppliedParameters parameters;
        try {
            parameters = RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        starHasLeftStarField.setStarID(container.starID);

        NullTerminatedASCIIStringImp_Encode nullTerminatedASCIIString = new NullTerminatedASCIIStringImp_Encode();
        nullTerminatedASCIIString.setValue(container.starName.value.getBytes());
        starHasLeftStarField.setStarName(nullTerminatedASCIIString);

        starHasLeftStarField.encode(parameters);

        try {
            rtiAmb.sendInteraction(interactionHandle, parameters, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
