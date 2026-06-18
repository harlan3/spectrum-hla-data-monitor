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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
//import java.util.TreeMap; // Useful for debugging. Swap for existing HashMap.
import java.util.Iterator;
import java.util.Map;

public class OricSymbolMap {

   /** Return the number of symbols reported in symbol map */
   public int getSymbolCount() {
      return Integer.parseInt(symbolMap.get(OricSymbolDict.SYMBOLCOUNT_KEY));
   }

   /** Return the base symbol (root of RDSM) */
   public String getBaseSymbol() {
      return OricSymbolDict.SYM_PREFIX + 0;
   }

   /** Return the SymbolType */
   public OricSymbolDict.SymbolType getSymbolType(String symbol) {
      return OricSymbolDict.SymbolType.valueOf(symbolMap.get(symbol
            + OricSymbolDict.SYMTYPE_KEY));
   }

   /** Return the symbol field name */
   public String getFieldName(String symbol) {
      return symbolMap.get(symbol + OricSymbolDict.FIELDNAME_KEY);
   }

   /** Return the symbol field type */
   public String getFieldType(String symbol) {
      return symbolMap.get(symbol + OricSymbolDict.FIELDTYPE_KEY);
   }

   /** Return the symbol field value */
   public String getFieldValue(String symbol) {
      return symbolMap.get(symbol + OricSymbolDict.FIELDVALUE_KEY);
   }

   /** Set the symbol field value */
   public void setFieldValue(String symbol, String fieldValue) {
      symbolMap.put(symbol + OricSymbolDict.FIELDVALUE_KEY, fieldValue);
   }

   /** Return the symbol field number of children */
   public String getFieldNumChildren(String symbol) {
      return symbolMap.get(symbol + OricSymbolDict.NUMCHILDREN_KEY);
   }

   /** Set the symbol field number of children */
   public void setFieldNumChildren(String symbol, String fieldNumChildren) {
      symbolMap.put(symbol + OricSymbolDict.NUMCHILDREN_KEY, fieldNumChildren);
   }

   /** Return true if symbol is a leaf */
   public boolean isLeaf(String symbol) {

      boolean isLeaf = false;
      int count;

      String numChildren = symbolMap.get(symbol
            + OricSymbolDict.NUMCHILDREN_KEY);

      if (numChildren == null)
         isLeaf = true;
      else {
         count = Integer.parseInt(numChildren);

         if (count == 0)
            isLeaf = true;
      }

      return isLeaf;
   }

   /** Return the symbol's parent */
   public String getParent(String symbol) {
      return symbolMap.get(symbol + OricSymbolDict.PARENT_KEY);
   }

   /** Return the child count for symbol */
   public int getChildCount(String symbol) {

      String numChildren = symbolMap.get(symbol
            + OricSymbolDict.NUMCHILDREN_KEY);

      if (numChildren == null)
         return 0;
      else
         return Integer.parseInt(symbolMap.get(symbol
               + OricSymbolDict.NUMCHILDREN_KEY));
   }

   /** Return the symbol's children */
   public String[] getChildren(String symbol) {
      return symbolMap.get(symbol + OricSymbolDict.CHILDREN_KEY).split(
            OricSymbolDict.CHILD_DELIMITER);
   }

   /** Return the index of a child symbol */
   public int getIndexOfChild(String parentSymbol, String childSymbol) {

      String[] children = symbolMap.get(
            parentSymbol + OricSymbolDict.CHILDREN_KEY).split(
            OricSymbolDict.CHILD_DELIMITER);
      int childIndex = 0;

      for (int i = 0; i < children.length; i++) {
         if (childSymbol.equals(children[i]))
            childIndex = i;
      }

      return childIndex;
   }

   /** Return a child symbol by index */
   public String getChildByIndex(String parentSymbol, int childIndex) {
      String[] children = symbolMap.get(
            parentSymbol + OricSymbolDict.CHILDREN_KEY).split(
            OricSymbolDict.CHILD_DELIMITER);

      if (childIndex < children.length)
         return children[childIndex];
      else
         return null;
   }

   /** Generate a persistent textual representation of the symbol map */
   public void objToSymMapBuffer(Object object, StringBuffer buffer) {

      try {
         objToSymMap(object);

         Iterator<Map.Entry<String, String>> it = symbolMap.entrySet()
               .iterator();

         buffer.setLength(0);

         while (it.hasNext()) {
            Map.Entry<String, String> pairs = it.next();
            buffer.append(pairs.getKey() + OricSymbolDict.EQUALS
                  + symbolMap.get(pairs.getKey()) + OricSymbolDict.EOL);
         }
      } catch (Exception e) {
         throw new OricException(e);
      }
   }

   /** Generate the symbol map from an object */
   public void objToSymMap(Object object) {

      try {
         Class<?> oClass = object.getClass();
         baseClassName = oClass.toString().replace("class ", "");

         symbolMap.clear();
         symCounter = 0;

         String symbolName = OricSymbolDict.SYM_PREFIX
               + Integer.toString(takeNextSym());

         // baseobj is primitive
         if (oClass.isPrimitive() || isPrimitiveType(oClass)) {
            insertPrimitive(symbolName, OricSymbolDict.BASEOBJ,
                  OricSymbolDict.ROOT, getTypeForClass(oClass),
                  object.toString());
         } // baseobj is array
         else if (oClass.isArray()) {
            insertArray(symbolName, OricSymbolDict.BASEOBJ,
                  OricSymbolDict.ROOT,
                  getTypeForClass(oClass.getComponentType()));
            genSymbols(object, symbolName);
         } // baseobj is object
         else {
            insertObject(symbolName, OricSymbolDict.BASEOBJ,
                  OricSymbolDict.ROOT, getTypeForClass(oClass));
            genSymbols(object, symbolName);
         }

         symbolMap.put(OricSymbolDict.SYMBOLCOUNT_KEY,
               Integer.toString(getSymCount()));
         findMissingChildren();
      } catch (Exception e) {
         throw new OricException(e);
      }
   }

   /** Generate the symbol map from a buffer */
   public void bufferToSymMap(StringBuffer buffer) {

      try {
         String[] lines = buffer.toString().split(OricSymbolDict.EOL);

         symbolMap.clear();
         symCounter = 0;

         for (String line : lines) {
            String[] symbols = line.split(OricSymbolDict.EQUALS);
            symbolMap.put(symbols[0], symbols[1]);
         }
      } catch (Exception e) {
         throw new OricException(e);
      }
   }

   /** Create a dump of the symbol map */
   public void dumpSymbolMap() {

      try {
         Iterator<Map.Entry<String, String>> it = symbolMap.entrySet()
               .iterator();

         while (it.hasNext()) {
            Map.Entry<String, String> pairs = it.next();
            System.out.print(pairs.getKey() + OricSymbolDict.EQUALS
                  + symbolMap.get(pairs.getKey()) + OricSymbolDict.EOL);
         }
      } catch (Exception e) {
         throw new OricException(e);
      }
   }

   /** Reconstruct an object from the symbol map */
   public Object symMapToObj() {

      Object object = null;
      try {
         // instantiate object
         String symbolName = OricSymbolDict.SYM_PREFIX + 0;
         OricSymbolDict.SymbolType symType = OricSymbolDict.SymbolType
               .valueOf(symbolMap.get(symbolName + OricSymbolDict.SYMTYPE_KEY));
         String fieldTypeKey = symbolName + OricSymbolDict.FIELDTYPE_KEY;

         // is primitive
         if (symType == OricSymbolDict.SymbolType.PRIMITIVE) {

            String fieldValueKey = symbolName + OricSymbolDict.FIELDVALUE_KEY;
            String fieldType = symbolMap.get(fieldTypeKey);
            String fieldValue = symbolMap.get(fieldValueKey);

            if (fieldType.equals("boolean"))
               object = Boolean.parseBoolean(fieldValue);
            else if (fieldType.equals("int"))
               object = Integer.parseInt(fieldValue);
            else if (fieldType.equals("char"))
               object = fieldValue.charAt(0);
            else if (fieldType.equals("byte"))
               object = Byte.parseByte(fieldValue);
            else if (fieldType.equals("short"))
               object = Short.parseShort(fieldValue);
            else if (fieldType.equals("double"))
               object = Double.parseDouble(fieldValue);
            else if (fieldType.equals("long"))
               object = Long.parseLong(fieldValue);
            else if (fieldType.equals("float"))
               object = Float.parseFloat(fieldValue);
            else if (fieldType.equals("String"))
               object = fieldValue;
         } // is array
         else if (symType == OricSymbolDict.SymbolType.ARRAY) {
            int arrayLen = Integer.parseInt(symbolMap.get(symbolName
                  + OricSymbolDict.NUMCHILDREN_KEY));
            object = Array.newInstance(
                  getClassForType(symbolMap.get(fieldTypeKey)), arrayLen);
            genObject(symbolName, object);
         } // is object
         else {
            Class<?> _class = getClassForType(symbolMap.get(fieldTypeKey));
            object = _class.newInstance();
            genObject(symbolName, object);
         }
      } catch (Exception e) {
         throw new OricException(e);
      }
      return object;
   }

   /**
    * Recursively descend object, instantiating all non non-null primitive
    * objects and arrays.
    */
   public void instantiateObj(Object object) {
      try {

         Class<?> oClass = object.getClass();
         baseClassName = oClass.toString().replace("class ", "");

         Field[] fields = oClass.getDeclaredFields();
         for (int i = 0; i < fields.length; i++) {

            fields[i].setAccessible(true);

            Class<?> childClass = fields[i].getType();
            Object childObj = fields[i].get(object);

            // is primitive
            if (childClass.isPrimitive() || isPrimitiveType(childClass)) {

               if (childClass == char.class) {
                  char arrayVal = fields[i].getChar(object);
                  if (arrayVal != 0) {
                     continue;
                  }
               } else if (childClass == String.class) {
                  String arrayVal = (String)fields[i].get(object);
                  if (arrayVal != null && arrayVal != "") {
                     continue;
                  }
               }
               
               try {
                  if (childClass == String.class) {
                     childObj = new String("#");
                     fields[i].set(object, childObj);
                  } else if (childClass == char.class) {
                     childObj = new Character('#');
                     fields[i].set(object, childObj);
                  }
               } catch (IllegalAccessException e) {
                  // Some field types (like static finals) cannot be set
                  // so silently ignore.
               }
            } // is array
            else if (childClass.isArray()) {

               String typeName = childClass.getComponentType()
                     .getCanonicalName();
               boolean isStatic = false;

               // Check for static field
               if (java.lang.reflect.Modifier
                     .isStatic(fields[i].getModifiers()))
                  isStatic = true;

               // Check for infinite recursion
               if (typeName.equals(baseClassName) && isStatic) {
                  // do nothing
               } else {
                  baseClassName = typeName;
                  Class<?> classType = getClassForType(typeName);
                  int arrayLength = 0;
                  boolean childIsPrimitive = classType.isPrimitive()
                        || classType == String.class;

                  // If the array has a dimension then use it.
                  try {
                     arrayLength = Array.getLength(fields[i].get(object));
                  } catch (Exception e) {
                  }

                  if (arrayLength < 1)
                     arrayLength = 1;

                  for (int index = 0; index < arrayLength; index++) {

                     // Check to see if valid arrayValue exist to avoid
                     // overwriting.
                     try {

                        if ((!childIsPrimitive) && (classType != String.class)) {
                           Object arrayVal = Array.get(childObj, index);
                           if (arrayVal != null) {
                              continue;
                           }
                        } else if (classType == char.class) {
                           char arrayVal = Array.getChar(childObj, index);
                           if (arrayVal != 0) {
                              continue;
                           }
                        } else if (classType == String.class) {
                           String arrayVal = (String) Array
                                 .get(childObj, index);
                           if (arrayVal != "") {
                              continue;
                           }
                        }
                     } catch (Exception e) {
                     }

                     if (childIsPrimitive) {

                        if (classType == boolean.class) {
                           if (index == 0)
                              childObj = Array.newInstance(
                                    java.lang.Boolean.TYPE, arrayLength);
                           Array.set(childObj, index, new java.lang.Boolean(
                                 false));
                        } else if (classType == int.class) {
                           if (index == 0)
                              childObj = Array.newInstance(
                                    java.lang.Integer.TYPE, arrayLength);
                           Array.set(childObj, index, new java.lang.Integer(0));
                        } else if (classType == char.class) {
                           if (index == 0)
                              childObj = Array.newInstance(
                                    java.lang.Character.TYPE, arrayLength);
                           Array.set(childObj, index, new java.lang.Character(
                                 '#'));
                        } else if (classType == byte.class) {
                           if (index == 0)
                              childObj = Array.newInstance(java.lang.Byte.TYPE,
                                    arrayLength);
                           Array.set(childObj, index, new java.lang.Byte(
                                 (byte) 0));
                        } else if (classType == short.class) {
                           if (index == 0)
                              childObj = Array.newInstance(
                                    java.lang.Short.TYPE, arrayLength);
                           Array.set(childObj, index, new java.lang.Short(
                                 (short) 0));
                        } else if (classType == double.class) {
                           if (index == 0)
                              childObj = Array.newInstance(
                                    java.lang.Double.TYPE, arrayLength);
                           Array.set(childObj, index, new java.lang.Double(0.0));
                        } else if (classType == long.class) {
                           if (index == 0)
                              childObj = Array.newInstance(java.lang.Long.TYPE,
                                    arrayLength);
                           Array.set(childObj, index, new java.lang.Long(0));
                        } else if (classType == float.class) {
                           if (index == 0)
                              childObj = Array.newInstance(
                                    java.lang.Float.TYPE, arrayLength);
                           Array.set(childObj, index, new java.lang.Float(0.0));
                        } else if (classType == String.class) {
                           if (index == 0)
                              childObj = Array.newInstance(String.class,
                                    arrayLength);
                           Array.set(childObj, index, new String("#"));
                        }
                     } else {

                        if (index == 0)
                           childObj = Array.newInstance(classType, arrayLength);

                        try {
                           Array.set(childObj, index, classType.newInstance());
                        } catch (Exception e1) {

                           try {
                              // This is a hack to deal with some
                              // autogenerated classes that do not contain
                              // a no-args constructor.
                              Array.set(childObj, index,
                                    classType.getMethod("from_int", int.class)
                                          .invoke(classType, 0));
                           } catch (Exception e2) {
                              throw new OricException(
                                    "Could not instantiate object.");
                           }
                        }
                     }

                  }

                  fields[i].set(object, childObj);

                  if (!childIsPrimitive) {
                     for (int index = 0; index < arrayLength; index++)
                        instantiateObj(Array.get(childObj, index));
                  }
               }
            } // is object
            else {

               String typeName = getTypeForClass(fields[i].getType());
               boolean isStatic = false;

               // Check for static field
               if (java.lang.reflect.Modifier
                     .isStatic(fields[i].getModifiers()))
                  isStatic = true;

               // Check for infinite recursion
               if (typeName.contains(baseClassName) && isStatic) {
                  // do nothing
               } else {
                  baseClassName = typeName;

                  if (childObj == null) {

                     try {
                        childObj = childClass.newInstance();
                     } catch (Exception e1) {

                        try {
                           // This is a hack to deal with some
                           // autogenerated classes that do not contain
                           // a no-args constructor.
                           childObj = childClass.getMethod("from_int",
                                 int.class).invoke(childClass, 0);
                        } catch (Exception e2) {
                           throw new OricException(
                                 "Could not instantiate object.");
                        }
                     }
                  }

                  instantiateObj(childObj);
               }
            }
         }
         oClass = oClass.getSuperclass();
      } catch (Exception e) {
         throw new OricException(e);
      }
   }

   /**
    * Returns primitive class type (or the non-reducible object class) for
    * textual string type
    */
   private Class<?> getClassForType(String type) {

      if (type.equals("boolean"))
         return boolean.class;
      else if (type.equals("int"))
         return int.class;
      else if (type.equals("char"))
         return char.class;
      else if (type.equals("byte"))
         return byte.class;
      else if (type.equals("short"))
         return short.class;
      else if (type.equals("double"))
         return double.class;
      else if (type.equals("long"))
         return long.class;
      else if (type.equals("float"))
         return float.class;
      else if (type.equals("String"))
         return String.class;
      else
         try {
            return Class.forName(type);
         } catch (ClassNotFoundException e) {
            throw new OricException(e);
         }
   }

   /** Return textual string type for class */
   private String getTypeForClass(Class<?> _class) {

      if (_class.equals(Boolean.class) || _class.equals(boolean.class))
         return "boolean";
      else if (_class.equals(Integer.class) || _class.equals(int.class))
         return "int";
      else if (_class.equals(Character.class) || _class.equals(char.class))
         return "char";
      else if (_class.equals(Byte.class) || _class.equals(byte.class))
         return "byte";
      else if (_class.equals(Short.class) || _class.equals(short.class))
         return "short";
      else if (_class.equals(Double.class) || _class.equals(double.class))
         return "double";
      else if (_class.equals(Long.class) || _class.equals(long.class))
         return "long";
      else if (_class.equals(Float.class) || _class.equals(float.class))
         return "float";
      else if (_class.equals(String.class))
         return "String";
      else
         return _class.toString().replace("class ", "");
   }

   /** Returns true if the class is considered Primitive */
   private boolean isPrimitiveType(Class<?> _class) {
      return _class.equals(Boolean.class) || _class.equals(Integer.class)
            || _class.equals(Character.class) || _class.equals(Byte.class)
            || _class.equals(Short.class) || _class.equals(Double.class)
            || _class.equals(Long.class) || _class.equals(Float.class)
            || _class.equals(String.class);
   }

   /** Insert a PRIMITIVE symbol into the symbol map */
   private void insertPrimitive(String symbol, String name, String parent,
         String fieldType, String fieldValue) {
      symbolMap.put(symbol + OricSymbolDict.SYMTYPE_KEY,
            OricSymbolDict.SymbolType.PRIMITIVE.toString());
      symbolMap.put(symbol + OricSymbolDict.PARENT_KEY, parent);
      symbolMap.put(symbol + OricSymbolDict.FIELDNAME_KEY, name);
      symbolMap.put(symbol + OricSymbolDict.FIELDTYPE_KEY, fieldType);
      symbolMap.put(symbol + OricSymbolDict.FIELDVALUE_KEY, fieldValue);
   }

   /** Insert an ARRAY symbol into the symbol map */
   private void insertArray(String symbol, String name, String parent,
         String fieldType) {
      symbolMap.put(symbol + OricSymbolDict.SYMTYPE_KEY,
            OricSymbolDict.SymbolType.ARRAY.toString());
      symbolMap.put(symbol + OricSymbolDict.PARENT_KEY, parent);
      symbolMap.put(symbol + OricSymbolDict.FIELDNAME_KEY, name);
      symbolMap.put(symbol + OricSymbolDict.FIELDTYPE_KEY, fieldType);
      symbolMap.put(symbol + OricSymbolDict.NUMCHILDREN_KEY, "0");
      symbolMap.put(symbol + OricSymbolDict.CHILDREN_KEY,
            OricSymbolDict.MISSING);
   }

   /** Insert an OBJECT symbol into the symbol map */
   private void insertObject(String symbol, String name, String parent,
         String fieldType) {
      symbolMap.put(symbol + OricSymbolDict.SYMTYPE_KEY,
            OricSymbolDict.SymbolType.OBJECT.toString());
      symbolMap.put(symbol + OricSymbolDict.PARENT_KEY, parent);
      symbolMap.put(symbol + OricSymbolDict.FIELDNAME_KEY, name);
      symbolMap.put(symbol + OricSymbolDict.FIELDTYPE_KEY, fieldType);
      symbolMap.put(symbol + OricSymbolDict.NUMCHILDREN_KEY, "0");
      symbolMap.put(symbol + OricSymbolDict.CHILDREN_KEY,
            OricSymbolDict.MISSING);
   }

   /** Recursively descend object, generating symbol map */
   private void genSymbols(Object object, String parentSym) {

      try {
         Class<?> objClass = object.getClass();
         if (objClass.isArray()) {

            for (int i = 0; i < Array.getLength(object); i++) {

               String symbolName = OricSymbolDict.SYM_PREFIX
                     + Integer.toString(takeNextSym());

               Object childObj = Array.get(object, i);

               if (childObj != null) {
                  Class<?> childClass = childObj.getClass();
                  // is array of primitives
                  if (childClass.isPrimitive() || isPrimitiveType(childClass)) {
                     insertPrimitive(symbolName, "[" + Integer.toString(i)
                           + "]", parentSym, getTypeForClass(childClass),
                           childObj.toString());
                  } // is array of arrays which is not currently supported
                  else if (childClass.isArray()) {
                     returnLastSym();
                  } // is array of objects
                  else {
                     String typeName = getTypeForClass(childClass);

                     insertObject(symbolName, "[" + Integer.toString(i) + "]",
                           parentSym, typeName);
                     genSymbols(childObj, symbolName);
                  }
               } else {
                  returnLastSym();
               }
            }
         } else {
            while (objClass != null) {
               Field[] fields = objClass.getDeclaredFields();
               for (int i = 0; i < fields.length; i++) {

                  String symbol = OricSymbolDict.SYM_PREFIX
                        + Integer.toString(takeNextSym());
                  fields[i].setAccessible(true);

                  Object childObj = fields[i].get(object);

                  if (childObj != null) {
                     Class<?> childClass = childObj.getClass();
                     // is primitive
                     if (childClass.isPrimitive()
                           || isPrimitiveType(childClass)) {
                        insertPrimitive(symbol, fields[i].getName(), parentSym,
                              getTypeForClass(fields[i].getType()),
                              childObj.toString());
                     } // is array
                     else if (childClass.isArray()) {

                        String typeName = childClass.getComponentType()
                              .getCanonicalName();
                        boolean isStatic = false;

                        // Check for static field
                        if (java.lang.reflect.Modifier.isStatic(fields[i]
                              .getModifiers()))
                           isStatic = true;

                        // Check for infinite recursion
                        if (typeName.equals(baseClassName) && isStatic) {
                           returnLastSym();
                        } else {
                           baseClassName = typeName;
                           insertArray(symbol, fields[i].getName(), parentSym,
                                 getTypeForClass(childClass.getComponentType()));
                           genSymbols(childObj, symbol);
                        }
                     } // is object
                     else {
                        String typeName = getTypeForClass(fields[i].getType());
                        boolean isStatic = false;

                        // Check for static field
                        if (java.lang.reflect.Modifier.isStatic(fields[i]
                              .getModifiers()))
                           isStatic = true;

                        // Check for infinite recursion
                        if (typeName.contains(baseClassName) && isStatic) {
                           returnLastSym();
                        } else {
                           baseClassName = typeName;
                           insertObject(symbol, fields[i].getName(), parentSym,
                                 typeName);
                           genSymbols(childObj, symbol);
                        }
                     }
                  } else {
                     returnLastSym();
                  }
               }
               objClass = objClass.getSuperclass();
            }
         }
      } catch (Exception e) {
         throw new OricException(e);
      }
   }

   /** Set parent's symbol children from child's known parent */
   private void findMissingChildren() {

      try {
         for (int i = 0; i < Integer.valueOf(symbolMap
               .get(OricSymbolDict.SYMBOLCOUNT_KEY)); i++) {

            String symbolName = OricSymbolDict.SYM_PREFIX + i;
            String parentKey = symbolName + OricSymbolDict.PARENT_KEY;

            if (symbolMap.containsKey(parentKey)) {
               String parentVal = symbolMap.get(parentKey);
               String childrenKey = parentVal + OricSymbolDict.CHILDREN_KEY;
               String numChildrenKey = parentVal
                     + OricSymbolDict.NUMCHILDREN_KEY;

               if (symbolMap.containsKey(childrenKey)) {
                  if (symbolMap.get(childrenKey).contains(
                        OricSymbolDict.MISSING)) {
                     symbolMap.put(childrenKey, symbolName);
                  } else {
                     symbolMap.put(childrenKey, symbolMap.get(childrenKey)
                           + OricSymbolDict.CHILD_DELIMITER + symbolName);
                  }
                  symbolMap.put(numChildrenKey, Integer.toString(Integer
                        .valueOf(symbolMap.get(numChildrenKey)) + 1));
               }
            }
         }
      } catch (Exception e) {
         throw new OricException(e);
      }
   }

   /** Recursively descend symbol map, reconstructing object */
   private void genObject(String symbolName, Object object) {

      try {
         OricSymbolDict.SymbolType symType = OricSymbolDict.SymbolType
               .valueOf(symbolMap.get(symbolName + OricSymbolDict.SYMTYPE_KEY));
         String fieldTypeKey = symbolName + OricSymbolDict.FIELDTYPE_KEY;
         String childrenKey = symbolName + OricSymbolDict.CHILDREN_KEY;
         String numChildrenKey = symbolName + OricSymbolDict.NUMCHILDREN_KEY;
         String[] children = symbolMap.get(childrenKey).split(
               OricSymbolDict.CHILD_DELIMITER);
         String fieldType = symbolMap.get(fieldTypeKey);
         int numChildren;

         // Use the smallest of either the number of children or child array
         // length
         if (Integer.parseInt(symbolMap.get(numChildrenKey)) < children.length)
            numChildren = Integer.parseInt(symbolMap.get(numChildrenKey));
         else
            numChildren = children.length;

         if (symType == OricSymbolDict.SymbolType.ARRAY) {

            // if array objects were never instanced, return
            if (children[0].equals(OricSymbolDict.MISSING))
               return;

            OricSymbolDict.SymbolType symbolType = OricSymbolDict.SymbolType
                  .valueOf(symbolMap.get(children[0]
                        + OricSymbolDict.SYMTYPE_KEY));

            boolean isPrimitiveArray = symbolType == OricSymbolDict.SymbolType.PRIMITIVE;
            boolean isObjectArray = symbolType == OricSymbolDict.SymbolType.OBJECT;

            for (int i = 0; i < numChildren; i++) {

               // is array of primitives
               if (isPrimitiveArray) {

                  String fieldValue = symbolMap.get(children[i]
                        + OricSymbolDict.FIELDVALUE_KEY);

                  if (fieldType.equals("boolean"))
                     Array.set(object, i, Boolean.parseBoolean(fieldValue));
                  else if (fieldType.equals("int"))
                     Array.set(object, i, Integer.parseInt(fieldValue));
                  else if (fieldType.equals("char"))
                     Array.set(object, i, fieldValue.charAt(0));
                  else if (fieldType.equals("byte"))
                     Array.set(object, i, Byte.parseByte(fieldValue));
                  else if (fieldType.equals("short"))
                     Array.set(object, i, Short.parseShort(fieldValue));
                  else if (fieldType.equals("double"))
                     Array.set(object, i, Double.parseDouble(fieldValue));
                  else if (fieldType.equals("long"))
                     Array.set(object, i, Long.parseLong(fieldValue));
                  else if (fieldType.equals("float"))
                     Array.set(object, i, Float.parseFloat(fieldValue));
                  else if (fieldType.equals("String"))
                     Array.set(object, i, fieldValue);
               } // is array of objects
               else if (isObjectArray) {
                  Class<?> _class = null;
                  _class = Class.forName(symbolMap.get(fieldTypeKey));
                  try {
                     Array.set(object, i, _class.newInstance());
                  } catch (Exception e1) {

                     try {
                        // This is a hack to deal with some
                        // autogenerated classes that do not contain
                        // a no-args constructor.
                        Array.set(
                              object,
                              i,
                              _class.getMethod("from_int", int.class).invoke(
                                    _class, 0));
                     } catch (Exception e2) {
                        throw new OricException("Could not instantiate object.");
                     }
                  }
                  genObject(children[i], Array.get(object, i));
               }
            }
         } else if (symType == OricSymbolDict.SymbolType.OBJECT) {

            // if objects were never instanced, return
            if (children[0].equals(OricSymbolDict.MISSING))
               return;

            for (int i = 0; i < numChildren; i++) {

               OricSymbolDict.SymbolType symbolType = OricSymbolDict.SymbolType
                     .valueOf(symbolMap.get(children[i]
                           + OricSymbolDict.SYMTYPE_KEY));

               boolean childIsPrimitive = symbolType == OricSymbolDict.SymbolType.PRIMITIVE;
               boolean childIsArray = symbolType == OricSymbolDict.SymbolType.ARRAY;

               String childValueTypeKey = children[i]
                     + OricSymbolDict.FIELDTYPE_KEY;
               String childValueType = symbolMap.get(childValueTypeKey);

               String childValueKey = children[i]
                     + OricSymbolDict.FIELDVALUE_KEY;
               String childValue = symbolMap.get(childValueKey);
               String childNameKey = children[i] + OricSymbolDict.FIELDNAME_KEY;
               String childName = symbolMap.get(childNameKey);

               Class<?> _class = getClassForType(fieldType);
               Field field = null;

               /*
                * _class.getDeclaredFields() provides access to private members
                * in classes that _class.getField(name) does not support.
                */
               Field[] objFields = _class.getDeclaredFields();
               for (int n = 0; n < objFields.length; n++) {
                  if (childName.equals(objFields[n].getName())) {
                     objFields[n].setAccessible(true);
                     field = objFields[n];
                     break;
                  }
               }

               // child is primitive
               if (childIsPrimitive) {

                  try {
                     if (childValueType.equals("boolean"))
                        field.setBoolean(object,
                              Boolean.parseBoolean(childValue));
                     else if (childValueType.equals("int"))
                        field.setInt(object, Integer.parseInt(childValue));
                     else if (childValueType.equals("char"))
                        field.setChar(object, childValue.charAt(0));
                     else if (childValueType.equals("byte"))
                        field.setByte(object, Byte.parseByte(childValue));
                     else if (childValueType.equals("short"))
                        field.setShort(object, Short.parseShort(childValue));
                     else if (childValueType.equals("double"))
                        field.setDouble(object, Double.parseDouble(childValue));
                     else if (childValueType.equals("long"))
                        field.setLong(object, Long.parseLong(childValue));
                     else if (childValueType.equals("float"))
                        field.setFloat(object, Float.parseFloat(childValue));
                     else if (childValueType.equals("String"))
                        field.set(object, childValue);
                  } catch (IllegalAccessException e) {
                     // Some field types (like static finals) cannot be set
                     // so silently ignore.
                  }
               } // child is array
               else if (childIsArray) {
                  int arrayLen = Integer.parseInt(symbolMap.get(children[i]
                        + OricSymbolDict.NUMCHILDREN_KEY));
                  Object childObj = Array.newInstance(
                        getClassForType(childValueType), arrayLen);
                  field.set(object, childObj);
                  genObject(children[i], childObj);
               } // child is object
               else {
                  Class<?> childClass = getClassForType(symbolMap
                        .get(childValueTypeKey));
                  Object childObj = null;

                  try {
                     childObj = childClass.newInstance();
                  } catch (Exception e1) {

                     try {
                        // This is a hack to deal with some autogenerated
                        // classes that do not contain a no-args constructor.
                        childObj = childClass.getMethod("from_int", int.class)
                              .invoke(childClass, 0);
                     } catch (Exception e2) {
                        throw new OricException("Could not instantiate object.");
                     }
                  }
                  field.set(object, childObj);
                  genObject(children[i], childObj);
               }
            }
         }
      } catch (Exception e) {
         throw new OricException(e);
      }
   }

   /** Returns the current symbol count and increments. */
   private int takeNextSym() {
      int sym = symCounter;
      symCounter++;
      return sym;
   }

   /**
    * Restores the previous symbol count. Used if a null symbol is encountered.
    */
   private void returnLastSym() {
      symCounter--;
   }

   /** Returns the symbol count */
   private int getSymCount() {
      return symCounter;
   }

   /** The Recursive Descent Symbol Map (RDSM) */
   private HashMap<String, String> symbolMap = new HashMap<String, String>();

   /** The string representation of the baseClass */
   private String baseClassName;

   /** Symbol counter used when building symbol map */
   private int symCounter = 0;
}