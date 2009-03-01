package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.SwingWorker;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.SaveKeyListener;
import com.jonas.agile.devleadtool.component.panel.MyInternalFrameInnerPanel;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public abstract class AbstractInternalFrameCreatorSwingthread extends SwingWorker<CombinedModelDTO, Object> {

   public AbstractInternalFrameCreatorSwingthread(PlannerHelper helper, PlannerDAO dao, SavePlannerDialog savePlannerDialog,
         SaveKeyListener saveKeyListener, DesktopPane desktopPane) {
      super();
      this.helper = helper;
      this.dao = dao;
      this.savePlannerDialog = savePlannerDialog;
      this.saveKeyListener = saveKeyListener;
      this.desktopPane = desktopPane;
   }

   private PlannerHelper helper;
   private PlannerDAO dao;
   private SavePlannerDialog savePlannerDialog;
   private SaveKeyListener saveKeyListener;
   private DesktopPane desktopPane;

   @Override
   protected void done() {
      try {
         CombinedModelDTO dto = get();
         MyInternalFrameInnerPanel internalFrameTabPanel = new MyInternalFrameInnerPanel(helper, dto.getBoardModel(), dto.getJiraModel());
         MyInternalFrame internalFrame = new MyInternalFrame(helper, helper.getTitle(), internalFrameTabPanel, dao, savePlannerDialog,
               saveKeyListener, desktopPane);
         internalFrame.setVisible(true);
      } catch (Throwable e) {
         AlertDialog.alertException(helper.getParentFrame(), e);
         e.printStackTrace();
      }
   }

}