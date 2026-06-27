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

import java.util.ArrayList;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.AttributeRegionAssociation;
import hla.rti1516e.AttributeSetRegionSetPairList;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RegionHandleSet;
import orbisoftware.hla_1516e_encoding.Common.FixedArrays.StarTypeArray_Encode;
import orbisoftware.hla_1516e_encoding.Common.FixedRecords.StarType_Encode;
import orbisoftware.hla_1516e_encoding.Common.PrefixedStringLength.HLAASCIIstringImp_Encode;
import orbisoftware.hla_1516e_encoding.Objects.StarField_db5867775c835118_Encode.StarField_db5867775c835118_Encode;
import orbisoftware.hla_shared.Utilities;
import orbisoftware.hlatools.spectrumhlamonitor.starfielddemo.StarModel;

public class PublishStarField {

	private ObjectInstanceHandle objectInstanceHandle;

	private RTIambassador rtiAmb;
	private CommonFederateAmbassador fedAmb;
	private RegionHandleSet defaultRegionSet;
	private StarField_db5867775c835118_Encode starField;

	private static PublishStarField single_instance = null;

	public static synchronized PublishStarField getInstance() {
		if (single_instance == null)
			single_instance = new PublishStarField();

		return single_instance;
	}

	public PublishStarField() {

	}

	public void performObjectPreExecution(RTIambassador rtiAmb, CommonFederateAmbassador fedAmb,
			RegionHandleSet defaultRegionSet) {

		this.rtiAmb = rtiAmb;
		this.fedAmb = fedAmb;
		this.defaultRegionSet = defaultRegionSet;

		starField = new StarField_db5867775c835118_Encode();
		starField.initialize(rtiAmb);

		try {

			rtiAmb.publishObjectClassAttributes(starField.objectHandle, starField.attribHandles);

			AttributeSetRegionSetPairList attrHandleSetPairList = rtiAmb.getAttributeSetRegionSetPairListFactory()
					.create(1);
			AttributeRegionAssociation attrHandleSetPair = new AttributeRegionAssociation(starField.attribHandles,
					defaultRegionSet);
			attrHandleSetPairList.add(attrHandleSetPair);

			objectInstanceHandle = rtiAmb.registerObjectInstanceWithRegions(starField.objectHandle,
					attrHandleSetPairList);
			fedAmb.instanceHandleToClassHandleMap.put(objectInstanceHandle.hashCode(), starField.objectHandle);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void performObjectExecution() {

	}

	public void sideLoadPublishSample(ArrayList<StarModel> starFieldList) {

		AttributeHandleValueMap attributes = null;
		Utilities utilities = new Utilities();

		byte[] tag = utilities.createTimestampTag();

		if (rtiAmb == null)
			return;

		try {
			attributes = rtiAmb.getAttributeHandleValueMapFactory().create(starField.getNumberAttributes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StarTypeArray_Encode starTypeEncode = new StarTypeArray_Encode();

		for (int i = 0; i < starTypeEncode.cardinality; i++) {

			StarType_Encode starType = starTypeEncode.getStarType_Encode(i);

			starType.setStarID(starFieldList.get(i).hlaStar.starID);
			starType.setBaseSize(starFieldList.get(i).hlaStar.baseSize);
			starType.setXpos(starFieldList.get(i).hlaStar.xPos);
			starType.setYpos(starFieldList.get(i).hlaStar.yPos);
			starType.setProjX(starFieldList.get(i).hlaStar.projX);
			starType.setProjY(starFieldList.get(i).hlaStar.projY);
			starType.setProjZ(starFieldList.get(i).hlaStar.projZ);
			starType.setSize(starFieldList.get(i).hlaStar.size);
			starType.setSpeed(starFieldList.get(i).hlaStar.speed);
			starType.setBlinkSpeed(starFieldList.get(i).hlaStar.blinkSpeed);
			starType.setBlinkPhase(starFieldList.get(i).hlaStar.blinkPhase);
			starType.setAlpha(starFieldList.get(i).hlaStar.alpha);
		}

		starField.setStars(starTypeEncode);
		starField.encode(attributes);

		try {

			rtiAmb.updateAttributeValues(objectInstanceHandle, attributes, tag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
