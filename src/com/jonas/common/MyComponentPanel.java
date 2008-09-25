package com.jonas.common;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.listener.FilterDocumentListener;
import com.jonas.agile.devleadtool.component.listener.FilterDocumentListenerListener;
import com.jonas.agile.devleadtool.component.listener.FilterType;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;

public class MyComponentPanel extends MyPanel {

   private Logger log = MyLogger.getLogger(MyComponentPanel.class);
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
      final JComboBox typeOfFilter = addComboBox(buttonPanel, FilterType.values());
      
      JTextField results = addTextField(buttonPanel, 20);
      results.setEditable(false);
      
      FilterDocumentListenerListener filterDocumentListenerListener = new FilterDocumentListenerListener(filterText, table, sorter, columns);
      final FilterDocumentListener filterDocumentListener = new FilterDocumentListener(filterDocumentListenerListener, typeOfFilter, results, table);
      filterText.getDocument().addDocumentListener(filterDocumentListener);
      
      table.getModel().addTableModelListener(new TableModelListener(){
         public void tableChanged(TableModelEvent e) {
            log.debug("changing state to: " + typeOfFilter.getSelectedItem());
            filterDocumentListener.changedUpdate(null);
         }
      });
      typeOfFilter.addItemListener(new ItemListener(){
         public void itemStateChanged(ItemEvent e) {
            log.debug("changing state to: " + typeOfFilter.getSelectedItem());
            filterDocumentListener.changedUpdate(null);
         }
      });
      
   }

}
