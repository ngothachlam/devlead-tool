package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;

public class PlannerCommunicator {

	private final PlannerHelper plannerHelper;

	public PlannerCommunicator(PlannerHelper plannerHelper) {
		this.plannerHelper = plannerHelper;
	}

	public BoardStatus getJiraStatusFromBoard(String jira) {
		InternalFrame activeFrame = plannerHelper.getActiveInternalFrame();
		return ((BoardTableModel)activeFrame.getBoardModel()).getStatus(jira);
	}
	
}
