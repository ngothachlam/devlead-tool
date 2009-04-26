package com.jonas.agile.devleadtool.gui.component.dialog;

import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.agile.devleadtool.gui.component.DesktopPane;
import com.jonas.agile.devleadtool.gui.component.SaveKeyListener;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;

public class NewPlannerDialog {
   final DesktopPane desktopPane;
   final PlannerHelper helper;
   final PlannerDAOExcelImpl dao;
   final SavePlannerDialog savePlannerDialog;
   final SaveKeyListener saveKeyListener;
   private ExcelSprintDao sprintDao;
   private final static Logger log = MyLogger.getLogger(NewPlannerDialog.class);

   public NewPlannerDialog(DesktopPane desktopPane, PlannerHelper helper, PlannerDAOExcelImpl dao, SavePlannerDialog savePlannerDialog, SaveKeyListener saveKeyListener, ExcelSprintDao sprintDao) {
      this.desktopPane = desktopPane;
      this.helper = helper;
      this.dao = dao;
      this.savePlannerDialog = savePlannerDialog;
      this.saveKeyListener = saveKeyListener;
      this.sprintDao = sprintDao;
      
   }
   
   public void openNew(){
      SwingWorker<CombinedModelDTO, Object> worker = new AbstractInternalFrameCreatorSwingthread(helper, dao, savePlannerDialog, saveKeyListener, desktopPane, sprintDao){
         @Override
         protected CombinedModelDTO doInBackground() throws Exception {
            SprintCache sprintCache = new SprintCache();
            return new CombinedModelDTO(new BoardTableModel(sprintCache), new JiraTableModel(), sprintCache);
         }
      };
      worker.execute();
   }
}

