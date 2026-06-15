/*
 *  Oric RDSM provides java object persistence and introspection
 *  through the use of a Recursive Descent Symbol Map.
 *
 *  Copyright (C) 2011 Harlan Murphy
 *  Orbis Software - orbisoftware@gmail.com
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License Version 3 or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License 
 *  along with this software.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */

package orbisoftware.oricrdsm;

public class OricSymbolDict {

   public static enum SymbolType {
      OBJECT, ARRAY, PRIMITIVE
   }

   public static final String EOL = ";";
   public static final String EQUALS = "=";
   public static final String CHILD_DELIMITER = ",";

   public static final String BASEOBJ = "sample";
   public static final String SYM_PREFIX = "@S";
   public static final String ROOT = "@ROOT@";
   public static final String MISSING = "@MISSING@";

   public static final String SYMBOLCOUNT_KEY = "SymbolCount";
   public static final String SYMTYPE_KEY = ".SymType";
   public static final String PARENT_KEY = ".Parent";
   public static final String FIELDNAME_KEY = ".FieldName";
   public static final String FIELDTYPE_KEY = ".FieldType";
   public static final String FIELDVALUE_KEY = ".FieldValue";
   public static final String NUMCHILDREN_KEY = ".NumChildren";
   public static final String CHILDREN_KEY = ".Children";

}
