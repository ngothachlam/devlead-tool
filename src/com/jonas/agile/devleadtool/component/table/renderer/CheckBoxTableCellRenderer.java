package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class CheckBoxTableCellRenderer extends JPanel implements TableCellRenderer {

   private static final Logger log = MyLogger.getLogger(CheckBoxTableCellRenderer.class);
   private JCheckBox checkbox = new JCheckBox();
   private Border border;
   private Border border2;
   private MyTableModel model = null;

   public CheckBoxTableCellRenderer(AbstractTableModel model) {
      super(new BorderLayout());
      if (model instanceof MyTableModel)
         this.model = (MyTableModel) model;
      checkbox.setHorizontalAlignment(JLabel.CENTER);
      this.add(checkbox, SwingUtilities.CENTER);
      border = UIManager.getBorder("Table.focusCellHighlightBorder");
      border2 = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

      if (hasFocus)
         checkbox.setBackground(SwingUtil.getTableCellFocusBackground());
      else if (isSelected)
         checkbox.setBackground(table.getSelectionBackground());
      else
         checkbox.setBackground(table.getBackground());

      if (hasFocus) {
         setBorder(border);
      } else {
         setBorder(border2);
      }

      if (!table.isCellEditable(row, column)) {
         checkbox.setEnabled(false);
      } else
         checkbox.setEnabled(true);

      if (model != null) {
         if (model.isRed(value, row, column)) {
            checkbox.setBackground(new Color(200, 0, 0));
         }
      }

      setSelected((value != null && ((Boolean) value).booleanValue()) ? true : false);
      return this;
   }

   private void setSelected(boolean b) {
      checkbox.setSelected(b);
   }

   JCheckBox getCheckBox() {
      return checkbox;
   }

}
