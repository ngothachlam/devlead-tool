package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.log4j.Logger;
import com.jonas.common.ColorUtil;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class ComboTableCellRenderer extends DefaultTableCellRenderer {

   private static final Logger log = MyLogger.getLogger(ComboTableCellRenderer.class);

   private JComboBox combo;

   public ComboTableCellRenderer() {
      super();
   }

   @SuppressWarnings("unchecked")
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      
      if (hasFocus) {
         cell.setBackground(SwingUtil.getTableCellFocusBackground());
      } else if (isSelected) {
         cell.setBackground(table.getSelectionBackground());
      } else {
         cell.setBackground(table.getBackground());
      }
      if (!table.isCellEditable(row, column) && !hasFocus) {
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -55));
      }
      
      return cell;
   }

}
