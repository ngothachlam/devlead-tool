package com.jonas.agile.devleadtool.component.panel;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class LoadDialog extends JFileChooser {

	private final PlannerDAO dao;

	private final DesktopPane desktopPane;

	public LoadDialog(DesktopPane desktop, PlannerDAO plannerDAO, JFrame frame, PlannerHelper plannerHelper) {
		super(new File("."));
		this.desktopPane = desktop;
		this.dao = plannerDAO;

		showOpenDialog(frame);
		File selFile = getSelectedFile();

		try {
			BoardTableModel model = dao.loadBoardModel(selFile);
			desktopPane.addInternalFrame(new InternalFrame(plannerHelper, model));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
