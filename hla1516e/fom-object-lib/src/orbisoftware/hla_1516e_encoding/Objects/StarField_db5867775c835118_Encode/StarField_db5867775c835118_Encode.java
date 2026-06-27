package orbisoftware.hla_1516e_encoding.Objects.StarField_db5867775c835118_Encode;

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
public class StarField_db5867775c835118_Encode {

   private Utilities utilities = new Utilities();

   public ObjectClassHandle objectHandle;
   public AttributeHandleSet attribHandles;
   public boolean hasInitialized = false;
   private int numberAttributes;
   private RTIambassador rtiAmb;
   private Map<Integer, Runnable> decodeActions = new HashMap<>();
   private Map.Entry currentMapEntry;

   private AttributeHandle starsAttributeHandle;

   public String getFullyQualifiedObjectName() {

      return "HLAobjectRoot.StarField";
   }

   public int getNumberAttributes() {

      return numberAttributes;
   }

   public void initialize(RTIambassador rtiAmb) {

      try {

         numberAttributes = 1;
         this.rtiAmb = rtiAmb;
         objectHandle = rtiAmb.getObjectClassHandle(getFullyQualifiedObjectName());
         attribHandles = rtiAmb.getAttributeHandleSetFactory().create();

         starsAttributeHandle = rtiAmb.getAttributeHandle(objectHandle, "Stars");
         setupAttributeMappingRun(starsAttributeHandle);

         attribHandles.add(starsAttributeHandle);

         hasInitialized = true;
      } catch (Exception e) {
			e.printStackTrace();
      }
   }

   private void setupAttributeMappingRun(AttributeHandle attributeHandle) {

      try {

         if (attributeHandle.equals(rtiAmb.getAttributeHandle(objectHandle, "Stars")))
            decodeActions.put(attributeHandle.hashCode(),() -> starsDecode());

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // Class Variable
   private StarTypeArray_Encode stars = new StarTypeArray_Encode();

   // Setter
   public void setStars(StarTypeArray_Encode stars) {
      this.stars = stars;
   }

   // Getter
   public StarTypeArray_Encode getStars() {
      return stars;
   }

   // Encode outgoing data obtained from source class variable definitions to destination AttributeHandleValueMap
   public void encode(AttributeHandleValueMap theAttributeValues) {

      // ID: 28376bfea3206703  Array
      if (stars != null) {

         DynamicBuffer dynamicBuffer = new DynamicBuffer();
         stars.encode(dynamicBuffer, stars.getAlignment());
         theAttributeValues.put(starsAttributeHandle, dynamicBuffer.getWrittenBytes());
      }

   }

   // Decode incoming data obtained from source AttributeHandleValueMap to destination class variable definitions
   public void decode(AttributeHandleValueMap theAttributeValues) {

      try {

      RtiFactory factory_ = RtiFactoryFactory.getRtiFactory();
      EncoderFactory encfact = factory_.getEncoderFactory();
      Utilities utilities = new Utilities();

         for (Iterator it = theAttributeValues.entrySet().iterator(); it.hasNext();) {
            Map.Entry mapEntry = (Map.Entry) it.next();
            AttributeHandle attrib = (AttributeHandle) mapEntry.getKey();

            if (attrib != null) {

               int hashCode = attrib.hashCode();
               currentMapEntry = mapEntry;

               if (decodeActions.containsKey(hashCode))
                  decodeActions.get(hashCode).run();
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // ID: 28376bfea3206703  Array
   // Decode incoming data byte array to destination class variable definitions
   private void starsDecode() {

      DynamicBuffer dynamicBuffer = new DynamicBuffer();
      dynamicBuffer.clear();
      dynamicBuffer.put((byte []) currentMapEntry.getValue());
      dynamicBuffer.flip();
      stars.decode(dynamicBuffer, stars.getAlignment());
   }

}
