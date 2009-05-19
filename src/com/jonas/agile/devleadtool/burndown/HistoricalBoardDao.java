package com.jonas.agile.devleadtool.burndown;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.gui.component.table.ColumnWrapper;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class HistoricalBoardDao {
   private static final Vector<String> HISTORICALPRECOLUMNS = new Vector<String>();

   static {
      HISTORICALPRECOLUMNS.add("HistoricalDate");
      HISTORICALPRECOLUMNS.add("DayInSprint");
   }

   private Logger log = MyLogger.getLogger(HistoricalBoardDao.class);

   private DateHelper dateHelper;

   public HistoricalBoardDao(DateHelper dateHelper) {
      super();
      this.dateHelper = dateHelper;
   }

   public void save(File file, MyTableModel boardModel, int dayOfSprint) throws IOException {
      FileWriter writer = null;
      BufferedWriter bw = null;
      try {
         writer = new FileWriter(file);
         bw = new BufferedWriter(writer);

         writeHeader(boardModel, bw);
         writeBody(boardModel, bw, dayOfSprint);

      } finally {
         if (bw != null) {
            bw.close();
         }
         if (writer != null) {
            writer.close();
         }
      }
   }

   private void writeHeader(MyTableModel boardModel, BufferedWriter bw) throws IOException {
      StringBuffer sb = new StringBuffer();
      for (String preColumn : HISTORICALPRECOLUMNS) {
         sb.append(preColumn).append(",");
      }

      for (int column = 0; column < boardModel.getColumnCount(); column++) {
         sb.append(column != 0 ? "," : "").append(boardModel.getColumnName(column));
      }
      sb.append("\n");
      bw.append(sb.toString());
   }

   private void writeBody(MyTableModel boardModel, BufferedWriter bw, int dayOfSprint) throws IOException {
      String today = dateHelper.getTodaysDateAsString();
      for (int row = 0; row < boardModel.getRowCount(); row++) {
         StringBuffer sb = new StringBuffer(today).append(",").append(dayOfSprint);
         for (int column = 0; column < boardModel.getColumnCount(); column++) {
            Object value = boardModel.getValueAt(row, column);
            sb.append(",").append(value.toString());
         }
         sb.append("\n");
         bw.append(sb.toString());
      }
   }

   public HistoricalData load(File file) throws IOException {
      FileReader reader = null;
      BufferedReader br = null;
      try {
         reader = new FileReader(file);
         br = new BufferedReader(reader);

         Vector<String> header = readHeader(br);
         Vector<Vector<Object>> body = readBody(br, header);

         return new HistoricalData(header, body);
      } finally {
         if (br != null) {
            br.close();
         }
         if (reader != null) {
            reader.close();
         }
      }
   }

   private Vector<String> readHeader(BufferedReader br) throws IOException {
      String header = br.readLine();
      StringTokenizer tokenizer = new StringTokenizer(header, ",");
      Vector<String> cols = new Vector<String>();
      for (int counter = 0; tokenizer.hasMoreElements(); counter++) {
         String headerValue = tokenizer.nextToken();
         if (HISTORICALPRECOLUMNS.contains(headerValue)) {
            cols.add(headerValue);
         } else {
            ColumnWrapper e = ColumnWrapper.get(headerValue);
            cols.add(e.getType().toString());
         }
      }
      return cols;
   }

   private Vector<Vector<Object>> readBody(BufferedReader br, Vector<String> cols) throws IOException {
      String body = br.readLine();
      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      while (body != null) {
         StringTokenizer tokenizer = new StringTokenizer(body, ",");
         Vector<Object> dataRow = new Vector<Object>();
         for (int counter = 0; tokenizer.hasMoreElements(); counter++) {
            String columnTypeName = cols.get(counter);
            String bodyValue = tokenizer.nextToken();
            if (HISTORICALPRECOLUMNS.contains(columnTypeName)) {
               dataRow.add(bodyValue);
            } else {
               ColumnWrapper columnWrapper = ColumnWrapper.get(columnTypeName);
               Object parsedFromFile = columnWrapper.parseFromPersistanceStore(bodyValue);
               dataRow.add(parsedFromFile);
            }
         }
         data.add(dataRow);
         body = br.readLine();
      }
      return data;
   }

}
