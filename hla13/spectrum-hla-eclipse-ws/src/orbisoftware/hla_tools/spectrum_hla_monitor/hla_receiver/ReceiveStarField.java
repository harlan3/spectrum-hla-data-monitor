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

package orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver;

import java.beans.PropertyChangeSupport;

import hla.rti.*;
import orbisoftware.hla13_containers.Objects.StarField_db5867775c835118_Cont.StarField_db5867775c835118_Cont;
import orbisoftware.hla13_encoding.Common.FixedArrays.StarTypeArray_Encode;
import orbisoftware.hla13_encoding.Common.FixedRecords.StarType_Encode;
import orbisoftware.hla13_encoding.Objects.StarField_db5867775c835118_Encode.StarField_db5867775c835118_Encode;
import orbisoftware.hla_shared.Utilities;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLADataLogger;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLAReadEventHandler;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLASamples;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLATopic;

public class ReceiveStarField {
	
	private PropertyChangeSupport readEventSample;

	private static ReceiveStarField single_instance = null;
	
	private HLATopic hlaTopic = null;
	private long initialStartTime = 0;
	public boolean autoUpdateEnabled = false;
	private StarField_db5867775c835118_Encode starField = new StarField_db5867775c835118_Encode();
	
	public static synchronized ReceiveStarField getInstance()
   {
       if (single_instance == null)
           single_instance = new ReceiveStarField();

       return single_instance;
   }
   
	public ReceiveStarField() {

	}
	
   public void initialize(HLATopic hlaTopic) {

      readEventSample = new PropertyChangeSupport(this);

      hlaTopic.readEventHandler = new HLAReadEventHandler(hlaTopic);
      readEventSample.addPropertyChangeListener(hlaTopic.readEventHandler);
      readEventSample.addPropertyChangeListener(hlaTopic.readEventHandler.receiveListGUI);
      hlaTopic.readEventHandler.start();

      hlaTopic.dataLogger = new HLADataLogger(hlaTopic);
      readEventSample.addPropertyChangeListener(hlaTopic.dataLogger);
      hlaTopic.dataLogger.start();

      initialStartTime = System.currentTimeMillis();

      this.hlaTopic = hlaTopic;
   }
	
	public void performObjectPreExecution(RTIambassador rtiAmb, CommonFederateAmbassador fedAmb,
	      Object defaultRegionSet) {

	   starField = new StarField_db5867775c835118_Encode();

		try {
		   
		   starField.initialize(rtiAmb);
		   
         rtiAmb.subscribeObjectClassAttributes(starField.objectHandle, starField.attribHandles);
      
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveAttributeUpdateCallback(ReflectedAttributes theAttributeValues, byte[] userSuppliedTag) {

	   StarField_db5867775c835118_Cont container = new StarField_db5867775c835118_Cont();
	   
	   starField.decode(theAttributeValues);
	   
	   StarTypeArray_Encode starTypeArray = starField.getStars();
	   
	   for (int i=0; i < starTypeArray.cardinality; i++) {
	      
	      StarType_Encode star = starTypeArray.getStarType_Encode(i);
	   
	      container.stars.value[i].starID = star.getStarID();
	      container.stars.value[i].baseSize = star.getBaseSize();
	      container.stars.value[i].xpos = star.getXpos();
	      container.stars.value[i].ypos = star.getYpos();
	      container.stars.value[i].projX = star.getProjX();
	      container.stars.value[i].projY = star.getProjY();
	      container.stars.value[i].projZ = star.getProjZ();
	      container.stars.value[i].size = star.getSize();
	      container.stars.value[i].speed = star.getSpeed();
	      container.stars.value[i].blinkSpeed = star.getBlinkSpeed();
	      container.stars.value[i].blinkPhase = star.getBlinkPhase();
	      container.stars.value[i].alpha = star.getAlpha();
	      
	   }
      
      if (autoUpdateEnabled) {

         HLASamples hlaSamples = new HLASamples();
         hlaSamples.sampleReadTime = System.currentTimeMillis() - initialStartTime;
         hlaSamples.seqHolderName = new String(hlaTopic.name);
         hlaSamples.seqHolder = container;

         readEventSample.firePropertyChange("hlaReadEvent", 0, hlaSamples);
      }
	}
}
