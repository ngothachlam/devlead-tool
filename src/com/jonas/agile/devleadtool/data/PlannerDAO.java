package com.jonas.agile.devleadtool.data;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;

public interface PlannerDAO {

	public BoardTableModel getModel();
	public void saveModel(BoardTableModel model) throws IOException;
	public BoardTableModel loadModel() throws IOException;

}
