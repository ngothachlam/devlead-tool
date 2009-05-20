package com.jonas.agile.devleadtool.burndown;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class HistoricalBoardDao {
   private static final String DELIMITER = "¬";

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

   public void save(File file, MyTableModel boardModel, int dayOfSprint, Sprint sprint) throws IOException {
      FileWriter writer = null;
      BufferedWriter bw = null;
      try {

         boolean fileExists = file.exists();

         HistoricalData data = null;
         if (fileExists) {
            data = load(file);
         }

         writer = new FileWriter(file);
         bw = new BufferedWriter(writer);

         writeHeaderIfRequired(boardModel, data, bw);
         writeBody(boardModel, data, bw, dayOfSprint, sprint);

      } finally {
         if (bw != null) {
            bw.close();
         }
         if (writer != null) {
            writer.close();
         }
      }
   }

   private void writeHeaderIfRequired(MyTableModel boardModel, HistoricalData data, BufferedWriter bw) throws IOException {
      String headerCSV = null;
      if (data == null || !data.hasHeader()) {
         headerCSV = getCSVHeaderString(boardModel);
      } else {
         headerCSV = data.getHeaderAsCSV(DELIMITER);
      }
      bw.append(headerCSV);
   }

   private String getCSVHeaderString(MyTableModel boardModel) {
      StringBuffer sb = new StringBuffer();
      for (String preColumn : HISTORICALPRECOLUMNS) {
         sb.append(preColumn).append(DELIMITER);
      }
      for (int column = 0; column < boardModel.getColumnCount(); column++) {
         sb.append(column != 0 ? DELIMITER : "").append(boardModel.getColumnName(column));
      }
      return sb.append("\n").toString();
   }

   private void writeBody(MyTableModel boardModel, HistoricalData data, BufferedWriter bw, int dayOfSprint, Sprint sprint) throws IOException {
      if (data != null) {
         Vector<Vector<Object>> oldDataToCopy = data.getBodyLinesThatAreNotForThisDayInSprint(sprint, dayOfSprint);
         for (Vector<Object> oldRow : oldDataToCopy) {
            StringBuffer sb = new StringBuffer(oldRow.get(0).toString());
            for (int counter = 1; counter < oldRow.size(); counter++) {
               sb.append(DELIMITER).append(oldRow.get(counter).toString());
            }
            bw.append(sb.append("\n").toString());
         }
      }

      String today = dateHelper.getTodaysDateAsString();
      for (int row = 0; row < boardModel.getRowCount(); row++) {
         String bodyLineCSV = getCSVBodyLineString(boardModel, dayOfSprint, today, row);
         bw.append(bodyLineCSV);
      }
   }

   private String getCSVBodyLineString(MyTableModel boardModel, int dayOfSprint, String today, int row) {
      StringBuffer sb = new StringBuffer(today).append(DELIMITER).append(dayOfSprint);
      for (int column = 0; column < boardModel.getColumnCount(); column++) {
         Object value = boardModel.getValueAt(row, column);
         sb.append(DELIMITER).append(value.toString());
      }
      return sb.append("\n").toString();
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
      String[] split = header.split(DELIMITER);
      Vector<String> cols = new Vector<String>();
      for (int counter = 0; counter < split.length; counter++) {
         String headerValue = split[counter];
         if (HISTORICALPRECOLUMNS.contains(headerValue)) {
            cols.add(headerValue);
         } else {
            // ColumnWrapper e = ColumnWrapper.get(headerValue);
            cols.add(headerValue);
         }
      }
      return cols;
   }

   private Vector<Vector<Object>> readBody(BufferedReader br, Vector<String> cols) throws IOException {
      String body = br.readLine();
      Vector<Vector<Object>> data = new Vector<Vector<Object>>();
      while (body != null) {
         String[] split = body.split(DELIMITER);
         Vector<Object> dataRow = new Vector<Object>();
         for (int counter = 0; counter < split.length; counter++) {
            String columnTypeName = cols.get(counter);
            String bodyValue = split[counter];

            if (HISTORICALPRECOLUMNS.contains(columnTypeName)) {
               dataRow.add(bodyValue);
            } else {
               // ColumnWrapper columnWrapper = ColumnWrapper.get(columnTypeName);
               // Object parsedFromFile = columnWrapper.parseFromPersistanceStore(bodyValue);
               dataRow.add(bodyValue);
            }
         }
         data.add(dataRow);
         body = br.readLine();
      }
      return data;
   }

   public void setDateHelper(DateHelper dateHelper) {
      this.dateHelper = dateHelper;
   }

}
