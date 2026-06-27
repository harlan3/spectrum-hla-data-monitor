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
public class PlanetTypeArray_Cont {

   public PlanetType_Cont[] value = new PlanetType_Cont[10];

   // Constructor
   public PlanetTypeArray_Cont() {

      for (int i=0; i < 10; i++)
         value[i] = new PlanetType_Cont();
   }
}
