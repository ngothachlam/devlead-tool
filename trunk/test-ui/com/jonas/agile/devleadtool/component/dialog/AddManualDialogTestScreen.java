package com.jonas.agile.devleadtool.component.dialog;

import java.awt.GridLayout;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.common.swing.MyComponentPanel;
import com.jonas.testHelpers.TryoutTester;

public class AddManualDialogTestScreen {
   public static void main(String... args) {
      JFrame frame = TryoutTester.getEmptyFrame();
      frame.setVisible(true);
      MyTable tableA = new MyTable("A", new BoardTableModel(null), true);
      MyTable tableB = new MyTable("B", new JiraTableModel(), true);

      MyComponentPanel panel = new MyComponentPanel(new GridLayout(2, 1));
      panel.add(tableA);
      panel.add(tableB);
      frame.setContentPane(panel);

      new AddManualDialog(frame, new MyTable[] { tableA, tableB });
   }
}
