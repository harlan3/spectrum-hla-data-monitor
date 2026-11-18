/*
 *  Spectrum HLA data monitor tool
 *
 *  Copyright (C) 2024 Harlan Murphy
 *  Orbis Software - orbisoftware@gmail.com
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */

package orbisoftware.hla_tools.spectrum_hla_monitor.sampleviewer;

import orbisoftware.oricrdsm.OricSymbolMap;
import orbisoftware.oricrdsm.OricSymbolDict;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.swing.outline.RowModel;

public class SymbolRowModel implements RowModel {

   @Override
   public Class<?> getColumnClass(int column) {
      switch (column) {
      case 0:
         return String.class;
      case 1:
         return String.class;
      case 2:
         return String.class;
      default:
         assert false;
      }
      return null;
   }

   @Override
   public int getColumnCount() {
      return 3;
   }

   public void setSymbolMap(OricSymbolMap oricSymbolMap) {
      this.oricSymbolMap = oricSymbolMap;
   }

   @Override
   public String getColumnName(int column) {

      switch (column) {
      case 0:
         return "Field Type";
      case 1:
         return "Data Type";
      case 2:
         return "Data Value";
      default:
         return "";
      }
   }

   @Override
   public Object getValueFor(Object node, int column) {

      switch (column) {
      case 0:
         return oricSymbolMap.getSymbolType((String) node).toString();
      case 1:
         return oricSymbolMap.getFieldType((String) node);
      case 2:
         return oricSymbolMap.getFieldValue((String) node);
      default:
         assert false;
      }
      return null;
   }

   @Override
   public boolean isCellEditable(Object node, int column) {

      switch (column) {
      case 2:
         return !readOnlyPaths.contains(getVariablePath((String) node));
      default:
         return false;
      }
   }

   @Override
   public void setValueFor(Object node, int column, Object value) {
      if (column == 2 && isCellEditable(node, column))
         oricSymbolMap.setFieldValue((String) node, (String) value);
   }

   public OricSymbolMap getOricSymbolMap() {
      return oricSymbolMap;
   }

   /**
    * Marks a variable read-only (or editable) in the tree table. A simple field
    * name is accepted if unique; use a qualified path for duplicate names,
    * e.g. "radio.frequency" or "channels[0].frequency".
    * This restriction only affects user edits, not updateSymbolValue().
    * Returns false if the variable cannot be found.
    */
   public boolean setSymbolReadOnly(String variableName, boolean readOnly) {
      String symbol = findSymbol(variableName);
      if (symbol == null)
         return false;
      String path = getVariablePath(symbol);
      if (readOnly)
         readOnlyPaths.add(path);
      else
         readOnlyPaths.remove(path);
      return true;
   }

   /** Returns whether a named variable is protected against tree-table edits. */
   public boolean isSymbolReadOnly(String variableName) {
      String symbol = findSymbol(variableName);
      return symbol != null && readOnlyPaths.contains(getVariablePath(symbol));
   }

   /**
    * Updates a primitive symbol even if it is read-only in the UI.
    * Returns false for an unknown variable or a non-primitive node.
    * An ambiguous simple name raises IllegalArgumentException.
    */
   public boolean updateSymbolValue(String variableName, String value) {
      String symbol = findSymbol(variableName);
      if (symbol == null || oricSymbolMap.getSymbolType(symbol)
            != OricSymbolDict.SymbolType.PRIMITIVE)
         return false;
      oricSymbolMap.setFieldValue(symbol, value);
      return true;
   }

   /**
    * Resolve an unqualified field name or a full path to a symbol key.
    * Matching is exact and case-sensitive. Duplicate simple names must be
    * disambiguated with the path (without the root object's name).
    */
   private String findSymbol(String variableName) {
      if (variableName == null || variableName.isEmpty())
         throw new IllegalArgumentException("variableName must not be empty");
      if (oricSymbolMap == null)
         return null;
      String found = null;
      for (int i = 1; i < oricSymbolMap.getSymbolCount(); i++) {
         String symbol = OricSymbolDict.SYM_PREFIX + i;
         String fieldName = oricSymbolMap.getFieldName(symbol);
         if (variableName.equals(fieldName) || variableName.equals(getVariablePath(symbol))) {
            if (found != null)
               throw new IllegalArgumentException("Ambiguous variable name: " + variableName
                     + "; use a qualified path");
            found = symbol;
         }
      }
      return found;
   }

   private String getVariablePath(String symbol) {
      StringBuilder path = new StringBuilder();
      String root = oricSymbolMap.getBaseSymbol();
      String current = symbol;
      while (current != null && !current.equals(root)) {
         String name = oricSymbolMap.getFieldName(current);
         if (name == null)
            break;
         if (path.length() == 0)
            path.insert(0, name);
         else
            path.insert(0, name + (path.charAt(0) == '[' ? "" : "."));
         current = oricSymbolMap.getParent(current);
      }
      return path.toString();
   }

   private final Set<String> readOnlyPaths = new HashSet<String>();
   private OricSymbolMap oricSymbolMap;
}
