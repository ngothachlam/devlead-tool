package com.jonas.agile.devleadtool.burndown;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class HistoricalBoardDao {
   private Logger log = MyLogger.getLogger(HistoricalBoardDao.class);

   private DateHelper dateHelper;

   public HistoricalBoardDao(DateHelper dateHelper) {
      super();
      this.dateHelper = dateHelper;
   }

   public void save(MyTableModel boardModel) {
      String today = dateHelper.getTodaysDateAsString();
      StringBuffer sb = new StringBuffer("HistoricalDate");
      for (int column = 0; column < boardModel.getColumnCount(); column++) {
         sb.append(",").append(boardModel.getColumnName(column));
      }
      System.out.println(sb.toString());
      
      for (int row = 0; row < boardModel.getRowCount(); row++) {
         sb = new StringBuffer(today);
         for (int column = 0; column < boardModel.getColumnCount(); column++) {
            Object value = boardModel.getValueAt(row, column);
            sb.append(",").append(value.toString());
         }
         System.out.println(sb.toString());
      }
   }
}
