package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import com.jonas.agile.devleadtool.gui.component.dialog.CombinedModelDTO;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.listener.DaoListener;
import com.jonas.agile.devleadtool.sprint.SprintCache;

public interface PlannerDAO {

   public CombinedModelDTO loadAllData(File xlsFile) throws IOException, PersistanceException;

   public void saveAllData(File file, MyTableModel boardModel, MyTableModel jiraModel, SprintCache sprintCache) throws IOException;

   public void addListener(DaoListener daoListener);

   public void removeListener(DaoListener daoListener);
}
