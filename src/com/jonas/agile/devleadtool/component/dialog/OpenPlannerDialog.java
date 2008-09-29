package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.panel.InternalFrameTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class OpenPlannerDialog {
   public OpenPlannerDialog(JFrame parent, DesktopPane desktopPane, final PlannerHelper plannerHelper, PlannerDAOExcelImpl dao) {

      BoardTableModel boardModel = new BoardTableModel();
      PlanTableModel planModel = new PlanTableModel();
      InternalFrameTabPanel internalFrameTabPanel = new InternalFrameTabPanel(plannerHelper, boardModel, planModel);
      InternalFrame internalFrame = new InternalFrame(plannerHelper, plannerHelper.getTitle(), internalFrameTabPanel, dao);
      // final InternalFrame internalFrame = new InternalFrame(plannerHelper.getTitle());
      desktopPane.addInternalFrame(internalFrame);

      // FIXME use GUIINternalFrame!!
      // final JInternalFrame internalFrame = new NewJInternalFrame();
      // desktopPane.add(internalFrame);

      internalFrame.setVisible(true);
   }

}
