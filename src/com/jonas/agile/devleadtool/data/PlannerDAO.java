package com.jonas.agile.devleadtool.data;

import java.io.IOException;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;

public interface PlannerDAO {

	public void saveBoardModel(MyTableModel modelToSaveFrom) throws IOException;
	public void savePlanModel(MyTableModel planModel) throws IOException;
	public MyTableModel loadBoardModel() throws IOException;
	public PlanTableModel loadPlanModel() throws IOException;

}
