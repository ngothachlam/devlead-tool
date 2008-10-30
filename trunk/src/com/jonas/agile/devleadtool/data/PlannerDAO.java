package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;

public interface PlannerDAO {

   public MyTableModel loadBoardModel() throws IOException;

   public void saveBoardModel(MyTableModel modelToSaveFrom) throws IOException;
   public void saveJiraModel(MyTableModel planModel) throws IOException;

   public void removeListener(DaoListener daoListener);
   public void setXlsFile(File selFile);
   public void addListener(DaoListener daoListener);

   public void notifySavingStarted();
   public void notifySavingFinished();

}
