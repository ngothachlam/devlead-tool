package com.jonas.agile.devleadtool.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.jonas.testHelpers.TryoutTester;

public class MyProgressMonitor {

	private ProgressMonitor pbar;
	private int counter = 0;
	private int max;

	public MyProgressMonitor(JFrame frame, int max) {
		this.max = max;
		pbar = new ProgressMonitor(frame, "Monitoring Progress", "Initializing . . .", 0, max);
		pbar.setMillisToDecideToPopup(0);
		pbar.setMillisToPopup(0);
		// Fire a timer every once in a while to update the progress.
//		Timer timer = new Timer(500, this);
//		timer.start();
	}

	public static void main(String args[]) {
		UIManager.put("ProgressMonitor.progressText", "This is progress?");
		UIManager.put("OptionPane.cancelButtonText", "Go Away");
		final JFrame frame = TryoutTester.getFrame();
		
		Thread t = new Thread(new Runnable(){
			public void run() {
				final MyProgressMonitor monitor = new MyProgressMonitor(frame, 10);
				for (int i = 0; i <= 10; i++) {
					monitor.increaseProgress();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
		
		frame.setVisible(true);
	}

	public void increaseProgress() {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				pbar.setProgress(counter);
				String string = "Operation is " + ((counter * 100)/ max) + "% complete";
				pbar.setNote(string);
				counter += 1;
			}
		});
	}

	class Update implements Runnable {
		public void run() {
			if (pbar.isCanceled()) {
				pbar.close();
				System.exit(1);
			}
			pbar.setProgress(counter);
			pbar.setNote("Operation is " + counter + "% complete");
			counter += 2;
		}
	}
}