package com.jonas.agile.devleadtool.gui.component.dialog;

import java.io.File;
import javax.swing.SwingWorker;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.agile.devleadtool.gui.component.DesktopPane;
import com.jonas.agile.devleadtool.gui.component.MyInternalFrame;
import com.jonas.agile.devleadtool.gui.component.SaveKeyListener;
import com.jonas.agile.devleadtool.gui.component.panel.MyInternalFrameInnerPanel;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;

public abstract class AbstractInternalFrameCreatorSwingthread extends SwingWorker<CombinedModelDTO, Object> {

   private File xlsFile;

   public AbstractInternalFrameCreatorSwingthread(PlannerHelper helper, PlannerDAO dao, SavePlannerDialog savePlannerDialog,
         SaveKeyListener saveKeyListener, DesktopPane desktopPane, File xlsFile, ExcelSprintDao sprintDao) {
      this(helper, dao, savePlannerDialog, saveKeyListener, desktopPane, sprintDao);
      this.xlsFile = xlsFile;
   }

   public AbstractInternalFrameCreatorSwingthread(PlannerHelper helper, PlannerDAO dao, SavePlannerDialog savePlannerDialog,
         SaveKeyListener saveKeyListener, DesktopPane desktopPane, ExcelSprintDao sprintDao) {
      super();
      this.helper = helper;
      this.dao = dao;
      this.savePlannerDialog = savePlannerDialog;
      this.saveKeyListener = saveKeyListener;
      this.desktopPane = desktopPane;
      this.sprintDao = sprintDao;
   }

   private PlannerHelper helper;
   private PlannerDAO dao;
   private SavePlannerDialog savePlannerDialog;
   private SaveKeyListener saveKeyListener;
   private DesktopPane desktopPane;
   private ExcelSprintDao sprintDao;

   @Override
   protected void done() {
      try {
         CombinedModelDTO dto = get();
         if (dto == null) {
            return;
         }
         MyInternalFrameInnerPanel internalFrameTabPanel = new MyInternalFrameInnerPanel(helper, dto.getBoardModel(), dto.getJiraModel(), dto.getSprintCache(), sprintDao);
         MyInternalFrame internalFrame = new MyInternalFrame(helper, helper.getTitle(), internalFrameTabPanel, dao, savePlannerDialog,
               saveKeyListener, desktopPane);
         if (xlsFile != null) {
            internalFrame.setSaveFile(xlsFile);
         }
         internalFrame.setVisible(true);
      } catch (Throwable e) {
         AlertDialog.alertException(helper.getParentFrame(), e);
         e.printStackTrace();
      }
   }

}