package com.jonas.stats.charts.excel;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.commons.httpclient.HttpException;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.jonas.agile.devleadtool.burndown.ContentsDto;
import com.jonas.stats.charts.common.ChartStatPanelBuilder;
import com.jonas.stats.charts.common.CommonTimeDenominatorStyle;
import com.jonas.stats.charts.common.DateRetriever;
import com.jonas.stats.charts.common.LowestCommonDenominatorRegularTime;
import com.jonas.stats.charts.common.PointsInTimeFacade;
import com.jonas.stats.charts.common.PointsInTimeFacadeAbstract;

public class ExcelStatChart extends ApplicationFrame {

   //excel formula = TEXT(A2,"yyyy-MM-dd hh")
   public ExcelStatChart(String title) {
      super(title);

      CommonTimeDenominatorStyle style = CommonTimeDenominatorStyle.hour;
      boolean aggregate = false;
      File excelFile = new File("C:\\Documents and Settings\\jonasjolofsson\\My Documents\\avail_mlc_requests.xls");
      String sheetName = "Results 1";
      int columnInExcelFile = 1;

      try {
         ExcelDao dao = new ExcelDao();

         ContentsDto fileContentsDto = dao.loadContents(excelFile, sheetName);

         DateRetriever<String> timeRetriever = null;
         switch (style) {
            case hour:
               timeRetriever = new DateFromExcelDataRetriever();
               break;
            default:
               throw new RuntimeException("Need to implement another DateRetriever");
         }

         PointsInTimeFacadeAbstract<StatChartCategory, RegularTimePeriod> data = getData(fileContentsDto.getBody(), timeRetriever, style, columnInExcelFile);

         JPanel chartPanel = createChartPanel(data, aggregate);
         chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

         setContentPane(chartPanel);
         pack();
         RefineryUtilities.centerFrameOnScreen(this);
      } catch (HttpException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private PointsInTimeFacadeAbstract<StatChartCategory, RegularTimePeriod> getData(Vector<Vector<Object>> data, DateRetriever<String> dateRetriever, CommonTimeDenominatorStyle style, int columnInExcelFile) {
      PointsInTimeFacade<StatChartCategory, RegularTimePeriod> dataSetAggregator = new PointsInTimeFacade<StatChartCategory, RegularTimePeriod>();
      for (Vector<Object> rowOfData : data) {
         String object = rowOfData.get(columnInExcelFile).toString();
         RegularTimePeriod timeRetriever = dateRetriever.retrieveTimeLinePointFromObject(object);
         LowestCommonDenominatorRegularTime denominator = new LowestCommonDenominatorRegularTime(timeRetriever, style);
         dataSetAggregator.addPointInTimeWithValue(StatChartCategory.REQUESTS, denominator, 1);
      }
      return dataSetAggregator;
   }

   public JPanel createChartPanel(PointsInTimeFacadeAbstract<StatChartCategory, ? extends RegularTimePeriod> dataSetAggregator, boolean aggregate) {
      ChartStatPanelBuilder panelBuilder = new ExcelStatPanelBuilder(aggregate, dataSetAggregator);
      return panelBuilder.createDatasetAndChartFromTimeAggregator();
   }

   /**
    * Starting point for the demonstration application.
    * 
    * @param args
    *           ignored.
    */
   public static void main(String[] args) {
      ExcelStatChart demo = new ExcelStatChart("ExcelStat Chart Tester");
      demo.setVisible(true);
   }

}

class DateFromExcelDataRetriever implements DateRetriever<String> {
   @Override
   public Hour retrieveTimeLinePointFromObject(String cell) {
      String string = cell.toString();

      Hour hour = Hour.parseHour(string);
      System.out.println("parsing to jfreechart day: " + string + " which became " + hour);
      return hour;
   }
}
