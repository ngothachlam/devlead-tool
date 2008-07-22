package com.jonas.agile.devleadtool.component.panel;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class LoadDialog extends JFileChooser {

	private final PlannerDAO dao;

	public LoadDialog(PlannerDAO plannerDAO, JFrame frame, PlannerHelper plannerHelper) {
		super(new File("."));
		this.dao = plannerDAO;

		showOpenDialog(frame);
		File selFile = getSelectedFile();
		
		try {
			MyTableModel model = dao.loadModel(selFile);
			// TODO load to internal frame;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
