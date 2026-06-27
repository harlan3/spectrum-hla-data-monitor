package orbisoftware.hla_1516e_encoding.Common.PrefixedStringLength;

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

import java.nio.charset.StandardCharsets;
import orbisoftware.hla_shared.*;

// HLAASCIIstringImp has a size prefix containing the length of the string
@SuppressWarnings("unused")
public class HLAASCIIstringImp_Encode {

   private Utilities utilities = new Utilities();

   // Constructor
   public HLAASCIIstringImp_Encode() {

   }

   // Class String
   private String internalClassRepresentation = new String();

   // Setter
   public void setString(String newString) {

      internalClassRepresentation = newString;
   }

   // Getter
   public String getString() {

      return internalClassRepresentation;
   }

   // Encode outgoing data obtained from internal class representation into DynamicBuffer
   public void encode(DynamicBuffer buffer, int alignment) {

      buffer.put(utilities.getBytesFromInteger(internalClassRepresentation.length()));
      buffer.put(internalClassRepresentation.getBytes());
   }

   // Decode incoming data obtained from DynamicBuffer into internal class representation
   public void decode(DynamicBuffer buffer, int alignment) {

      byte[] sizeBytes = new byte[Integer.BYTES];


      buffer.rewind();
      buffer.get(sizeBytes);
      int numElements = utilities.getIntegerFromBytes(sizeBytes);

      internalClassRepresentation = "";

      byte[] stringBytes = new byte[numElements];
      buffer.get(stringBytes);

      internalClassRepresentation = new String(stringBytes, StandardCharsets.UTF_8);
   }

   // Get the structure alignment
   public int getAlignment() {

      int largestStructureMember = 1;

      return largestStructureMember;
   }
}
