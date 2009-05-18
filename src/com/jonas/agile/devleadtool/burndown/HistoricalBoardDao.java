package com.jonas.agile.devleadtool.burndown;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

   public void save(File file, MyTableModel boardModel) throws IOException {
      FileWriter writer = null;
      try {
         writer = new FileWriter(file);
         String today = dateHelper.getTodaysDateAsString();
         StringBuffer sb = new StringBuffer("HistoricalDate");
         for (int column = 0; column < boardModel.getColumnCount(); column++) {
            sb.append(",").append(boardModel.getColumnName(column));
         }
         sb.append("\n");
         writer.write(sb.toString());

         for (int row = 0; row < boardModel.getRowCount(); row++) {
            sb = new StringBuffer(today);
            for (int column = 0; column < boardModel.getColumnCount(); column++) {
               Object value = boardModel.getValueAt(row, column);
               sb.append(",").append(value.toString());
            }
            sb.append("\n");
            writer.write(sb.toString());
         }
      } finally {
         if (writer != null) {
            writer.close();
         }
      }
   }
}
