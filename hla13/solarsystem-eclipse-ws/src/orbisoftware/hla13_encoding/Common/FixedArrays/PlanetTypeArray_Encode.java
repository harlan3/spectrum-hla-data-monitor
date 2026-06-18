package orbisoftware.hla13_encoding.Common.FixedArrays;

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
public class PlanetTypeArray_Encode {

   private PlanetType_Encode[] internalClassRepresentation = new PlanetType_Encode[10];

   public int sizeOfValue() {

      return 10;
   }

   public int cardinality = 10;

   // Constructor
   public PlanetTypeArray_Encode() {

      for (int i=0; i < cardinality; i++)
         internalClassRepresentation[i] = new PlanetType_Encode();
   }

   // Setter
   public void setPlanetType_Encode(int index, PlanetType_Encode planetType) {

      internalClassRepresentation[index] = planetType;
   }

   // Getter
   public PlanetType_Encode getPlanetType_Encode(int index) {

      return internalClassRepresentation[index];
   }

   // Get the element size
   private int getElementSize(int alignment) {

      // Determine the number of bytes for an element
      DynamicBuffer tmpBuffer = new DynamicBuffer();
      new PlanetType_Encode().encode(tmpBuffer, alignment);
      int elementSize = tmpBuffer.position();

      return elementSize;
   }

   // Encode outgoing data obtained from internal class representation into DynamicBuffer
   public void encode(DynamicBuffer buffer, int alignment) {

      int elementSize = getElementSize(alignment);
      DynamicBuffer tmpBuffer = new DynamicBuffer();
      byte[] elementBytes = new byte[elementSize];

      for (int i=0; i < cardinality; i++) {

         tmpBuffer.rewind();
         internalClassRepresentation[i].encode(tmpBuffer, alignment);
         tmpBuffer.rewind();
         tmpBuffer.get(elementBytes);
         buffer.put(elementBytes);
      }
   }

   // Decode incoming data obtained from DynamicBuffer into internal class representation
   public void decode(DynamicBuffer buffer, int alignment) {

      int elementSize = getElementSize(alignment);
      DynamicBuffer tmpBuffer = new DynamicBuffer();
      byte[] elementBytes = new byte[elementSize];

      for (int i=0; i < cardinality; i++) {

         buffer.position(i * elementSize);
         buffer.get(elementBytes);
         tmpBuffer.rewind();
         tmpBuffer.put(elementBytes);
         tmpBuffer.rewind();
         internalClassRepresentation[i].decode(tmpBuffer, alignment);
      }
   }

   // Get the structure alignment
   public int getAlignment() {

      int largestStructureMember = 1;

      return largestStructureMember;
   }
}
