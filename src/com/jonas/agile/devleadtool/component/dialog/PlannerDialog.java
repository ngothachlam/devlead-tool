package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.gui.jigloo.NewJInternalFrame;

public class PlannerDialog {
	public PlannerDialog(JFrame parent, DesktopPane desktopPane, final PlannerHelper plannerHelper) {
//		final InternalFrame internalFrame1 = new InternalFrame(plannerHelper, plannerHelper.getTitle());
//	   desktopPane.addInternalFrame(internalFrame1);
	   //FIXME use GUIINternalFrame!!
		final JInternalFrame internalFrame = new NewJInternalFrame();
		desktopPane.add(internalFrame);
		internalFrame.setVisible(true);
	}

}
