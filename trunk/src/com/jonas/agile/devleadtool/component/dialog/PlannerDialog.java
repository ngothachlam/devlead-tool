package com.jonas.agile.devleadtool.component.dialog;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.netbeans.guibuilding.GUIInternalFrame;

public class PlannerDialog {
	public PlannerDialog(JFrame parent, DesktopPane desktopPane, final PlannerHelper plannerHelper) {
		final InternalFrame internalFrame1 = new InternalFrame(plannerHelper, plannerHelper.getTitle());
	   desktopPane.addInternalFrame(internalFrame1);
		final JInternalFrame internalFrame = new GUIInternalFrame();
		desktopPane.add(internalFrame);
		internalFrame.setVisible(true);
	}

}
