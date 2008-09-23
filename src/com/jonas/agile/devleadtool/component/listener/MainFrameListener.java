package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jonas.agile.devleadtool.component.dialog.ClosePlannerDialog;

	public final class MainFrameListener extends WindowAdapter {
		private JFrame frame;

		public MainFrameListener(JFrame frame) {
			super();
			this.frame = frame;
		}

		public void windowClosing(WindowEvent e) {
			new ClosePlannerDialog(frame);				
		}
	}

