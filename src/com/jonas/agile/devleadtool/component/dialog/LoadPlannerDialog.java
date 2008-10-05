package com.jonas.agile.devleadtool.component.dialog;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.CutoverLength;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class LoadPlannerDialog extends JFileChooser {

	private final PlannerDAOExcelImpl dao;

	public LoadPlannerDialog(MyDesktopPane desktop, PlannerDAOExcelImpl plannerDAO, JFrame frame, PlannerHelper plannerHelper) {
		super(new File("."));
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
				InternalTabPanel internalFrameTabPanel = new InternalTabPanel(plannerHelper, boardModel, planModel, jiraModel);
            MyInternalFrame internalFrame = new MyInternalFrame(plannerHelper, plannerHelper.getTitle(), internalFrameTabPanel, dao);
            desktop.addInternalFrame(internalFrame);
				internalFrame.setExcelFile(xlsFile.getAbsolutePath(), CutoverLength.DEFAULT);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
