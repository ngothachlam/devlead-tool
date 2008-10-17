package com.jonas.common;

import java.awt.ComponentOrientation;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;

public class MyComponentPanel extends MyPanel {

   private List<MyComponentPanel> changeListeners = new ArrayList<MyComponentPanel>();
   private List<MyComponentPanel> closeListeners = new ArrayList<MyComponentPanel>();

   public MyComponentPanel(LayoutManager layoutManager) {
      super(layoutManager);
   }

   protected void savePreferences() {
      // override as needed
   }

   public final void close() {
      for (Iterator<MyComponentPanel> iterator = closeListeners.iterator(); iterator.hasNext();) {
         iterator.next().close();
      }
      savePreferences();
      closing();
   }

   protected void closing() {
      // override as needed
   }

   protected void fireComponentChanged() {
      componentChanged();
      for (Iterator<MyComponentPanel> iterator = changeListeners.iterator(); iterator.hasNext();) {
         iterator.next().fireComponentChanged();
      }
   }

   protected void componentChanged() {
      // override as needed
   }

   public final void addComponentListener(MyComponentPanel panel) {
      changeListeners.add(panel);
      panel.closeListeners.add(this);
   }

   public final void removeComponentListener(MyComponentPanel panel) {
      changeListeners.remove(panel);
      panel.closeListeners.remove(this);
   }

   int[] getArrayOfIndices(MyTable table, Column... columns) {
      List<Integer> array = new ArrayList<Integer>();
      for (Column column : columns) {
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

   private String getResultText(JTable table) {
      StringBuffer sb = new StringBuffer();
      sb.append(table.getRowCount());
      sb.append("/");
      sb.append(table.getModel().getRowCount());
      return sb.toString();
   }

}
