package com.jonas.agile.devleadtool.component.dialog;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class LoadPlannerDialog extends JFileChooser {

   private final PlannerDAO dao;
   private final JFrame frame;
   private final DaoListener daoListener;
   private final PlannerHelper plannerHelper;
   private final MyDesktopPane desktop;

   public LoadPlannerDialog(MyDesktopPane desktop, PlannerDAO plannerDAO, JFrame frame, PlannerHelper plannerHelper, DaoListener daoListener) {
      super(new File("."));
      this.desktop = desktop;
      this.dao = plannerDAO;
      this.frame = frame;
      this.plannerHelper = plannerHelper;
      this.daoListener = daoListener;

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
   }

   public void start() {
      int result = showOpenDialog(frame);

      if (result == JFileChooser.APPROVE_OPTION) {
         final File xlsFile = getSelectedFile();

         dao.setXlsFile(xlsFile);

         SwingWorker<ModelDTO, Object> swingWorker = new SwingWorker<ModelDTO, Object>() {
            @Override
            protected ModelDTO doInBackground() throws Exception {
               BoardTableModel boardModel = dao.loadBoardModel();
               JiraTableModel jiraModel = dao.loadJiraModel();

               return new ModelDTO(boardModel, jiraModel);
            }

            @Override
            protected void done() {
               try {
                  ModelDTO dto = get();
                  if (dto != null) {
                     InternalTabPanel internalFrameTabPanel = new InternalTabPanel(plannerHelper, dto.getBoardModel(), dto.getJiraModel());
                     MyInternalFrame internalFrame = new MyInternalFrame(plannerHelper, plannerHelper.getTitle(), internalFrameTabPanel, dao);
                     desktop.addInternalFrame(internalFrame);
                     internalFrame.setSaveFile(xlsFile);
                  }
                  super.done();
               } catch (Throwable e) {
                  AlertDialog.alertException(plannerHelper.getParentFrame(), e);
                  e.printStackTrace();
               }
            }
         };

         swingWorker.execute();
      }
   }
}
