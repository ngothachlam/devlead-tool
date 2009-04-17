package com.jonas.testing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXTable;
import com.jonas.testHelpers.TryoutTester;

public class ActionTest {

   public ActionTest() {
      Object[][] rowData = getColAndRow(30, 23);
      Object[] colData = getRow("col", 23);

      JXTable table = new JXTable(rowData, colData);
      table.setColumnControlVisible(true);
      table.setFillsViewportHeight(true);

      JScrollPane scrollPane = new JScrollPane(table);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(scrollPane, BorderLayout.CENTER);

      JPopupMenu menu = new JPopupMenu("Popup Menu");
      menu.add(new TestAction("test action", "this is the longer description for this action"));
      menu.add(new TestAction("mark", "this marks selected rows", new Integer(KeyEvent.VK_M)));

      table.addMouseListener(new MousePopupListener(menu));

      TryoutTester.showInFrame(panel);
   }

   private Object[][] getColAndRow(int rows, int cols) {
      Object[][] data = new Object[rows][cols];
      for (int row = 0; row < data.length; row++) {
         data[row] = getRow(new Integer(row).toString(), cols);
      }
      return data;
   }

   private Object[] getRow(String prefix, int cols) {
      Object[] data = new Object[cols];
      for (int col = 0; col < data.length; col++) {
         data[col] = prefix + "-" + col;
      }
      return data;
   }

   public static void main(String[] args) {
      ActionTest test = new ActionTest();
   }

}


class TestAction extends AbstractAction {

   public TestAction() {
      super();
   }

   public TestAction(String name, Icon icon) {
      super(name, icon);
   }

   public TestAction(String name, String shortDesc, Icon icon) {
      this(name, icon);
      putValue(SHORT_DESCRIPTION, shortDesc);
   }

   public TestAction(String name, String shortDesc) {
      this(name);
      putValue(SHORT_DESCRIPTION, shortDesc);
   }
   
   public TestAction(String name, String shortDesc, Integer mnemonic) {
      this(name, shortDesc);
      putValue(MNEMONIC_KEY, mnemonic);
   }
   public TestAction(String name, String shortDesc, Integer mnemonic, Integer accelerator) {
      this(name, shortDesc, mnemonic);
      putValue(ACCELERATOR_KEY, accelerator);
   }

   public TestAction(String name) {
      super(name);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      System.out.println(getValue(SHORT_DESCRIPTION));
   }
}


class MousePopupListener extends MouseAdapter {
   private final JPopupMenu popup;

   public MousePopupListener(JPopupMenu menu) {
      this.popup = menu;
   }

   @Override
   public void mousePressed(MouseEvent e) {
      checkPopup(e);
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      checkPopup(e);
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      checkPopup(e);
   }

   private void checkPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
         popup.show((Component) e.getSource(), e.getX(), e.getY());
      }
   }
}
