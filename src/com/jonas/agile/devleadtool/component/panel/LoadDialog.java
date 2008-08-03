package com.jonas.agile.devleadtool.component.panel;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class LoadDialog extends JFileChooser {

	private final PlannerDAO dao;

	private final DesktopPane desktopPane;

	public LoadDialog(DesktopPane desktop, PlannerDAO plannerDAO, JFrame frame, PlannerHelper plannerHelper) {
		super(new File("."));
		this.desktopPane = desktop;
		this.dao = plannerDAO;

		// File file = plannerHelper.getFile();
		// if (file != null)
		// setSelectedFile(file);

		int result = showOpenDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
			File xlsFile = getSelectedFile();

			try {
				BoardTableModel boardModel = dao.loadBoardModel(xlsFile);
				PlanTableModel planModel = dao.loadPlanModel(xlsFile);
				JiraTableModel jiraModel = dao.loadJiraModel(xlsFile);
				InternalFrame internalFrame = new InternalFrame(plannerHelper, boardModel, planModel, jiraModel);
				desktopPane.addInternalFrame(internalFrame);
				internalFrame.setExcelFile(xlsFile.getAbsolutePath());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
