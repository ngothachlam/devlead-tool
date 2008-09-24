package com.jonas.common;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import com.jonas.agile.devleadtool.component.listener.FilterDocumentListener;
import com.jonas.agile.devleadtool.component.listener.FilterDocumentListenerListener;
import com.jonas.agile.devleadtool.component.listener.FilterDocumentListenerListenerImpl;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;

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

   protected void addFilter(JPanel buttonPanel, MyTable table, TableRowSorter<TableModel> sorter, Column... columns) {
      addLabel(buttonPanel, "Jira Filter:");
      JTextField filterText = addTextField(buttonPanel, 20);
      FilterDocumentListenerListener filterDocumentListenerListener = new FilterDocumentListenerListenerImpl(filterText, table, sorter, columns);
      filterText.getDocument().addDocumentListener(new FilterDocumentListener(filterDocumentListenerListener));
   }

}
