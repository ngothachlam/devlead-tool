package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.SwingWorker;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class OpenPlannerDialog {
   public OpenPlannerDialog(final MyDesktopPane desktopPane, final PlannerHelper plannerHelper, final PlannerDAOExcelImpl dao) {

      SwingWorker<ModelDTO, Object> worker = new SwingWorker<ModelDTO, Object>() {
         protected ModelDTO doInBackground() throws Exception {
            return new ModelDTO(new BoardTableModel(), new JiraTableModel());
         }

         @Override
         protected void done() {
            try {
               ModelDTO dto = get();
               InternalTabPanel internalFrameTabPanel = new InternalTabPanel(plannerHelper, dto.getBoardModel(), dto.getJiraModel());
               MyInternalFrame internalFrame = new MyInternalFrame(plannerHelper, plannerHelper.getTitle(), internalFrameTabPanel, dao);
               desktopPane.addInternalFrame(internalFrame);
               internalFrame.setVisible(true);
            } catch (Throwable e) {
               AlertDialog.alertException(plannerHelper.getParentFrame(), e);
               e.printStackTrace();
            }
         }

      };
      worker.execute();
   }

}
