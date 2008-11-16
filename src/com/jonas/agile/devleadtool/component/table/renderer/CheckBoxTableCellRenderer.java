package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class CheckBoxTableCellRenderer extends JPanel implements TableCellRenderer {

   private JCheckBox checkbox = new JCheckBox();
   private MyTableModel model = null;

   public CheckBoxTableCellRenderer(AbstractTableModel model) {
      super(new BorderLayout());
      if (model instanceof MyTableModel)
         this.model = (MyTableModel) model;
      checkbox.setHorizontalAlignment(JLabel.CENTER);
      this.add(checkbox, SwingUtilities.CENTER);
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

      MyTableCellRenderer.setBackground(table, isSelected, hasFocus, row, column, checkbox, model, value, this);
      
      if (!table.isCellEditable(row, column)) {
         checkbox.setEnabled(false);
      } else
         checkbox.setEnabled(true);

      setSelected((value != null && ((Boolean) value).booleanValue()) ? true : false, row);

      return this;
   }

   private void setSelected(final boolean b, final int row) {
      checkbox.setSelected(b);
   }

   JCheckBox getCheckBox() {
      return checkbox;
   }

}
