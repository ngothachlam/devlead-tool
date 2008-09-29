package com.jonas.agile.devleadtool.component.dialog;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.CutoverLength;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.panel.InternalFrameTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class LoadPlannerDialog extends JFileChooser {

	private final PlannerDAOExcelImpl dao;

	private final DesktopPane desktopPane;

	public LoadPlannerDialog(DesktopPane desktop, PlannerDAOExcelImpl plannerDAO, JFrame frame, PlannerHelper plannerHelper) {
		super(new File("."));
		this.desktopPane = desktop;
		this.dao = plannerDAO;

		addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (getTypeDescription(f).equalsIgnoreCase("Microsoft Excel Worksheet") || f.isDirectory())
					return true;
				return false;
			}

			public String getDescription() {
				return "XLS files";
			}
		});
		int result = showOpenDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
			File xlsFile = getSelectedFile();

			try {
			   dao.setXlsFile(xlsFile);
				BoardTableModel boardModel = dao.loadBoardModel();
				PlanTableModel planModel = dao.loadPlanModel();
				JiraTableModel jiraModel = dao.loadJiraModel();
				// TODO remove plannerHelper.getTitle?
				InternalFrameTabPanel internalFrameTabPanel = new InternalFrameTabPanel(plannerHelper, boardModel, planModel, jiraModel);
            InternalFrame internalFrame = new InternalFrame(plannerHelper, plannerHelper.getTitle(), internalFrameTabPanel, dao);
				desktopPane.addInternalFrame(internalFrame);
				internalFrame.setExcelFile(xlsFile.getAbsolutePath(), CutoverLength.DEFAULT);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
