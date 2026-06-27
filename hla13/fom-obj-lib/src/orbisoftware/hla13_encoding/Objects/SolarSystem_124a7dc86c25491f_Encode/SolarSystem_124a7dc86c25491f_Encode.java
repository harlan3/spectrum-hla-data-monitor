package orbisoftware.hla13_encoding.Objects.SolarSystem_124a7dc86c25491f_Encode;

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
public class SolarSystem_124a7dc86c25491f_Encode {

   private Utilities utilities = new Utilities();

   public int objectHandle;
   public AttributeHandleSet attribHandles;
   public boolean hasInitialized = false;
   private int numberAttributes;
   private RTIambassador rtiAmb;
   private Map<Integer, Runnable> decodeActions = new HashMap<>();
   private byte[] currentValue;

   private int planetsAttributeHandle;

   public String getFullyQualifiedObjectName() {

      return "ObjectRoot.SolarSystem";
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

         planetsAttributeHandle = rtiAmb.getAttributeHandle("Planets", objectHandle);
         setupAttributeMappingRun(planetsAttributeHandle);

         attribHandles.add(planetsAttributeHandle);

         hasInitialized = true;
      } catch (Exception e) {
			e.printStackTrace();
      }
   }

   private void setupAttributeMappingRun(int attributeHandle) {

      try {

         if (attributeHandle == rtiAmb.getAttributeHandle("Planets", objectHandle))
            decodeActions.put(attributeHandle,() -> planetsDecode());

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // Class Variable
   private PlanetTypeArray_Encode planets = new PlanetTypeArray_Encode();

   // Setter
   public void setPlanets(PlanetTypeArray_Encode planets) {
      this.planets = planets;
   }

   // Getter
   public PlanetTypeArray_Encode getPlanets() {
      return planets;
   }

   // Encode outgoing data obtained from source class variable definitions to destination SuppliedAttributes
   public void encode(SuppliedAttributes theAttributeValues) {

      // ID: 12d8296c87e6a7e6  Array
      if (planets != null) {

         DynamicBuffer dynamicBuffer = new DynamicBuffer();
         planets.encode(dynamicBuffer, planets.getAlignment());
         theAttributeValues.add(planetsAttributeHandle, dynamicBuffer.getWrittenBytes());
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

   // ID: 12d8296c87e6a7e6  Array
   // Decode incoming data byte array to destination class variable definitions
   private void planetsDecode() {

      DynamicBuffer dynamicBuffer = new DynamicBuffer();
      dynamicBuffer.clear();
      dynamicBuffer.put(currentValue);
      dynamicBuffer.flip();
      planets.decode(dynamicBuffer, planets.getAlignment());
   }

}
