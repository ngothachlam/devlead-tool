package com.jonas.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JTable;

public class SwingUtil {
	
	public static final Color COLOR_NONSELECT_ERROR = new Color(200, 0, 0);
	public static final Color COLOR_SELECTION_ERROR = new Color(225, 0, 0);
	public static final Color COLOR_FOCUS_ERROR = new Color(250, 0, 0);

	
	private static Color selectionBackground = null;

	private static Object lock = new Object();

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

	public static Color getTableCellFocusBackground(JTable table) {
		if (selectionBackground == null) {
			synchronized (lock) {
				if (selectionBackground == null) {
					Color color = table.getSelectionBackground();
					selectionBackground = new Color(color.getRed() + 25, color.getGreen() + 25, color.getBlue() + 25);
				}
			}
		}
		return selectionBackground;
	}
}
