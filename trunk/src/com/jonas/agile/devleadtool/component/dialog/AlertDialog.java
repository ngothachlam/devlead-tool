package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;
import com.jonas.testHelpers.TryoutTester;

public class AlertDialog extends JDialog {
	private JTextArea textArea;

	private AlertDialog(JFrame parent, String alertMessage) {
		super(parent, "Alert ...", true);

		MyPanel panel = new MyPanel(new BorderLayout()).bordered(15, 15, 15, 15);
		textArea = new JTextArea(alertMessage);
		textArea.setEditable(false);
		panel.addNorth(new JLabel("Message:"));
		JScrollPane scrollPane = new JScrollPane(textArea);
		// scrollPane.setMaximumSize(new Dimension(300,300));
		panel.addCenter(scrollPane);
		JButton button = new JButton("Close");
		panel.addSouth(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		setContentPane(panel);
		pack();
		setSize(new Dimension(700, 350));
		SwingUtil.centreWindowWithinWindow(this, parent);
		setVisible(true);
	}

	public static void alertException(JFrame parentFrame, Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stacktrace = sw.toString();
		AlertDialog alertDialog = new AlertDialog(parentFrame, stacktrace);
	}

	public static void main(String[] args) {
		JFrame frame = TryoutTester.getFrame();
		frame.setLocation(100, 100);
		frame.setVisible(true);
		AlertDialog.alertException(frame, TryoutTester.getTestException());
	}

	public static void alertException(PlannerHelper helper, Exception e) {
		alertException(helper.getParentFrame(), e);
	}

}
