package com.jonas.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

public class SwingUtil {
	public static void centreWindow(Window window) {
		Toolkit toolkit = window.getToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		window.setLocation((screenSize.width - window.getWidth()) / 2, (screenSize.height - window.getHeight()) / 2);
	}

	public static void centreWindowWithinWindow(Window window, Window parentWindow) {
		Point parentLocation = parentWindow.getLocation();
		window.setLocation(parentLocation.x + (parentWindow.getWidth() - window.getWidth()) / 2, parentLocation.y
				+ (parentWindow.getHeight() - window.getHeight()) / 2);
	}

	public static MyPanel getBorderPanel() {
		return new MyPanel(new BorderLayout());
	}

	public static MyPanel getGridPanel(int rows, int cols, int hgap, int vgap) {
		return new MyPanel(new GridLayout(rows, cols, hgap, vgap));
	}
}
