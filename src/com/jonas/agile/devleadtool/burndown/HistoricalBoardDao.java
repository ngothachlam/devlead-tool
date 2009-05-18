package com.jonas.agile.devleadtool.burndown;

import java.io.BufferedWriter;
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
      BufferedWriter bw = null;
      try {
         writer = new FileWriter(file);
         bw = new BufferedWriter(writer);
         
         writeHeader(boardModel, bw);
         writeBody(boardModel, bw);
         
      } finally {
         if (bw != null) {
            bw.close();
         }
         if (writer != null) {
            writer.close();
         }
      }
   }

   private void writeBody(MyTableModel boardModel, BufferedWriter bw) throws IOException {
      String today = dateHelper.getTodaysDateAsString();
      for (int row = 0; row < boardModel.getRowCount(); row++) {
         StringBuffer sb = new StringBuffer(today);
         for (int column = 0; column < boardModel.getColumnCount(); column++) {
            Object value = boardModel.getValueAt(row, column);
            sb.append(",").append(value.toString());
         }
         sb.append("\n");
         bw.append(sb.toString());
      }
   }

   private void writeHeader(MyTableModel boardModel, BufferedWriter bw) throws IOException {
      StringBuffer sb = new StringBuffer("HistoricalDate");
      for (int column = 0; column < boardModel.getColumnCount(); column++) {
         sb.append(",").append(boardModel.getColumnName(column));
      }
      sb.append("\n");
      bw.append(sb.toString());
   }
}
