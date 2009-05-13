package com.jonas.agile.devleadtool.gui.component.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;

import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;

import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class DuplicateHighlighter extends AbstractHighlighter {

   private EnabledQuery enabledQuery;

   private MyTableModel model;

   public DuplicateHighlighter(MyTable myTable) {
      this.model = myTable.getMyModel();
   }

   @Override
   protected Component doHighlight(Component cellComponent, ComponentAdapter componentAdapter) {
      if (enabledQuery != null && enabledQuery.isEnabled()) {
         if (cellComponent instanceof JComponent) {

            if (componentAdapter.column == 0) {
               JComponent textField = (JComponent) cellComponent;
               String jira = (String) model.getValueAt(ColumnType.Jira, componentAdapter.row);
               if (model.isJiraPresentAsDupe(jira)) {
                  textField.setForeground(Color.RED);
               }
            }
         }
      }
      return cellComponent;
   }

   public void setEnabledQuery(EnabledQuery enabledQuery) {
      this.enabledQuery = enabledQuery;
   }

}