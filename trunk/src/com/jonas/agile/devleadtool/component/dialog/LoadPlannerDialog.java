package com.jonas.agile.devleadtool.component.dialog;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.CutoverLength;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class LoadPlannerDialog extends JFileChooser {

   private final PlannerDAOExcelImpl dao;

   public LoadPlannerDialog(MyDesktopPane desktop, PlannerDAOExcelImpl plannerDAO, JFrame frame, PlannerHelper plannerHelper) {
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
         File xlsFile = getSelectedFile();

         dao.setXlsFile(xlsFile);

         RunnableDTO runnableDTO1 = new RunnableDTO() {
            public void run() {
               try {
                  setModel(dao.loadBoardModel());
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         };
         RunnableDTO runnableDTO2 = new RunnableDTO() {
            public void run() {
               try {
                  setModel(dao.loadPlanModel());
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         };
         RunnableDTO runnableDTO3 = new RunnableDTO() {
            public void run() {
               try {
                  setModel(dao.loadJiraModel());
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         };

         Thread t1 = new Thread(runnableDTO1);
         Thread t2 = new Thread(runnableDTO2);
         Thread t3 = new Thread(runnableDTO3);

         t1.start();
         t2.start();
         t3.start();

         try {
            t1.join();
            t2.join();
            t3.join();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }

         // BoardTableModel boardModel = dao.loadBoardModel();
         // PlanTableModel planModel = dao.loadPlanModel();
         // JiraTableModel jiraModel = dao.loadJiraModel();

         InternalTabPanel internalFrameTabPanel = new InternalTabPanel(plannerHelper, runnableDTO1.getModel(), runnableDTO2.getModel(), runnableDTO3.getModel());
         MyInternalFrame internalFrame = new MyInternalFrame(plannerHelper, plannerHelper.getTitle(), internalFrameTabPanel, dao);
         desktop.addInternalFrame(internalFrame);
         internalFrame.setExcelFile(xlsFile.getAbsolutePath(), CutoverLength.DEFAULT);

      }
   }

}
