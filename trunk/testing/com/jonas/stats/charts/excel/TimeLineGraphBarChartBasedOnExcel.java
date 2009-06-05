package com.jonas.stats.charts.excel;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import com.jonas.agile.devleadtool.burndown.BurnData;
import com.jonas.agile.devleadtool.burndown.BurnDataRetriever;
import com.jonas.agile.devleadtool.burndown.BurnType;
import com.jonas.agile.devleadtool.burndown.Category;
import com.jonas.agile.devleadtool.burndown.ContentsDto;
import com.jonas.agile.devleadtool.burndown.ManualBurnFrame;
import com.jonas.common.DateHelper;

public class TimeLineGraphBarChartBasedOnExcel implements BurnDataRetriever {
//   DateHelper dateHelper = new DateHelper();
   ExcelDao dao = new ExcelDao();
   private BurnData data;
   private final File file;

   public static void main(String[] args) throws IOException {
      File file = new File("test-data\\aqua 2.xls");
      TimeLineGraphBarChartBasedOnExcel excel = new TimeLineGraphBarChartBasedOnExcel(file);
      ManualBurnFrame boardStatsFrame = new ManualBurnFrame(null, new DateHelper(), excel);
      excel.calculateBurndownData();
      boardStatsFrame.updateBurndown();
      boardStatsFrame.setVisible(true);
   }

   public TimeLineGraphBarChartBasedOnExcel(File file) {
      this.file = file;
   }

   private double getDouble(Object string) {
      double parseDouble;
      try {
         parseDouble = Double.parseDouble(string.toString());
      } catch (NumberFormatException e) {
         return 0d;
      }
      return parseDouble;
   }

   public BurnData getSortedDataUsingCriteria(ContentsDto data) {
      Vector<Vector<Object>> dataBody = data.getBody();

      Integer length = 10;
      BurnData burnData = new BurnData(BurnType.BurnUp, length, "Estimated Points");

      for (Vector<Object> bodyRow : dataBody) {

         Category category = new Category(bodyRow.get(2).toString(), Color.black, 0, true);
         double dayInSprint = 1d;
         double value = 1d;
         
         burnData.add(category, dayInSprint, value);
      }

      return burnData;
   }

   @Override
   public void calculateBurndownData() {
      ContentsDto contents;
      try {
         contents = dao.loadContents(file, "Results 1");
         data = getSortedDataUsingCriteria(contents);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   public BurnData getBurnData() {
      return data;
   }

}
