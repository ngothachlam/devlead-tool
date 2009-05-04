package com.jonas.common.swing;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class MyComponentPanel extends MyPanel {

   private List<MyComponentPanel> changeListeners = new ArrayList<MyComponentPanel>();
   private List<MyComponentPanel> closeListeners = new ArrayList<MyComponentPanel>();

   public MyComponentPanel(LayoutManager layoutManager) {
      super(layoutManager);
   }

   public final void addComponentListener(MyComponentPanel panel) {
      changeListeners.add(panel);
      panel.closeListeners.add(this);
   }

   public final void removeComponentListener(MyComponentPanel panel) {
      changeListeners.remove(panel);
      panel.closeListeners.remove(this);
   }

   int[] getArrayOfIndices(MyTable table, ColumnType... columns) {
      List<Integer> array = new ArrayList<Integer>();
      for (ColumnType column : columns) {
         int columnIndex = table.getColumnIndex(column);
         if (columnIndex >= 0) {
            addToArrayIfPositive(columnIndex, array);
         }
      }
      int[] intArray = new int[array.size()];
      for (int i = 0; i < intArray.length; i++) {
         intArray[i] = array.get(i);
      }
      return intArray;
   }

   void addToArrayIfPositive(Integer columnIndexOne, List<Integer> array) {
      if (columnIndexOne >= 0)
         array.add(columnIndexOne);
   }
}
