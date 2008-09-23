package com.jonas.agile.devleadtool.component.dialog;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class SavePlannerDialog extends JFileChooser {

	public SavePlannerDialog(PlannerDAOExcelImpl dao, Component frame, PlannerHelper plannerHelper) {
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
			dao.setXlsFile(selFile);
			plannerHelper.saveModels(dao);
		}
	}

}
