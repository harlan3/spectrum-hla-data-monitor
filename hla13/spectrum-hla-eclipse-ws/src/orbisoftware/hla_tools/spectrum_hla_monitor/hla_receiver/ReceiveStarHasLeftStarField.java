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

import hla.rti.*;
import java.beans.PropertyChangeSupport;

import orbisoftware.hla13_containers.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Cont.StarHasLeftStarField_bd6b9371adddafc6_Cont;
import orbisoftware.hla13_encoding.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Encode.StarHasLeftStarField_bd6b9371adddafc6_Encode;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLADataLogger;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLAReadEventHandler;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLASamples;
import orbisoftware.hla_tools.spectrum_hla_monitor.HLATopic;

public class ReceiveStarHasLeftStarField {

   private int interactionHandle;
	
	public PropertyChangeSupport readEventSample;
	
	private static ReceiveStarHasLeftStarField single_instance = null;
	
	private HLATopic hlaTopic = null;
	private long initialStartTime = 0;
	public boolean autoUpdateEnabled = false;
	private StarHasLeftStarField_bd6b9371adddafc6_Encode starHasLeftStarField;
	
   public static synchronized ReceiveStarHasLeftStarField getInstance()
   {
       if (single_instance == null)
           single_instance = new ReceiveStarHasLeftStarField();

       return single_instance;
   }
   
	private ReceiveStarHasLeftStarField() {

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

	   starHasLeftStarField = new StarHasLeftStarField_bd6b9371adddafc6_Encode();

		try {
		   
		   starHasLeftStarField.initialize(rtiAmb);
         
		   interactionHandle = starHasLeftStarField.interactionHandle;
		   
		   rtiAmb.subscribeInteractionClass(interactionHandle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

   public void receiveInteractionCallback(
         ReceivedInteraction theParameterValues, byte[] userSuppliedTag) {

      StarHasLeftStarField_bd6b9371adddafc6_Cont container = new StarHasLeftStarField_bd6b9371adddafc6_Cont();

      starHasLeftStarField.decode(theParameterValues);

      container.starID = starHasLeftStarField.getStarID();
      container.starName.value = starHasLeftStarField.getStarName().getString();

      if (autoUpdateEnabled) {

         HLASamples hlaSamples = new HLASamples();
         hlaSamples.sampleReadTime = System.currentTimeMillis()
               - initialStartTime;
         hlaSamples.seqHolderName = new String(hlaTopic.name);
         hlaSamples.seqHolder = container;

         readEventSample.firePropertyChange("hlaReadEvent", 0, hlaSamples);
      }
   }
}
