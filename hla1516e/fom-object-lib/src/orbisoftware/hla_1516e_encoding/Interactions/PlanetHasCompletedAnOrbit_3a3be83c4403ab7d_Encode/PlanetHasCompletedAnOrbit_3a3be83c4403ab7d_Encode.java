package orbisoftware.hla_1516e_encoding.Interactions.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import hla.rti1516e.*;
import hla.rti1516e.encoding.*;
import orbisoftware.hla_shared.*;

import orbisoftware.hla_1516e_encoding.Common.Enums.*;
import orbisoftware.hla_1516e_encoding.Common.FixedArrays.*;
import orbisoftware.hla_1516e_encoding.Common.FixedRecords.*;
import orbisoftware.hla_1516e_encoding.Common.LengthlessArrays.*;
import orbisoftware.hla_1516e_encoding.Common.NullTerminatedArrays.*;
import orbisoftware.hla_1516e_encoding.Common.PrefixedStringLength.*;
import orbisoftware.hla_1516e_encoding.Common.VariableArrays.*;
import orbisoftware.hla_1516e_encoding.Common.VariantRecords.*;
import orbisoftware.hla_1516e_encoding.Common.Misc.*;

@SuppressWarnings("unused")
public class PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode {

   private Utilities utilities = new Utilities();

   public InteractionClassHandle interactionHandle;
   public boolean hasInitialized = false;
   private int numberParameters;
   private RTIambassador rtiAmb;
   private Map<Integer, Runnable> decodeActions = new HashMap<>();
   private Map.Entry currentMapEntry;

   private ParameterHandle planetIDParameterHandle;
   private ParameterHandle planetNameParameterHandle;


   public String getFullyQualifiedInteractionName() {

      return "HLAinteractionRoot.PlanetHasCompletedAnOrbit";
   }

   public int getNumberParameters() {

      return numberParameters;
   }

   public void initialize(RTIambassador rtiAmb) {

      try {

         numberParameters = 2;
         this.rtiAmb = rtiAmb;
         interactionHandle = rtiAmb.getInteractionClassHandle(getFullyQualifiedInteractionName());

         planetIDParameterHandle = rtiAmb.getParameterHandle(interactionHandle, "PlanetID");
         setupParameterMappingRun(planetIDParameterHandle);

         planetNameParameterHandle = rtiAmb.getParameterHandle(interactionHandle, "PlanetName");
         setupParameterMappingRun(planetNameParameterHandle);

         hasInitialized = true;
      } catch (Exception e) {
			e.printStackTrace();
      }
   }

   private void setupParameterMappingRun(ParameterHandle parameterHandle) {

      try {

         if (parameterHandle.equals(rtiAmb.getParameterHandle(interactionHandle, "PlanetID")))
            decodeActions.put(parameterHandle.hashCode(),() -> planetIDDecode());

         if (parameterHandle.equals(rtiAmb.getParameterHandle(interactionHandle, "PlanetName")))
            decodeActions.put(parameterHandle.hashCode(),() -> planetNameDecode());

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

   // Encode outgoing data obtained from source class variable definitions to destination ParameterHandleValueMap
   public void encode(ParameterHandleValueMap theParameterValues) {

      // ID: ee8abadf5f871977  Basic
      theParameterValues.put(planetIDParameterHandle, utilities.getBytesFromInteger(planetID));

      // ID: c2477ca15c890656  Array
      if (planetName != null) {

         DynamicBuffer dynamicBuffer = new DynamicBuffer();
         planetName.encode(dynamicBuffer, planetName.getAlignment());
         theParameterValues.put(planetNameParameterHandle, dynamicBuffer.getWrittenBytes());
      }

   }

   // Decode incoming data obtained from source ParameterHandleValueMap to destination class variable definitions
   public void decode(ParameterHandleValueMap theParameterValues) {

      try {

      RtiFactory factory_ = RtiFactoryFactory.getRtiFactory();
      EncoderFactory encfact = factory_.getEncoderFactory();
      Utilities utilities = new Utilities();

         for (Iterator it = theParameterValues.entrySet().iterator(); it.hasNext();) {
            Map.Entry mapEntry = (Map.Entry) it.next();
            ParameterHandle param = (ParameterHandle) mapEntry.getKey();

            if (param != null) {

               int hashCode = param.hashCode();
               currentMapEntry = mapEntry;

               if (decodeActions.containsKey(hashCode))
                  decodeActions.get(hashCode).run();
            }
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
      dynamicBuffer.put((byte []) currentMapEntry.getValue());
      planetID = utilities.getIntegerFromBytes(dynamicBuffer.getWrittenBytes());
   }

   // ID: c2477ca15c890656  Array
   // Decode incoming data byte array to destination class variable definitions
   private void planetNameDecode() {

      DynamicBuffer dynamicBuffer = new DynamicBuffer();
      dynamicBuffer.clear();
      dynamicBuffer.put((byte []) currentMapEntry.getValue());
      dynamicBuffer.flip();
      planetName.decode(dynamicBuffer, planetName.getAlignment());
   }

}
