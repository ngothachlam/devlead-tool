package com.jonas.testHelpers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import com.jonas.agile.devleadtool.component.frame.main.MainFrame;
import com.jonas.agile.devleadtool.data.SystemProperties;

public class TryoutTester {

	private static MainFrame frame;

	public static void viewPanel(JPanel panel) {
		frame = new MainFrame("Tryout Tester Frame");
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

	public static MainFrame getFrame() {
		frame = new MainFrame("Tryout Tester Frame");
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
		return getTestExceptionThatHasAVeryLongNameAndThatMightNotFitWellInAWindow(25);
	}

	private static Exception getTestExceptionThatHasAVeryLongNameAndThatMightNotFitWellInAWindow(int i) {
		if (i < 1)
			return new RuntimeException("testException");
		return getTestExceptionThatHasAVeryLongNameAndThatMightNotFitWellInAWindow(--i);
	}

}
