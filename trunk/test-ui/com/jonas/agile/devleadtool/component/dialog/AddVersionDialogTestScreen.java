package com.jonas.agile.devleadtool.component.dialog;

import java.awt.GridLayout;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.testHelpers.TryoutTester;

public class AddVersionDialogTestScreen {
   public static void main(String... args) {
      JFrame frame = TryoutTester.getFrame();
      frame.setVisible(true);
      MyTable tableA = new MyTable("A", new BoardTableModel(), false);
      MyTable tableB = new MyTable("B", new JiraTableModel(), false);
      tableA.addJira("bla-1");
      tableB.addJira("bla-2");

      MyComponentPanel panel = new MyComponentPanel(new GridLayout(2, 1));
      panel.add(tableA);
      panel.add(tableB);
      frame.setContentPane(panel);

      new AddVersionDialog(frame, new MyTable[] { tableA, tableB });
   }

}
