package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class PlannerHelper {

	private String title;
	private MyTableModel model;

	public PlannerHelper(String title) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public MyTableModel getBoardModel() {
		return model;
	}

	public void setBoardModel(MyTableModel model) {
		this.model = model;
	}

}
