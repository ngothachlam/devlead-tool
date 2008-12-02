package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.SwingUtil;

public class BoardStatusCellEditor extends DefaultCellEditor {

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
}
