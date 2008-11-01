package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public interface PlannerDAO {

   public BoardTableModel loadBoardModel() throws IOException;
   public JiraTableModel loadJiraModel() throws IOException;

   public void saveBoardModel(MyTableModel modelToSaveFrom) throws IOException;
   public void saveJiraModel(MyTableModel planModel) throws IOException;

   public void setXlsFile(File selFile);

   public void addListener(DaoListener daoListener);
   public void removeListener(DaoListener daoListener);
}
