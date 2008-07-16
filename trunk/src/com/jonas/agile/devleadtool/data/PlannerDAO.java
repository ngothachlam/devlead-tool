package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.component.table.BoardTableModel;

public interface PlannerDAO {

	public BoardTableModel getModel();
	public void saveModel(BoardTableModel model);

}
