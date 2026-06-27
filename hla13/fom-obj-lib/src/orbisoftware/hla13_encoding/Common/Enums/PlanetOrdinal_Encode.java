package orbisoftware.hla13_encoding.Common.Enums;

import orbisoftware.hla_shared.*;

public class PlanetOrdinal_Encode {

   // Constructor
   public PlanetOrdinal_Encode() {

   }

   private Utilities utilities = new Utilities();

   public int value = 0;

   // Enumerator Values
   public static final int Mercury = 1;
   public static final int Venus = 2;
   public static final int Earth = 3;
   public static final int Mars = 4;
   public static final int Jupiter = 5;
   public static final int Saturn = 6;
   public static final int Uranus = 7;
   public static final int Neptune = 8;
   public static final int Pluto = 9;

   // Get Enumerator Name from the integer Enumerator value
   public static String getName(int enumValue) {

      String returnValue = "";

      switch(enumValue) {

      case 1:
         returnValue = "Mercury";
         break;

      case 2:
         returnValue = "Venus";
         break;

      case 3:
         returnValue = "Earth";
         break;

      case 4:
         returnValue = "Mars";
         break;

      case 5:
         returnValue = "Jupiter";
         break;

      case 6:
         returnValue = "Saturn";
         break;

      case 7:
         returnValue = "Uranus";
         break;

      case 8:
         returnValue = "Neptune";
         break;

      case 9:
         returnValue = "Pluto";
         break;

      }

      return returnValue;
   }

   // Encode outgoing data obtained from internal type into DynamicBuffer
   public void encode(DynamicBuffer buffer, int alignment) {

      buffer.put(utilities.getBytesFromInteger(value));
   }

   // Decode incoming data obtained from DynamicBuffer into internal type
   public void decode(DynamicBuffer buffer, int alignment) {

      byte[] elementBytes = new byte[4];
      buffer.get(elementBytes);
      value = utilities.getIntegerFromBytes(elementBytes);
   }

   // Get the structure alignment
   public int getAlignment() {

      int largestStructureMember = 4;

      return largestStructureMember;
   }
}
