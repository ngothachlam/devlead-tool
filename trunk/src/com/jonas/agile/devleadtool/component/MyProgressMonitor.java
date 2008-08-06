package com.jonas.agile.devleadtool.component;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jonas.common.SwingWorker;
import com.jonas.testHelpers.TryoutTester;

public class MyProgressMonitor {

	private ProgressMonitor pbar;
	private int counter = 0;
	private int max;

	public MyProgressMonitor(JFrame frame, int max) {
		this.max = max;
		pbar = new ProgressMonitor(frame, "Monitoring Progress", "Initializing . . .", 0, max);
		pbar.setMillisToDecideToPopup(100);
		pbar.setMillisToPopup(10);
	}

	public static void main(String args[]) {
		UIManager.put("ProgressMonitor.progressText", "This is progress?");
		UIManager.put("OptionPane.cancelButtonText", "Go Away");
		final JFrame frame = TryoutTester.getFrame();

		// Thread t = new Thread(new Runnable(){
		// public void run() {
		// for (int i = 0; i <= 10; i++) {
		// monitor.increaseProgress();
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// });
		// t.start();
		frame.setVisible(true);

		SwingWorker worker = new SwingWorker() {
			public Object construct() {
				final int j = 5;
				final MyProgressMonitor monitor = new MyProgressMonitor(frame, j);
				for (int i = 0; i <= j-1; i++) {
					monitor.increaseProgress();
					try {
						System.out.println("sleeping...");
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return "result";
			}
		};
		worker.start();

		System.out.println("worker returns: \"" + worker.get() + "\"");
	}

	public void increaseProgress() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pbar.setProgress(counter);
				String string = "Operation is " + ((counter * 100) / max) + "% complete";
				System.out.println(string);
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