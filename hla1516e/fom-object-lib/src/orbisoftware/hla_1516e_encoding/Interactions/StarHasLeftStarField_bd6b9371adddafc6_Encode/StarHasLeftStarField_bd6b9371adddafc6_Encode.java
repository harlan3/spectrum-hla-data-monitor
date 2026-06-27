package orbisoftware.hla_1516e_encoding.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Encode;

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
public class StarHasLeftStarField_bd6b9371adddafc6_Encode {

   private Utilities utilities = new Utilities();

   public InteractionClassHandle interactionHandle;
   public boolean hasInitialized = false;
   private int numberParameters;
   private RTIambassador rtiAmb;
   private Map<Integer, Runnable> decodeActions = new HashMap<>();
   private Map.Entry currentMapEntry;

   private ParameterHandle starIDParameterHandle;
   private ParameterHandle starNameParameterHandle;


   public String getFullyQualifiedInteractionName() {

      return "HLAinteractionRoot.StarHasLeftStarField";
   }

   public int getNumberParameters() {

      return numberParameters;
   }

   public void initialize(RTIambassador rtiAmb) {

      try {

         numberParameters = 2;
         this.rtiAmb = rtiAmb;
         interactionHandle = rtiAmb.getInteractionClassHandle(getFullyQualifiedInteractionName());

         starIDParameterHandle = rtiAmb.getParameterHandle(interactionHandle, "StarID");
         setupParameterMappingRun(starIDParameterHandle);

         starNameParameterHandle = rtiAmb.getParameterHandle(interactionHandle, "StarName");
         setupParameterMappingRun(starNameParameterHandle);

         hasInitialized = true;
      } catch (Exception e) {
			e.printStackTrace();
      }
   }

   private void setupParameterMappingRun(ParameterHandle parameterHandle) {

      try {

         if (parameterHandle.equals(rtiAmb.getParameterHandle(interactionHandle, "StarID")))
            decodeActions.put(parameterHandle.hashCode(),() -> starIDDecode());

         if (parameterHandle.equals(rtiAmb.getParameterHandle(interactionHandle, "StarName")))
            decodeActions.put(parameterHandle.hashCode(),() -> starNameDecode());

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
   private HLAASCIIstringImp_Encode starName = new HLAASCIIstringImp_Encode();

   // Setter
   public void setStarName(HLAASCIIstringImp_Encode starName) {
      this.starName = starName;
   }

   // Getter
   public HLAASCIIstringImp_Encode getStarName() {
      return starName;
   }

   // Encode outgoing data obtained from source class variable definitions to destination ParameterHandleValueMap
   public void encode(ParameterHandleValueMap theParameterValues) {

      // ID: 14bff4c3499df288  Basic
      theParameterValues.put(starIDParameterHandle, utilities.getBytesFromInteger(starID));

      // ID: a5da5bf1d0283e8f  Array
      if (starName != null) {

         DynamicBuffer dynamicBuffer = new DynamicBuffer();
         starName.encode(dynamicBuffer, starName.getAlignment());
         theParameterValues.put(starNameParameterHandle, dynamicBuffer.getWrittenBytes());
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

   // ID: 14bff4c3499df288  Basic
   // Decode incoming data byte array to destination class variable definitions
   private void starIDDecode() {

      DynamicBuffer dynamicBuffer = new DynamicBuffer();
      dynamicBuffer.clear();
      dynamicBuffer.put((byte []) currentMapEntry.getValue());
      starID = utilities.getIntegerFromBytes(dynamicBuffer.getWrittenBytes());
   }

   // ID: a5da5bf1d0283e8f  Array
   // Decode incoming data byte array to destination class variable definitions
   private void starNameDecode() {

      DynamicBuffer dynamicBuffer = new DynamicBuffer();
      dynamicBuffer.clear();
      dynamicBuffer.put((byte []) currentMapEntry.getValue());
      dynamicBuffer.flip();
      starName.decode(dynamicBuffer, starName.getAlignment());
   }

}
