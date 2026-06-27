package orbisoftware.hla13_encoding.Common.FixedArrays;

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
public class StarTypeArray_Encode {

   private StarType_Encode[] internalClassRepresentation = new StarType_Encode[571];

   public int sizeOfValue() {

      return 571;
   }

   public int cardinality = 571;

   // Constructor
   public StarTypeArray_Encode() {

      for (int i=0; i < cardinality; i++)
         internalClassRepresentation[i] = new StarType_Encode();
   }

   // Setter
   public void setStarType_Encode(int index, StarType_Encode starType) {

      internalClassRepresentation[index] = starType;
   }

   // Getter
   public StarType_Encode getStarType_Encode(int index) {

      return internalClassRepresentation[index];
   }

   // Get the element size
   private int getElementSize(int alignment) {

      // Determine the number of bytes for an element
      DynamicBuffer tmpBuffer = new DynamicBuffer();
      new StarType_Encode().encode(tmpBuffer, alignment);
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
