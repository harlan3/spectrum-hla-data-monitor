package orbisoftware.hla_1516e_encoding.Common.FixedRecords;

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
public class PlanetType_Encode {

   private Utilities utilities = new Utilities();

   // Class Variable
   private int planetID;

   // Setter
   public void setPlanetID(int planetID) {
      this.planetID = planetID;
   }

   // Getter
   public int getPlanetID() {
      return planetID;
   }

   // Class Variable
   private int planetOrdinalValue;

   // Setter
   public void setPlanetOrdinalValue(int planetOrdinalValue) {
      this.planetOrdinalValue = planetOrdinalValue;
   }

   // Getter
   public int getPlanetOrdinalValue() {
      return planetOrdinalValue;
   }

   // Class Variable
   private double xpos;

   // Setter
   public void setXpos(double xpos) {
      this.xpos = xpos;
   }

   // Getter
   public double getXpos() {
      return xpos;
   }

   // Class Variable
   private double ypos;

   // Setter
   public void setYpos(double ypos) {
      this.ypos = ypos;
   }

   // Getter
   public double getYpos() {
      return ypos;
   }

   // Class Variable
   private double theta;

   // Setter
   public void setTheta(double theta) {
      this.theta = theta;
   }

   // Getter
   public double getTheta() {
      return theta;
   }

   // Class Variable
   private double planetSize;

   // Setter
   public void setPlanetSize(double planetSize) {
      this.planetSize = planetSize;
   }

   // Getter
   public double getPlanetSize() {
      return planetSize;
   }

   // Class Variable
   private double orbitalRadius;

   // Setter
   public void setOrbitalRadius(double orbitalRadius) {
      this.orbitalRadius = orbitalRadius;
   }

   // Getter
   public double getOrbitalRadius() {
      return orbitalRadius;
   }

   // Class Variable
   private double orbitalVelocity;

   // Setter
   public void setOrbitalVelocity(double orbitalVelocity) {
      this.orbitalVelocity = orbitalVelocity;
   }

   // Getter
   public double getOrbitalVelocity() {
      return orbitalVelocity;
   }

   // Encode outgoing data obtained from member variables into DynamicBuffer
   public void encode(DynamicBuffer buffer, int alignment) {

      int bufferOffset = buffer.position();

      // Write the Integer field
      buffer.put(utilities.getBytesFromInteger(planetID));
      bufferOffset = buffer.position();

      // Align and write the Integer field
      utilities.insertPadding(buffer, bufferOffset, Integer.BYTES);
      buffer.put(utilities.getBytesFromInteger(planetOrdinalValue));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(xpos));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(ypos));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(theta));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(planetSize));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(orbitalRadius));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(orbitalVelocity));
      bufferOffset = buffer.position();

      // Insert padding for alignment of the largest structure member
      utilities.insertPadding(buffer, bufferOffset, alignment);
   }

   // Decode incoming data obtained from DynamicBuffer into member variables
   public void decode(DynamicBuffer buffer, int alignment) {

      int bufferOffset = buffer.position();
      byte[] bytes;

      // Read the Integer field
      bytes = new byte[Integer.BYTES];
      buffer.get(bytes);
      planetID = utilities.getIntegerFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Integer field
      bufferOffset = utilities.align(bufferOffset, Integer.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Integer.BYTES];
      buffer.get(bytes);
      planetOrdinalValue = utilities.getIntegerFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      xpos = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      ypos = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      theta = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      planetSize = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      orbitalRadius = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      orbitalVelocity = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Advance buffer to alignment of the largest structure member
      bufferOffset = utilities.align(bufferOffset, alignment);
      buffer.position(bufferOffset);
   }

   // Get the structure alignment
   public int getAlignment() {

      int largestStructureMember = 8;

      return largestStructureMember;
   }
}
