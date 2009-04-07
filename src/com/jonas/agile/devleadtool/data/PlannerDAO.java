package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import com.jonas.agile.devleadtool.gui.component.dialog.CombinedModelDTO;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.listener.DaoListener;

public interface PlannerDAO {

   public CombinedModelDTO loadModels() throws IOException, PersistanceException;
   public void saveModels(MyTableModel boardModel, MyTableModel jiraModel) throws IOException;
   
   public void setXlsFile(File selFile);
   public void addListener(DaoListener daoListener);
   public void removeListener(DaoListener daoListener);
}
