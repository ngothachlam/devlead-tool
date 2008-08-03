package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;
import com.jonas.testHelpers.TryoutTester;

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

	public static void alertException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stacktrace = sw.toString();
		System.out.println(sw.toString());
		e.printStackTrace();
		AlertDialog alertDialog = new AlertDialog(TryoutTester.getFrame(), new PlannerHelper("test"), stacktrace);
	}
	
	// public static void AlertDialog(JFrame parent, PlannerHelper plannerHelper, Exception e) {
	// this(parent, plannerHelper, stacktrace);
	// }

	public static void main(String[] args) {
		AlertDialog.alertException(TryoutTester.getTestException());
	}

}
