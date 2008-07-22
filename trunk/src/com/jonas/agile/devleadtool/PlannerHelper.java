package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class PlannerHelper {

	private String title;

	private BoardTableModel model;

	public PlannerHelper(String title) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public BoardTableModel getBoardModel() {
		return model;
	}

	private void setBoardModel(BoardTableModel model) {
		this.model = model;
	}

	public void setPlanner(InternalFrame internalFrame) {
		System.out.println("focusing " + internalFrame.getTitle());
		this.setBoardModel(internalFrame.getBoardPanel().getBoardModel());
	}

}
