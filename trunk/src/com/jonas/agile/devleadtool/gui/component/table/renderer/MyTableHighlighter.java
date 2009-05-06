package com.jonas.agile.devleadtool.gui.component.table.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;

import com.jonas.agile.devleadtool.gui.component.table.ColorDTO;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.ColorUtil;
import com.jonas.common.swing.SwingUtil;

public class MyTableHighlighter extends AbstractHighlighter {

   private final MyTable table;
   private final MyTableModel model;

   public MyTableHighlighter(MyTable table) {
      this.table = table;
      this.model = table.getMyModel();
   }

   @Override
   public Component doHighlight(Component component, ComponentAdapter adapter) {
      int row = adapter.row;
      int column = adapter.column;

      boolean hasFocus = adapter.hasFocus();
      boolean isSelected = adapter.isSelected();
      boolean isEditable = adapter.isEditable();

      JComponent jComponent = null;
      if (component instanceof JComponent) {
         jComponent = (JComponent) component;
      }

      if (hasFocus) {
         component.setBackground(SwingUtil.getTableCellFocusBackground());
         if (jComponent != null) {
            jComponent.setBorder(SwingUtil.focusCellBorder);
         }
      } else if (isSelected) {
         component.setBackground(table.getSelectionBackground());
         if (jComponent != null) {
            jComponent.setBorder(SwingUtil.defaultCellBorder);
         }
      } else {
         component.setBackground(table.getBackground());
         if (jComponent != null) {
            jComponent.setBorder(SwingUtil.defaultCellBorder);
         }
      }
      if (!isEditable && !hasFocus) {
         component.setBackground(ColorUtil.darkenColor(component.getBackground(), -75));
      }

      if (model != null) {
         setColor(table, isSelected, hasFocus, row, column, component, model, adapter.getValue(), table);
      }

      return component;
   }

   private void setColor(final JTable table, final boolean isSelected, final boolean hasFocus, final int row, final int column, final Component cell, final MyTableModel model, final Object value, final MyTable myTable) {
      ColorDTO colorDTO = model.getColor(value, table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column));

      Color color = colorDTO.getColor();
      if (color != null) {
         if (isSelected) {
            color = ColorUtil.darkenColor(color, -55);
         }
         cell.setBackground(color);
      } else if (myTable != null && !isSelected && !hasFocus && myTable.isMarked(row)) {
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -35));
      }

      if (colorDTO.isMarked()) {
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -25));
      } else {
         cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), +35));
      }
   }
}