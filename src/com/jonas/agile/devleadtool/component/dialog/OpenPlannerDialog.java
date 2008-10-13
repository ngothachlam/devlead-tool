package com.jonas.agile.devleadtool.component.dialog;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class OpenPlannerDialog {
   public OpenPlannerDialog(MyDesktopPane desktopPane, final PlannerHelper plannerHelper, PlannerDAOExcelImpl dao) {
      RunnableDTO runnableDTO1 = new RunnableDTO() {
         public void run() {
            setModel(new BoardTableModel());
         }
      };
      RunnableDTO runnableDTO2 = new RunnableDTO() {
         public void run() {
            setModel(new PlanTableModel());
         }
      };
      RunnableDTO runnableDTO3 = new RunnableDTO() {
         public void run() {
            setModel(new JiraTableModel());
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

      InternalTabPanel internalFrameTabPanel = new InternalTabPanel(plannerHelper, (BoardTableModel) runnableDTO1.getModel(), runnableDTO2.getModel(),
            (JiraTableModel) runnableDTO3.getModel());
      MyInternalFrame internalFrame = new MyInternalFrame(plannerHelper, plannerHelper.getTitle(), internalFrameTabPanel, dao);
      desktopPane.addInternalFrame(internalFrame);

      internalFrame.setVisible(true);
   }

}


abstract class RunnableDTO implements Runnable {
   private MyTableModel model;

   protected void setModel(MyTableModel boardTableModel) {
      this.model = boardTableModel;
   }

   public MyTableModel getModel() {
      return model;
   }

   public abstract void run();
}