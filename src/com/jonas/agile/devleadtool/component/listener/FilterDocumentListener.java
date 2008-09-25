/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;

public final class FilterDocumentListener implements DocumentListener {

   private final FilterDocumentListenerListener listener;
   private Logger log = MyLogger.getLogger(FilterDocumentListener.class);
   private final JTextField results;
   private final JComboBox typeOfFilter;
   private final MyTable table;

   public FilterDocumentListener(FilterDocumentListenerListener listener, JComboBox typeOfFilter, JTextField results, MyTable table) {
      this.listener = listener;
      this.typeOfFilter = typeOfFilter;
      this.results = results;
      this.table = table;
      log.debug("creating FilterDocumentListener!");
   }

   public void changedUpdate(DocumentEvent e) {
      FilterType filterType = (FilterType) typeOfFilter.getSelectedItem();
      listener.newFilter(filterType);
      results.setText(getResultText(listener.getSearches()));
   }

   private String getResultText(int noOfSearches){
      StringBuffer sb = new StringBuffer();
      sb.append("Searched: ");
      sb.append(noOfSearches);
      sb.append(" Found: ");
      sb.append(table.getRowCount());
      sb.append(" Total: ");
      sb.append(table.getModel().getRowCount());
      return sb.toString();
      }

   public void insertUpdate(DocumentEvent e) {
      FilterType filterType = (FilterType) typeOfFilter.getSelectedItem();
      listener.newFilter(filterType);
      results.setText(getResultText(listener.getSearches()));
   }

   public void removeUpdate(DocumentEvent e) {
      FilterType filterType = (FilterType) typeOfFilter.getSelectedItem();
      listener.newFilter(filterType);
      results.setText(getResultText(listener.getSearches()));
   }
}