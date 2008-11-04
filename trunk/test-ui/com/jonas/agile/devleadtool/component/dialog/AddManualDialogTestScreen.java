package com.jonas.agile.devleadtool.component.dialog;

import java.awt.GridLayout;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.testHelpers.TryoutTester;

public class AddManualDialogTestScreen {
   public static void main(String... args) {
      JFrame frame = TryoutTester.getFrame();
      frame.setVisible(true);
      MyTable tableA = new MyTable("A", new BoardTableModel());
      MyTable tableB = new MyTable("B", new JiraTableModel());

      MyComponentPanel panel = new MyComponentPanel(new GridLayout(2, 1));
      panel.add(tableA);
      panel.add(tableB);
      frame.setContentPane(panel);

      new AddManualDialog(frame, new MyTable[] { tableA, tableB });
   }
}
