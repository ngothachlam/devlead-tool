package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public interface PlannerDAO {

	public void saveBoardModel(File file, MyTableModel modelToSaveFrom) throws IOException;
	public BoardTableModel loadBoardModel(File xlsFile) throws IOException;
	public void savePlanModel(File file, MyTableModel planModel) throws IOException;
	public void saveJiraModel(File file, MyTableModel jiraModel) throws IOException;

}
