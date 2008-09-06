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

public class AlertDialog extends JDialog {
	private JTextArea textArea;

	private AlertDialog(JFrame parent, String alertMessage) {
		super(parent, "Alert ...", true);

		MyPanel panel = new MyPanel(new BorderLayout()).bordered(15, 15, 15, 15);
		textArea = new JTextArea(alertMessage);
		textArea.setEditable(false);
		JButton button = new JButton("Close");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		panel.addNorth(new JLabel("Message:"));
		panel.addCenter(new JScrollPane(textArea));
		panel.addSouth(button);

		setContentPane(panel);

		pack();
		setSize(new Dimension(700, 350));
		if (parent != null)
			SwingUtil.centreWindowWithinWindow(this, parent);
		else
			setLocationRelativeTo(null);

		setVisible(true);
	}

	static void alertException(JFrame parentFrame, Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stacktrace = sw.toString();
		alertException(parentFrame, stacktrace);
	}

	public static void alertException(PlannerHelper helper, Throwable e) {
		alertException(helper.getParentFrame(), e);
	}

	public static void alertException(PlannerHelper helper, String e) {
		alertException(helper.getParentFrame(), e);
	}

	private static void alertException(JFrame parentFrame, String e) {
		new AlertDialog(parentFrame, e);

	}

	public static AlertDialog message(PlannerHelper helper, String e) {
		return new AlertDialog(helper != null ? helper.getParentFrame() : null, e);
	}

	public void addText(String string) {
		textArea.setText(textArea.getText() + string);
	}

}
