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
public class StarType_Encode {

   private Utilities utilities = new Utilities();

   // Class Variable
   private int starID;

   // Setter
   public void setStarID(int starID) {
      this.starID = starID;
   }

   // Getter
   public int getStarID() {
      return starID;
   }

   // Class Variable
   private int baseSize;

   // Setter
   public void setBaseSize(int baseSize) {
      this.baseSize = baseSize;
   }

   // Getter
   public int getBaseSize() {
      return baseSize;
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
   private double projX;

   // Setter
   public void setProjX(double projX) {
      this.projX = projX;
   }

   // Getter
   public double getProjX() {
      return projX;
   }

   // Class Variable
   private double projY;

   // Setter
   public void setProjY(double projY) {
      this.projY = projY;
   }

   // Getter
   public double getProjY() {
      return projY;
   }

   // Class Variable
   private double projZ;

   // Setter
   public void setProjZ(double projZ) {
      this.projZ = projZ;
   }

   // Getter
   public double getProjZ() {
      return projZ;
   }

   // Class Variable
   private double size;

   // Setter
   public void setSize(double size) {
      this.size = size;
   }

   // Getter
   public double getSize() {
      return size;
   }

   // Class Variable
   private double speed;

   // Setter
   public void setSpeed(double speed) {
      this.speed = speed;
   }

   // Getter
   public double getSpeed() {
      return speed;
   }

   // Class Variable
   private double blinkSpeed;

   // Setter
   public void setBlinkSpeed(double blinkSpeed) {
      this.blinkSpeed = blinkSpeed;
   }

   // Getter
   public double getBlinkSpeed() {
      return blinkSpeed;
   }

   // Class Variable
   private double blinkPhase;

   // Setter
   public void setBlinkPhase(double blinkPhase) {
      this.blinkPhase = blinkPhase;
   }

   // Getter
   public double getBlinkPhase() {
      return blinkPhase;
   }

   // Class Variable
   private double alpha;

   // Setter
   public void setAlpha(double alpha) {
      this.alpha = alpha;
   }

   // Getter
   public double getAlpha() {
      return alpha;
   }

   // Encode outgoing data obtained from member variables into DynamicBuffer
   public void encode(DynamicBuffer buffer, int alignment) {

      int bufferOffset = buffer.position();

      // Write the Integer field
      buffer.put(utilities.getBytesFromInteger(starID));
      bufferOffset = buffer.position();

      // Align and write the Integer field
      utilities.insertPadding(buffer, bufferOffset, Integer.BYTES);
      buffer.put(utilities.getBytesFromInteger(baseSize));
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
      buffer.put(utilities.getBytesFromDouble(projX));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(projY));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(projZ));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(size));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(speed));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(blinkSpeed));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(blinkPhase));
      bufferOffset = buffer.position();

      // Align and write the Double field
      utilities.insertPadding(buffer, bufferOffset, Double.BYTES);
      buffer.put(utilities.getBytesFromDouble(alpha));
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
      starID = utilities.getIntegerFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Integer field
      bufferOffset = utilities.align(bufferOffset, Integer.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Integer.BYTES];
      buffer.get(bytes);
      baseSize = utilities.getIntegerFromBytes(bytes);
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
      projX = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      projY = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      projZ = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      size = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      speed = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      blinkSpeed = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      blinkPhase = utilities.getDoubleFromBytes(bytes);
      bufferOffset = buffer.position();

      // Align and read the Double field
      bufferOffset = utilities.align(bufferOffset, Double.BYTES);
      buffer.position(bufferOffset);
      bytes = new byte[Double.BYTES];
      buffer.get(bytes);
      alpha = utilities.getDoubleFromBytes(bytes);
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
