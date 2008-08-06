package com.jonas.agile.devleadtool.component.dialog;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.InternalFrame;

public class PlannerDialog {
	public PlannerDialog(JFrame parent, DesktopPane desktopPane, final PlannerHelper plannerHelper) {
		final InternalFrame internalFrame = new InternalFrame(plannerHelper, plannerHelper.getTitle());
		desktopPane.addInternalFrame(internalFrame);
	}

}
