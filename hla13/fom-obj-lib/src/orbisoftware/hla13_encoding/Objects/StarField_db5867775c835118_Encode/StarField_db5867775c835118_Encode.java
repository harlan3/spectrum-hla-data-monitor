package orbisoftware.hla13_encoding.Objects.StarField_db5867775c835118_Encode;

import java.util.Map;
import java.util.HashMap;

import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
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
public class StarField_db5867775c835118_Encode {

   private Utilities utilities = new Utilities();

   public int objectHandle;
   public AttributeHandleSet attribHandles;
   public boolean hasInitialized = false;
   private int numberAttributes;
   private RTIambassador rtiAmb;
   private Map<Integer, Runnable> decodeActions = new HashMap<>();
   private byte[] currentValue;

   private int starsAttributeHandle;

   public String getFullyQualifiedObjectName() {

      return "ObjectRoot.StarField";
   }

   public int getNumberAttributes() {

      return numberAttributes;
   }

   public void initialize(RTIambassador rtiAmb) {

      try {

         numberAttributes = 1;
         this.rtiAmb = rtiAmb;
         objectHandle = rtiAmb.getObjectClassHandle(getFullyQualifiedObjectName());
         attribHandles = RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();

         starsAttributeHandle = rtiAmb.getAttributeHandle("Stars", objectHandle);
         setupAttributeMappingRun(starsAttributeHandle);

         attribHandles.add(starsAttributeHandle);

         hasInitialized = true;
      } catch (Exception e) {
			e.printStackTrace();
      }
   }

   private void setupAttributeMappingRun(int attributeHandle) {

      try {

         if (attributeHandle == rtiAmb.getAttributeHandle("Stars", objectHandle))
            decodeActions.put(attributeHandle,() -> starsDecode());

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

   // Encode outgoing data obtained from source class variable definitions to destination SuppliedAttributes
   public void encode(SuppliedAttributes theAttributeValues) {

      // ID: 28376bfea3206703  Array
      if (stars != null) {

         DynamicBuffer dynamicBuffer = new DynamicBuffer();
         stars.encode(dynamicBuffer, stars.getAlignment());
         theAttributeValues.add(starsAttributeHandle, dynamicBuffer.getWrittenBytes());
      }

   }

   // Decode incoming data obtained from source ReflectedAttributes to destination class variable definitions
   public void decode(ReflectedAttributes theAttributeValues) {

      try {

         for (int i = 0; i < theAttributeValues.size(); i++) {
            int attributeHandle = theAttributeValues.getAttributeHandle(i);
            currentValue = theAttributeValues.getValue(i);

            Runnable decodeAction = decodeActions.get(attributeHandle);
            if (decodeAction != null)
               decodeAction.run();
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
      dynamicBuffer.put(currentValue);
      dynamicBuffer.flip();
      stars.decode(dynamicBuffer, stars.getAlignment());
   }

}
