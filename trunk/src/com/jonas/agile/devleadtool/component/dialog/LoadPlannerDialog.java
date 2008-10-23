package com.jonas.agile.devleadtool.component.dialog;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.CutoverLength;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class LoadPlannerDialog extends JFileChooser {

   private final PlannerDAOExcelImpl dao;

   public LoadPlannerDialog(final MyDesktopPane desktop, PlannerDAOExcelImpl plannerDAO, JFrame frame, final PlannerHelper plannerHelper, final DaoListener daoListener) {
      super(new File("."));
      this.dao = plannerDAO;

      addChoosableFileFilter(new FileFilter() {
         public boolean accept(File f) {
            if (getTypeDescription(f).equalsIgnoreCase("Microsoft Excel Worksheet") || f.isDirectory())
               return true;
            return false;
         }

         public String getDescription() {
            return "XLS files";
         }
      });
      int result = showOpenDialog(frame);

      if (result == JFileChooser.APPROVE_OPTION) {
         final File xlsFile = getSelectedFile();

         dao.setXlsFile(xlsFile);
         
         SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
               try {
                  dao.addListener(daoListener);
                  dao.notifyLoadingStarted();

                  BoardTableModel boardModel = dao.loadBoardModel();
                  JiraTableModel jiraModel = dao.loadJiraModel();
                  PlanTableModel planModel = dao.loadPlanModel();

                  ModelDto modelDto = new ModelDto(boardModel, jiraModel, planModel);
                  
                  return modelDto;
               } finally {
                  dao.notifyLoadingFinished();
                  dao.removeListener(daoListener);
               }
            }

            @Override
            protected void done() {
               ModelDto modelDto;
               try {
                  modelDto = (ModelDto) get();
                  InternalTabPanel internalFrameTabPanel = new InternalTabPanel(plannerHelper, modelDto.getBoardModel(), modelDto.getPlanModel(), modelDto.getJiraModel());
                  MyInternalFrame internalFrame = new MyInternalFrame(plannerHelper, plannerHelper.getTitle(), internalFrameTabPanel, dao);
                  desktop.addInternalFrame(internalFrame);
                  internalFrame.setExcelFile(xlsFile.getAbsolutePath(), CutoverLength.DEFAULT);
               } catch (Throwable e) {
                  AlertDialog.alertException(plannerHelper.getParentFrame(), e);
                  e.printStackTrace();
               }
               super.done();
            }
         };

         swingWorker.execute();
      }
   }
   
   private class ModelDto{
      private final BoardTableModel boardModel;
      private final JiraTableModel jiraModel;
      private final MyTableModel planModel;
      public ModelDto(BoardTableModel boardModel, JiraTableModel jiraModel, MyTableModel planModel) {
         this.boardModel = boardModel;
         this.jiraModel = jiraModel;
         this.planModel = planModel;
      }
      public BoardTableModel getBoardModel() {
         return boardModel;
      }
      public JiraTableModel getJiraModel() {
         return jiraModel;
      }
      public MyTableModel getPlanModel() {
         return planModel;
      }
   }
}
