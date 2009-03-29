package com.jonas.agile.devleadtool.gui.component;

import java.awt.Color;
import javax.swing.JDesktopPane;

public class DesktopPane extends JDesktopPane {

	public DesktopPane() {
		super();
		setBackground(new Color(188, 218, 241));
	}

	public void addInternalFrame(MyInternalFrame internalFrame) {
		add(internalFrame);
		internalFrame.setSize(1050, 700);
		int xLocation = (internalFrame.getInternalFramesCount() - 1) * 20;
		int yLocation = (internalFrame.getInternalFramesCount() - 1) * 25;

		this.putClientProperty("JDesktopPane.dragMode", "outline");

		internalFrame.setLocation(xLocation, yLocation);
		internalFrame.moveToFront();
		internalFrame.setVisible(true);
		
//		scrollpane.setViewportView(this);

	}
}
