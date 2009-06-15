package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.frame.main.MainFrame;
import com.jonas.agile.devleadtool.gui.component.panel.SprintManagerPanel;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;

public class SprintManagerGuiAction extends BasicAbstractGUIAction {

   private static final Logger log = MyLogger.getLogger(SprintManagerGuiAction.class);

   private SprintManagerPanel sprintManagerPanel;
   private MainFrame frame;

   public SprintManagerGuiAction(Frame parentFrame, PlannerHelper helper, ExcelSprintDao sprintDao) {
      super("Manage Sprints", "Manage sprints!", parentFrame);
      frame = new MainFrame("Sprint Manager");
      sprintManagerPanel = new SprintManagerPanel(helper, sprintDao, parentFrame);
      frame.setContentPane(sprintManagerPanel);
   }


   @Override
   public void doActionPerformed(ActionEvent e) {
      sprintManagerPanel.initialiseForViewing();
      
      frame.pack();
      log.debug("gui's parent frame: " + getParentFrame());
      SwingUtil.centreWindowWithinWindow(frame, getParentFrame());
      frame.setVisible(true);
   }
}

