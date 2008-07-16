package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.jonas.agile.devleadtool.component.dialog.ClosingDialog;

	public final class MainFrameListener implements WindowListener {
		private JFrame frame;

		public MainFrameListener(JFrame frame) {
			super();
			this.frame = frame;
		}

		public void windowClosing(WindowEvent e) {
			new ClosingDialog(frame);				
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowOpened(WindowEvent e) {
		}
	}

