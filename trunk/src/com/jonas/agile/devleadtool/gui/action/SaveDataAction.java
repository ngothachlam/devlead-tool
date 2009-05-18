package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import com.jonas.agile.devleadtool.burndown.HistoricalBoardDao;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class SaveDataAction extends BasicAbstractGUIAction {

   private final MyTableModel boardModel;
   private HistoricalBoardDao historicalBoardDao;

   public SaveDataAction(String name, String description, Frame parentFrame, MyTableModel boardModel, HistoricalBoardDao historicalBoardDao) {
      super(name, description, parentFrame);
      this.boardModel = boardModel;
      this.historicalBoardDao = historicalBoardDao;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      historicalBoardDao.save(boardModel);
   }

}
