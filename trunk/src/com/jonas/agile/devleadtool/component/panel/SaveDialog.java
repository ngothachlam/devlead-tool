package com.jonas.agile.devleadtool.component.panel;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class SaveDialog extends JFileChooser {

	public SaveDialog(PlannerDAO dao, Component frame, PlannerHelper plannerHelper) {
		super(new File("."));
		File file = plannerHelper.getFile();
		if (file != null)
			setSelectedFile(file);
		
		// addChoosableFileFilter(new FileFilter() {
		// public boolean accept(File f) {
		// if (getTypeDescription(f).equalsIgnoreCase("Microsoft Excel Worksheet") || f.isDirectory())
		// return true;
		// return false;
		// }
		//
		// public String getDescription() {
		// return "XLS files";
		// }
		// });

		int result = showSaveDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selFile = getSelectedFile();
			plannerHelper.setFile(selFile);
			plannerHelper.saveModels(selFile, dao);
		}
	}

}
