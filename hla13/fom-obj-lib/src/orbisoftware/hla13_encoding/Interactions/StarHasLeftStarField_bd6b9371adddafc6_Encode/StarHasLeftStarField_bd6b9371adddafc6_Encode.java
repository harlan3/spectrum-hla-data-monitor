package orbisoftware.hla13_encoding.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Encode;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import hla.rti.*;
import orbisoftware.hla_shared.*;

import orbisoftware.hla13_encoding.Common.Enums.*;
import orbisoftware.hla13_encoding.Common.FixedArrays.*;
import orbisoftware.hla13_encoding.Common.FixedRecords.*;
import orbisoftware.hla13_encoding.Common.LengthlessArrays.*;
import orbisoftware.hla13_encoding.Common.NullTerminatedArrays.*;
import orbisoftware.hla13_encoding.Common.PrefixedStringLength.*;
import orbisoftware.hla13_encoding.Common.VariableArrays.*;
import orbisoftware.hla13_encoding.Common.VariantRecords.*;
import orbisoftware.hla13_encoding.Common.Misc.*;

@SuppressWarnings("unused")
public class StarHasLeftStarField_bd6b9371adddafc6_Encode {

   private Utilities utilities = new Utilities();

   public int interactionHandle;
   public boolean hasInitialized = false;
   private int numberParameters;
   private RTIambassador rtiAmb;
   private Map<Integer, Runnable> decodeActions = new HashMap<>();
   private byte[] currentValue;

   private int starIDParameterHandle;
   private int starNameParameterHandle;


   public String getFullyQualifiedInteractionName() {

      return "InteractionRoot.StarHasLeftStarField";
   }

   public int getNumberParameters() {

      return numberParameters;
   }

   public void initialize(RTIambassador rtiAmb) {

      try {

         numberParameters = 2;
         this.rtiAmb = rtiAmb;
         interactionHandle = rtiAmb.getInteractionClassHandle(getFullyQualifiedInteractionName());

         starIDParameterHandle = rtiAmb.getParameterHandle("StarID", interactionHandle);
         setupParameterMappingRun(starIDParameterHandle);

         starNameParameterHandle = rtiAmb.getParameterHandle("StarName", interactionHandle);
         setupParameterMappingRun(starNameParameterHandle);

         hasInitialized = true;
      } catch (Exception e) {
			e.printStackTrace();
      }
   }

   private void setupParameterMappingRun(int parameterHandle) {

      try {

         if (parameterHandle == rtiAmb.getParameterHandle("StarID", interactionHandle))
            decodeActions.put(parameterHandle,() -> starIDDecode());

         if (parameterHandle == rtiAmb.getParameterHandle("StarName", interactionHandle))
            decodeActions.put(parameterHandle,() -> starNameDecode());

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // Class Variable
   private int starID;

   // Setter
   public void setStarID(int starID) {
      this.starID = starID;
   }

   // Getter
   public int getStarID() {
      return starID;
   }

   // Class Variable
   private NullTerminatedASCIIStringImp_Encode starName = new NullTerminatedASCIIStringImp_Encode();

   // Setter
   public void setStarName(NullTerminatedASCIIStringImp_Encode starName) {
      this.starName = starName;
   }

   // Getter
   public NullTerminatedASCIIStringImp_Encode getStarName() {
      return starName;
   }

   // Encode outgoing data obtained from source class variable definitions to destination ParameterHandleValueMap
   public void encode(SuppliedParameters theParameterValues) {

      // ID: 8eadbf9b0552f322  Basic
      theParameterValues.add(starIDParameterHandle, utilities.getBytesFromInteger(starID));

      // ID: 295856e9bd1c2541  Array
      if (starName != null) {

         DynamicBuffer dynamicBuffer = new DynamicBuffer();
         starName.encode(dynamicBuffer, starName.getAlignment());
         theParameterValues.add(starNameParameterHandle, dynamicBuffer.getWrittenBytes());
      }

   }

   // Decode incoming data obtained from source ParameterHandleValueMap to destination class variable definitions
   public void decode(ReceivedInteraction theParameterValues) {

		try {

			for (int i = 0; i < theParameterValues.size(); i++) {
				int parameterHandle = theParameterValues.getParameterHandle(i);
				currentValue = theParameterValues.getValue(i);

				Runnable decodeAction = decodeActions.get(parameterHandle);
				if (decodeAction != null)
					decodeAction.run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ID: 14bff4c3499df288 Basic
	// Decode incoming data byte array to destination class variable definitions
	private void starIDDecode() {

		DynamicBuffer dynamicBuffer = new DynamicBuffer();
		dynamicBuffer.clear();
		dynamicBuffer.put(currentValue);
		starID = utilities.getIntegerFromBytes(dynamicBuffer.getWrittenBytes());
	}

	// ID: a5da5bf1d0283e8f Array
	// Decode incoming data byte array to destination class variable definitions
	private void starNameDecode() {

		DynamicBuffer dynamicBuffer = new DynamicBuffer();
		dynamicBuffer.clear();
		dynamicBuffer.put(currentValue);
		dynamicBuffer.flip();
		starName.decode(dynamicBuffer, starName.getAlignment());
	}

}
