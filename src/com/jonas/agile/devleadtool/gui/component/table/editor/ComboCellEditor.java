package com.jonas.agile.devleadtool.gui.component.table.editor;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;

import com.jonas.common.swing.SwingUtil;

public class ComboCellEditor extends DefaultCellEditor implements MyEditor {

   private int colEdited;
   private int rowEdited;

   public ComboCellEditor(final JComboBox combo, final JTable table) {
      super(combo);
      if (getComponent() instanceof JComponent) {
         JComponent component = (JComponent) getComponent();
         component.setBorder(SwingUtil.focusCellBorder);
         
         //FIXME 1 - Fix this combo box in the table!!
         // component.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "pressed");
         // component.getActionMap().put("pressed", new AbstractAction() {
         //
         // @Override
         // public void actionPerformed(ActionEvent e) {
         // boolean ok = table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
         // Component comp = table.getEditorComponent();
         // if (ok && comp instanceof JComboBox) {
         // JComboBox comboBox = (JComboBox) comp;
         // comboBox.requestFocusInWindow();
         // comboBox.setPopupVisible(true);
         // }
         // }
         //
         // });

      }
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
      rowEdited = row;
      colEdited = col;
      return super.getTableCellEditorComponent(table, value, isSelected, row, col);
   }

   @Override
   public int getColEdited() {
      return colEdited;
   }

   @Override
   public int getRowEdited() {
      return rowEdited;
   }

   @Override
   public Object getValue() {
      return ((JComboBox) getComponent()).getSelectedItem();
   }
}
