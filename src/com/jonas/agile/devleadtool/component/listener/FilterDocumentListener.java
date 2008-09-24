/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.jonas.agile.devleadtool.component.listener.FilterDocumentListenerListener;

public final class FilterDocumentListener implements DocumentListener {

   private final FilterDocumentListenerListener listener;

   public FilterDocumentListener(FilterDocumentListenerListener listener) {
      this.listener = listener;
   }

   public void changedUpdate(DocumentEvent e) {
      listener.newFilter();
   }

   public void insertUpdate(DocumentEvent e) {
      listener.newFilter();
   }

   public void removeUpdate(DocumentEvent e) {
      listener.newFilter();
   }
}