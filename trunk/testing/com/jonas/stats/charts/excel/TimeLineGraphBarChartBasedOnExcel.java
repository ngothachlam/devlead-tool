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
import com.jonas.stats.charts.common.DateRetriever;
import com.jonas.stats.charts.common.PointsInTimeFacade;
import com.jonas.stats.charts.common.PointsInTimeFacadeAbstract;

public class TimeLineGraphBarChartBasedOnExcel extends ApplicationFrame {
   ExcelDao dao = new ExcelDao();
   private final File file;

   public static void main(String[] args) throws IOException {
      File nfile = new File("test-data\\aqua 2.xls");
      String sheetName = "Results 1";
      
      TimeLineGraphBarChartBasedOnExcel demo = new TimeLineGraphBarChartBasedOnExcel("ExcelFile stat Tester", nfile, sheetName);
      demo.setVisible(true);
   }

   public TimeLineGraphBarChartBasedOnExcel(String title, File xlsFile, String sheetName) {
      super(title);
      this.file = xlsFile;

      boolean aggregate = false;

      try {
         DateRetriever<Object> dateRetriever = new DateFromExcelDataRetriever();
         ContentsDto data = dao.loadContents(xlsFile, sheetName);
         PointsInTimeFacadeAbstract<String, Hour> datas = getData(data, dateRetriever);

         JPanel chartPanel = createChartPanel(datas, aggregate);
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

   private PointsInTimeFacadeAbstract<String, Hour> getData(ContentsDto jiras, DateRetriever<Object> dateRetriever) {
      PointsInTimeFacade<String, Hour> dataSetAggregator = new PointsInTimeFacade<String, Hour>();
      for (Vector<Object> row : jiras.getBody()) {
         RegularTimePeriod retrievedTime = dateRetriever.retrieveTimeLinePointFromObject(row.get(2));
         dataSetAggregator.addPointInTime("Requests", new TimeWithHourAsLeastLowestCommonDenominatorDTO(retrievedTime));
      }
      return dataSetAggregator;
   }
   
   public JPanel createChartPanel(PointsInTimeFacadeAbstract<?, ? extends RegularTimePeriod> dataSetAggregator, boolean aggregate) {
      ExcelStatPanelBuilder panelBuilder = new ExcelStatPanelBuilder(false, dataSetAggregator);
      return panelBuilder.createDatasetAndChartFromTimeAggregator();
   }
}


