package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.tree.model.ReleaseTreeModel;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyComponentPanel;

public class ReleaseNotePanel extends MyComponentPanel {

   private Logger log = MyLogger.getLogger(ReleaseNotePanel.class);

   public ReleaseNotePanel() {
      this(new ReleaseTreeModel(null));
   }

   public ReleaseNotePanel(ReleaseTreeModel planModel) {
      super(new BorderLayout());
      ReleaseTreeModel model = planModel;

      JTree tree = new JTree(model);
      JScrollPane scrollpane = new JScrollPane(tree);

      this.addCenter(scrollpane);
      this.addSouth(getBottomPanel());
   }

   private Component getBottomPanel() {
      JPanel buttons = new JPanel();
      JButton refreshFixVersions = new JButton("");

      buttons.add(refreshFixVersions);
      return buttons;
   }

}