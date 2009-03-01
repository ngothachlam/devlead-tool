package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.SaveKeyListener;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.common.logging.MyLogger;

public class NewPlannerDialog {
   final MyDesktopPane desktopPane;
   final PlannerHelper helper;
   final PlannerDAOExcelImpl dao;
   final SavePlannerDialog savePlannerDialog;
   final SaveKeyListener saveKeyListener;
   private final static Logger log = MyLogger.getLogger(NewPlannerDialog.class);

   public NewPlannerDialog(final MyDesktopPane desktopPane, final PlannerHelper helper, final PlannerDAOExcelImpl dao, final SavePlannerDialog savePlannerDialog, final SaveKeyListener saveKeyListener) {
      this.desktopPane = desktopPane;
      this.helper = helper;
      this.dao = dao;
      this.savePlannerDialog = savePlannerDialog;
      this.saveKeyListener = saveKeyListener;
      
   }
   
   public void openNew(){
      SwingWorker<CombinedModelDTO, Object> worker = new AbstractInternalFrameCreatorSwingthread(helper, dao, savePlannerDialog, saveKeyListener, desktopPane){
         protected CombinedModelDTO doInBackground() throws Exception {
            return new CombinedModelDTO(new BoardTableModel(), new JiraTableModel());
         }
      };
      worker.execute();
   }
}

