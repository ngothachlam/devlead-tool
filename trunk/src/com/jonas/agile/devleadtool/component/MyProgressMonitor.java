package com.jonas.agile.devleadtool.component;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class MyProgressMonitor {

	private ProgressMonitor pbar;
	private int counter = 0;
	private int max;
	private Logger log = MyLogger.getLogger(MyProgressMonitor.class);

	public MyProgressMonitor(JFrame frame, int max) {
		this.max = max;
		pbar = new ProgressMonitor(frame, "Monitoring Progress", "Initializing . . .", 0, max);
		pbar.setMillisToDecideToPopup(100);
		pbar.setMillisToPopup(10);
	}

	public void increaseProgress() {
		SwingUtilities.invokeLater(new Runnable() {

         public void run() {
				pbar.setProgress(counter);
				String string = "Operation is " + ((counter * 100) / max) + "% complete";
				log.debug(string);
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