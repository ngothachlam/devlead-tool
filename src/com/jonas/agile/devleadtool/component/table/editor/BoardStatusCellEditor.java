package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import com.jonas.common.SwingUtil;

public class BoardStatusCellEditor extends DefaultCellEditor implements MyEditor{

   private int colEdited;
   private int rowEdited;

   public BoardStatusCellEditor(final JComboBox combo, final JTable table) {
      super(combo);
      if (getComponent() instanceof JComponent) {
         JComponent component = (JComponent) getComponent();
         component.setBorder(SwingUtil.focusCellBorder);
         component.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "pressed");
         component.getActionMap().put("pressed", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
               // when space is prssed we want to start editing and expand the ComboBox as well as focus it 
               // (to allow for arrow key selection changes)
               boolean ok = table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
               Component comp = table.getEditorComponent();
               if (ok && comp instanceof JComboBox) {
                  JComboBox comboBox = (JComboBox) comp;
                  comboBox.requestFocusInWindow();
                  comboBox.setPopupVisible(true);
               }
            }

         });

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
      return ((JComboBox)getComponent()).getSelectedItem();
   }
}
