package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.SaveKeyListener;
import com.jonas.agile.devleadtool.component.panel.MyInternalFrameInnerPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.common.logging.MyLogger;

public class NewPlannerDialog {
   private final MyDesktopPane desktopPane;
   private final PlannerHelper helper;
   private final PlannerDAOExcelImpl dao;
   private final SavePlannerDialog savePlannerDialog;
   private final SaveKeyListener saveKeyListener;
   private final static Logger log = MyLogger.getLogger(NewPlannerDialog.class);

   public NewPlannerDialog(final MyDesktopPane desktopPane, final PlannerHelper helper, final PlannerDAOExcelImpl dao, final SavePlannerDialog savePlannerDialog, final SaveKeyListener saveKeyListener) {
      this.desktopPane = desktopPane;
      this.helper = helper;
      this.dao = dao;
      this.savePlannerDialog = savePlannerDialog;
      this.saveKeyListener = saveKeyListener;
      
   }
   
   public void openNew(){
      log .trace("openNew");
      SwingWorker<CombinedModelDTO, Object> worker = new SwingWorker<CombinedModelDTO, Object>() {

         protected CombinedModelDTO doInBackground() throws Exception {
            return new CombinedModelDTO(new BoardTableModel(), new JiraTableModel());
         }
         
         @Override
         protected void done() {
            try {
               log .trace("done 1");
               CombinedModelDTO dto = get();
               MyInternalFrameInnerPanel internalFrameTabPanel = new MyInternalFrameInnerPanel(helper, dto.getBoardModel(), dto.getJiraModel());
               log .trace("done 1.1");
               MyInternalFrame internalFrame = new MyInternalFrame(helper, helper.getTitle(), internalFrameTabPanel, dao, savePlannerDialog, saveKeyListener, desktopPane);
               internalFrame.setVisible(true);
               log.trace("done 2");
            } catch (Throwable e) {
               AlertDialog.alertException(helper.getParentFrame(), e);
               e.printStackTrace();
            }
         }
         
         
         
      };
      worker.execute();
   }
}
