package com.jonas.agile.devleadtool.component.panel;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class SaveDialog extends JFileChooser {

	PlannerDAO dao;
	
	public SaveDialog(PlannerDAO dao, Component frame, PlannerHelper plannerHelper) {
		super(new File("."));
		this.dao = dao;
		
		// TODO : Ensure only saved planner is being saved if several open!!

		showSaveDialog(frame);
		File selFile = getSelectedFile();

		try {
			dao.saveModel(selFile, plannerHelper.getBoardModel());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
