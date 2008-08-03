package com.jonas.testHelpers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jonas.agile.devleadtool.data.SystemProperties;

public class TryoutTester {

	private static JFrame frame;

	public static void viewPanel(JPanel panel) {
		frame = new JFrame();
		frame.setContentPane(panel);
		frame.setSize(800, 400);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				SystemProperties.close();
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}

	public static JFrame getFrame() {
		JFrame frame = new JFrame();
		JPanel contentPanel = new JPanel();
		frame.setContentPane(contentPanel);
		frame.setSize(800, 400);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				SystemProperties.close();
				System.exit(0);
			}
		});
		return frame;
	}

	public static Exception getTestException() {
		return new RuntimeException("testException");
	}

}
