package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public interface PlannerDAO {

	public void saveModel(File file, MyTableModel modelToSaveFrom) throws IOException;
	public BoardTableModel loadModel(File xlsFile) throws IOException;

}
