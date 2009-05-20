package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.burndown.HistoricalBoardDao;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.DateHelper;

public class SaveDataAction extends BasicAbstractGUIAction {

   private final MyTableModel boardModel;
   private HistoricalBoardDao historicalBoardDao;
   private PlannerHelper helper;

   public SaveDataAction(String name, String description, Frame parentFrame, MyTableModel boardModel, HistoricalBoardDao historicalBoardDao, PlannerHelper helper) {
      super(name, description, parentFrame);
      this.boardModel = boardModel;
      this.historicalBoardDao = historicalBoardDao;
      this.helper = helper;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      File file = getFileForHistoricalSave(helper.getSaveDirectory(), helper.getExcelFile());
      try {
         // FIXME 1- copy from BoardStatsFrame
         historicalBoardDao.save(file, boardModel, dayOfSprint, sprint);
      } catch (IOException e1) {
         e1.printStackTrace();
         throw new RuntimeException(e1);
      }
   }

   protected File getFileForHistoricalSave(File saveDirectory, File originalFile) {
      String orignalFileName = originalFile.getName();
      orignalFileName = orignalFileName.substring(0, orignalFileName.indexOf("."));
      return new File(saveDirectory, "HISTORICAL - " + orignalFileName + ".csv");
   }
}
