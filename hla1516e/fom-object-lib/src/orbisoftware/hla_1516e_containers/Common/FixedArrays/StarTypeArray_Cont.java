package orbisoftware.hla_1516e_containers.Common.FixedArrays;

import orbisoftware.hla_1516e_containers.Common.Enums.*;
import orbisoftware.hla_1516e_containers.Common.FixedArrays.*;
import orbisoftware.hla_1516e_containers.Common.FixedRecords.*;
import orbisoftware.hla_1516e_containers.Common.LengthlessArrays.*;
import orbisoftware.hla_1516e_containers.Common.NullTerminatedArrays.*;
import orbisoftware.hla_1516e_containers.Common.PrefixedStringLength.*;
import orbisoftware.hla_1516e_containers.Common.VariableArrays.*;
import orbisoftware.hla_1516e_containers.Common.VariantRecords.*;
import orbisoftware.hla_1516e_containers.Common.Misc.*;

@SuppressWarnings("unused")
public class StarTypeArray_Cont {

   public StarType_Cont[] value = new StarType_Cont[571];

   // Constructor
   public StarTypeArray_Cont() {

      for (int i=0; i < 571; i++)
         value[i] = new StarType_Cont();
   }
}
