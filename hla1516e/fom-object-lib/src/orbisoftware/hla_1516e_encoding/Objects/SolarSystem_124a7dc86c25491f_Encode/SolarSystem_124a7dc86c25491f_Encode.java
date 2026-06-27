package orbisoftware.hla_1516e_encoding.Objects.SolarSystem_124a7dc86c25491f_Encode;

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
public class SolarSystem_124a7dc86c25491f_Encode {

   private Utilities utilities = new Utilities();

   public ObjectClassHandle objectHandle;
   public AttributeHandleSet attribHandles;
   public boolean hasInitialized = false;
   private int numberAttributes;
   private RTIambassador rtiAmb;
   private Map<Integer, Runnable> decodeActions = new HashMap<>();
   private Map.Entry currentMapEntry;

   private AttributeHandle planetsAttributeHandle;

   public String getFullyQualifiedObjectName() {

      return "HLAobjectRoot.SolarSystem";
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

         planetsAttributeHandle = rtiAmb.getAttributeHandle(objectHandle, "Planets");
         setupAttributeMappingRun(planetsAttributeHandle);

         attribHandles.add(planetsAttributeHandle);

         hasInitialized = true;
      } catch (Exception e) {
			e.printStackTrace();
      }
   }

   private void setupAttributeMappingRun(AttributeHandle attributeHandle) {

      try {

         if (attributeHandle.equals(rtiAmb.getAttributeHandle(objectHandle, "Planets")))
            decodeActions.put(attributeHandle.hashCode(),() -> planetsDecode());

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

   // Encode outgoing data obtained from source class variable definitions to destination AttributeHandleValueMap
   public void encode(AttributeHandleValueMap theAttributeValues) {

      // ID: 12d8296c87e6a7e6  Array
      if (planets != null) {

         DynamicBuffer dynamicBuffer = new DynamicBuffer();
         planets.encode(dynamicBuffer, planets.getAlignment());
         theAttributeValues.put(planetsAttributeHandle, dynamicBuffer.getWrittenBytes());
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

   // ID: 12d8296c87e6a7e6  Array
   // Decode incoming data byte array to destination class variable definitions
   private void planetsDecode() {

      DynamicBuffer dynamicBuffer = new DynamicBuffer();
      dynamicBuffer.clear();
      dynamicBuffer.put((byte []) currentMapEntry.getValue());
      dynamicBuffer.flip();
      planets.decode(dynamicBuffer, planets.getAlignment());
   }

}
