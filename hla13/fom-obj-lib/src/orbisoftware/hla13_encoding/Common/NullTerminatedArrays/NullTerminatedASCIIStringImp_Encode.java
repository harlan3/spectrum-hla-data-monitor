package orbisoftware.hla13_encoding.Common.NullTerminatedArrays;

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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SuppressWarnings("unused")
public class NullTerminatedASCIIStringImp_Encode {

   private Utilities utilities = new Utilities();

   // Constructor
   public NullTerminatedASCIIStringImp_Encode() {

   }

   // Class Variable
   private byte[] value = new byte[1];

   public int sizeOfValue() {

      return value.length;
   }

   // Setter
   public void setValue(byte[] value) {

      byte[] byteArray = value;
      int pos = 0;
      int newSize = 0;

      while (newSize < byteArray.length) {

         if (byteArray[pos] == '\0')
            break;

         newSize++;
         pos++;
      }

      byte[] newByteArray = Arrays.copyOf(byteArray, newSize + 1);
      newByteArray[newSize] = 0x00;

      this.value = newByteArray;
   }

   // Getter
   public byte[] getValue() {
      return value;
   }

   // String Getter
   public String getString() {

      int length = 0;

      while (length < value.length && value[length] != 0) {
         length++;
      }

      return new String(value, 0, length, StandardCharsets.US_ASCII);
   }

   // Encode outgoing data obtained from internal class representation into DynamicBuffer
   public void encode(DynamicBuffer buffer, int alignment) {

      buffer.put(value);
   }

   // Decode incoming data obtained from DynamicBuffer into internal class representation
   public void decode(DynamicBuffer buffer, int alignment) {

      int newSize = 0;

      while (newSize < buffer.limit()) {

         if (buffer.get() == '\0')
            break;

         newSize++;
      }

      byte[] newByteArray = new byte[(newSize + 1)];

      buffer.position(buffer.position() - (newSize + 1));
      buffer.get(newByteArray);
      setValue(newByteArray);
   }

   // Get the structure alignment
   public int getAlignment() {

      int largestStructureMember = 1;

      return largestStructureMember;
   }
}
