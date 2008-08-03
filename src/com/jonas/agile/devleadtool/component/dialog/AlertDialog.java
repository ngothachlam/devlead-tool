package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.apache.commons.httpclient.HttpException;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.data.SystemProperties;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;

public class AlertDialog extends JDialog {
	private final PlannerHelper plannerHelper;
	private JTextArea textArea;

	public AlertDialog(JFrame parent, PlannerHelper plannerHelper, String alertMessage) {
		super(parent, "Alert ...", true);
		this.plannerHelper = plannerHelper;

		MyPanel panel = new MyPanel(new BorderLayout()).bordered(15, 15, 15, 15);
		textArea = new JTextArea(alertMessage);
		panel.addNorth(new JLabel("Message:"));
		panel.addCenter(textArea);
		setContentPane(panel);
		pack();
		SwingUtil.centreWindowWithinWindow(this, parent);
		setVisible(true);
	}

	public AlertDialog(JFrame parent, PlannerHelper plannerHelper, Exception e) {
		this(parent, plannerHelper, e.getStackTrace().toString());
	}
}
