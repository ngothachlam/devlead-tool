package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.SwingWorker;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.SaveKeyListener;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class NewPlannerDialog {
   private final MyDesktopPane desktopPane;
   private final PlannerHelper helper;
   private final PlannerDAOExcelImpl dao;
   private final SavePlannerDialog savePlannerDialog;
   private final SaveKeyListener saveKeyListener;

   public NewPlannerDialog(final MyDesktopPane desktopPane, final PlannerHelper helper, final PlannerDAOExcelImpl dao, final SavePlannerDialog savePlannerDialog, final SaveKeyListener saveKeyListener) {
      this.desktopPane = desktopPane;
      this.helper = helper;
      this.dao = dao;
      this.savePlannerDialog = savePlannerDialog;
      this.saveKeyListener = saveKeyListener;
      
   }
   
   public void openNew(){
      SwingWorker<ModelDTO, Object> worker = new SwingWorker<ModelDTO, Object>() {
         protected ModelDTO doInBackground() throws Exception {
            return new ModelDTO(new BoardTableModel(), new JiraTableModel());
         }
         
         @Override
         protected void done() {
            try {
               ModelDTO dto = get();
               InternalTabPanel internalFrameTabPanel = new InternalTabPanel(helper, dto.getBoardModel(), dto.getJiraModel());
               MyInternalFrame internalFrame = new MyInternalFrame(helper, helper.getTitle(), internalFrameTabPanel, dao, savePlannerDialog, saveKeyListener, desktopPane);
               internalFrame.setVisible(true);
            } catch (Throwable e) {
               AlertDialog.alertException(helper.getParentFrame(), e);
               e.printStackTrace();
            }
         }
         
         
         
      };
      worker.execute();
   }
}
