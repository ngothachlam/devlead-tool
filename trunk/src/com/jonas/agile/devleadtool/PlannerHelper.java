package com.jonas.agile.devleadtool;

import java.io.File;
import java.io.IOException;

import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.panel.SaveDialog;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class PlannerHelper {

	private String title;

	private BoardTableModel model;

	private InternalFrame internalFrame;

	public PlannerHelper(String title) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setActiveInternalFrame(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}
	public InternalFrame getActiveInternalFrame() {
		return internalFrame;
	}

	public void saveModels(File selFile, PlannerDAO dao) {
		try {
			dao.saveBoardModel(selFile, this.getActiveInternalFrame().getBoardModel());
			dao.saveJiraModel(selFile, this.getActiveInternalFrame().getJiraModel());
			dao.savePlanModel(selFile, this.getActiveInternalFrame().getPlanModel());
			// TODO add save for plan and Jira
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
