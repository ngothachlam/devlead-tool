package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.gui.jigloo.NewJInternalFrame;

public class OpenPlannerDialog {
   public OpenPlannerDialog(JFrame parent, DesktopPane desktopPane, final PlannerHelper plannerHelper) {
      final InternalFrame internalFrame = new InternalFrame(plannerHelper.getTitle());
      desktopPane.addInternalFrame(internalFrame);
      
      // FIXME use GUIINternalFrame!!
//      final JInternalFrame internalFrame = new NewJInternalFrame();
//      desktopPane.add(internalFrame);
      
      internalFrame.setVisible(true);
   }

}
