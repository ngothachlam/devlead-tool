package com.jonas.agile.devleadtool.gui.component.table.renderer;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class BoardStatusCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer{

//   private int colEdited;
//   private int rowEdited;
//
//   public BoardStatusCellRenderer(final JComboBox combo, final JTable table) {
//      super();
////      super(combo);
//      if (getComponent() instanceof JComponent) {
//         JComponent component = (JComponent) getComponent();
//         component.setBorder(SwingUtil.focusCellBorder);
//         component.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "pressed");
//         component.getActionMap().put("pressed", new AbstractAction() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//               boolean ok = table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
//               Component comp = table.getEditorComponent();
//               if (ok && comp instanceof JComboBox) {
//                  JComboBox comboBox = (JComboBox) comp;
//                  comboBox.requestFocusInWindow();
//                  comboBox.setPopupVisible(true);
//               }
//            }
//
//         });
//
//      }
//   }
//
//   @Override
//   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
//      rowEdited = row;
//      colEdited = col;
//      return super.getTableCellEditorComponent(table, value, isSelected, row, col);
//   }
//
//   @Override
//   public int getColEdited() {
//      return colEdited;
//   }
//
//   @Override
//   public int getRowEdited() {
//      return rowEdited;
//   }
//
//   @Override
//   public Object getValue() {
//      return ((JComboBox)getComponent()).getSelectedItem();
//   }
}
