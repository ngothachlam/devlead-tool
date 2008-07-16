package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import com.jonas.agile.devleadtool.data.SystemProperties;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;

public class ClosingDialog extends JDialog {
	public ClosingDialog(JFrame parent) {
		super(parent, "Closing ...", true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		MyPanel panel = SwingUtil.getBorderPanel().bordered(15, 15, 15, 15);
		panel.addCenter(new JLabel("Closing all connections ... ", JLabel.CENTER));
		setContentPane(panel);
		pack();
		SwingUtil.centreWindowWithinWindow(this, parent);

		new Thread(new Runnable() {
			public void run() {
				SystemProperties.close();
				System.exit(0);
			}
		}).start();

		setVisible(true);
	}
}
