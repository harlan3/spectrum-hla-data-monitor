package orbisoftware.hla13_encoding.Interactions.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode;

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
public class PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode {

   private Utilities utilities = new Utilities();

   public int interactionHandle;
   public boolean hasInitialized = false;
   private int numberParameters;
   private RTIambassador rtiAmb;
   private Map<Integer, Runnable> decodeActions = new HashMap<>();
   private byte[] currentValue;

   private int planetIDParameterHandle;
   private int planetNameParameterHandle;


   public String getFullyQualifiedInteractionName() {

      return "InteractionRoot.PlanetHasCompletedAnOrbit";
   }

   public int getNumberParameters() {

      return numberParameters;
   }

   public void initialize(RTIambassador rtiAmb) {

      try {

         numberParameters = 2;
         this.rtiAmb = rtiAmb;
         interactionHandle = rtiAmb.getInteractionClassHandle(getFullyQualifiedInteractionName());

         planetIDParameterHandle = rtiAmb.getParameterHandle("PlanetID", interactionHandle);
         setupParameterMappingRun(planetIDParameterHandle);

         planetNameParameterHandle = rtiAmb.getParameterHandle("PlanetName", interactionHandle);
         setupParameterMappingRun(planetNameParameterHandle);

         hasInitialized = true;
      } catch (Exception e) {
			e.printStackTrace();
      }
   }

   private void setupParameterMappingRun(int parameterHandle) {

      try {

         if (parameterHandle == rtiAmb.getParameterHandle("PlanetID", interactionHandle))
            decodeActions.put(parameterHandle,() -> planetIDDecode());

         if (parameterHandle == rtiAmb.getParameterHandle("PlanetName", interactionHandle))
            decodeActions.put(parameterHandle,() -> planetNameDecode());

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // Class Variable
   private int planetID;

   // Setter
   public void setPlanetID(int planetID) {
      this.planetID = planetID;
   }

   // Getter
   public int getPlanetID() {
      return planetID;
   }

   // Class Variable
   private HLAASCIIstringImp_Encode planetName = new HLAASCIIstringImp_Encode();

   // Setter
   public void setPlanetName(HLAASCIIstringImp_Encode planetName) {
      this.planetName = planetName;
   }

   // Getter
   public HLAASCIIstringImp_Encode getPlanetName() {
      return planetName;
   }

   // Encode outgoing data obtained from source class variable definitions to destination SuppliedParameters
   public void encode(SuppliedParameters theParameterValues) {

      // ID: ee8abadf5f871977  Basic
      theParameterValues.add(planetIDParameterHandle, utilities.getBytesFromInteger(planetID));

      // ID: c2477ca15c890656  Array
      if (planetName != null) {

         DynamicBuffer dynamicBuffer = new DynamicBuffer();
         planetName.encode(dynamicBuffer, planetName.getAlignment());
         theParameterValues.add(planetNameParameterHandle, dynamicBuffer.getWrittenBytes());
      }

   }

   // Decode incoming data obtained from source ReceivedInteraction to destination class variable definitions
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

   // ID: ee8abadf5f871977  Basic
   // Decode incoming data byte array to destination class variable definitions
   private void planetIDDecode() {

      DynamicBuffer dynamicBuffer = new DynamicBuffer();
      dynamicBuffer.clear();
      dynamicBuffer.put(currentValue);
      planetID = utilities.getIntegerFromBytes(dynamicBuffer.getWrittenBytes());
   }

   // ID: c2477ca15c890656  Array
   // Decode incoming data byte array to destination class variable definitions
   private void planetNameDecode() {

      DynamicBuffer dynamicBuffer = new DynamicBuffer();
      dynamicBuffer.clear();
      dynamicBuffer.put(currentValue);
      dynamicBuffer.flip();
      planetName.decode(dynamicBuffer, planetName.getAlignment());
   }

}
