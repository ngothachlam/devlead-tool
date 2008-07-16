package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.InternalFrame;

public class PlannerDialog extends JDialog {
	public PlannerDialog(JFrame parent, DesktopPane desktopPane) {
		PlannerHelper plannerHolder = new PlannerHelper("Planner");
		desktopPane.addInternalFrame(new InternalFrame(plannerHolder));

		setVisible(false);
	}

}
