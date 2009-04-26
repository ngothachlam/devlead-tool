package com.jonas.agile.devleadtool.component.dialog;

import java.awt.GridLayout;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.common.swing.MyComponentPanel;
import com.jonas.testHelpers.TryoutTester;

public class AddVersionDialogTestScreen {
   public static void main(String... args) {
      JFrame frame = TryoutTester.getEmptyFrame();
      frame.setVisible(true);
      MyTable tableA = new MyTable("A", new BoardTableModel(null), false);
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
