package com.jonas.agile.devleadtool.component.panel;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class SaveDialog extends JFileChooser {

	public SaveDialog(PlannerDAO dao, Component frame, PlannerHelper plannerHelper) {
		super(new File("."));

		showSaveDialog(frame);
		File selFile = getSelectedFile();

		plannerHelper.saveModels(selFile, dao);
	}

}
