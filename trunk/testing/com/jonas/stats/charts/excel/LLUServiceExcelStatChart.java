package com.jonas.stats.charts.excel;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JPanel;
import org.apache.commons.httpclient.HttpException;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import com.jonas.agile.devleadtool.burndown.ContentsDto;
import com.jonas.common.swing.SwingUtil;
import com.jonas.stats.charts.common.CommonTimeDenominatorStyle;
import com.jonas.stats.charts.common.DateRetriever;
import com.jonas.stats.charts.common.GraphPanelBuilder;
import com.jonas.stats.charts.common.GroupingDTO;
import com.jonas.stats.charts.common.LowestCommonDenominatorRegularTime;
import com.jonas.stats.charts.common.PointsInTimeFacade;
import com.jonas.stats.charts.common.PointsInTimeFacadeAbstract;

public class LLUServiceExcelStatChart extends ApplicationFrame {

   // private static final String LiveServicesInInventory = "Live Services (IMS)";
   private static final String Builds = "FENS Builds";
   private static final String Deletes = "FENS Deletes";
   private int dateCol = 1;
   private int startRowToParse = 3;

   // excel formula = TEXT(A2,"yyyy-MM-dd hh")
   public LLUServiceExcelStatChart(String title) {
      super(title);

      CommonTimeDenominatorStyle style = CommonTimeDenominatorStyle.day;
      boolean aggregate = false;
      String chartTitle = "LLU SMPF Inventory Stats";
      String yTitle = "Services/Requests";
      File excelFile = new File(
            "C:\\Documents and Settings\\jonasjolofsson\\My Documents\\01Work\\llu-docs\\LLU Development Support\\LLU Daily Stats.xls");
      String sheetName = "Stats";

      try {
         ExcelDao dao = new ExcelDao();

         ContentsDto fileContentsDto = dao.loadContents(excelFile, sheetName);

         DateRetriever<String> timeRetriever = null;
         switch (style) {
         case day:
            timeRetriever = new DayFromExcelDataRetriever();
            break;
         case hour:
            timeRetriever = new HourFromExcelDataRetriever();
            break;
         default:
            throw new RuntimeException("Need to implement another DateRetriever");
         }

         PointsInTimeFacadeAbstract<String, RegularTimePeriod> data = getData(fileContentsDto.getBody(), timeRetriever, style);

         JPanel chartPanel = createChartPanel(data, aggregate, chartTitle, yTitle);
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

   private PointsInTimeFacadeAbstract<String, RegularTimePeriod> getData(Vector<Vector<Object>> data, DateRetriever<String> dateRetriever,
         CommonTimeDenominatorStyle style) {
      PointsInTimeFacade<String, RegularTimePeriod> dataSetAggregator = new PointsInTimeFacade<String, RegularTimePeriod>();
      startRowToParse = startRowToParse - 2;
      for (Vector<Object> rowOfData : data) {
         if (startRowToParse-- > 0) {
            continue;
         }
         System.out.println("Col 0: " + rowOfData.get(0).toString());
         if (rowOfData.get(0).toString() == "") {
            continue;
         }
         String aRequest = rowOfData.get(dateCol).toString();
         RegularTimePeriod timeRetriever = dateRetriever.retrieveTimeLinePointFromObject(aRequest);
         LowestCommonDenominatorRegularTime denominator = new LowestCommonDenominatorRegularTime(timeRetriever, style);

         String services = rowOfData.get(2).toString();
         String builds = rowOfData.get(4).toString();
         String deletes = rowOfData.get(5).toString();

         // dataSetAggregator.addPointInTimeWithValue(LiveServicesInInventory, denominator, Double.parseDouble(services));
         dataSetAggregator.addPointInTimeWithValue(Builds, denominator, Double.parseDouble(builds));
         dataSetAggregator.addPointInTimeWithValue(Deletes, denominator, Double.parseDouble(deletes));
      }
      return dataSetAggregator;
   }

   public JPanel createChartPanel(PointsInTimeFacadeAbstract<String, ? extends RegularTimePeriod> dataSetAggregator, boolean aggregate,
         String chartTitle, String yTitle) {
      GroupingDTO<String>[] groupings = new GroupingDTO[] {
      // new GroupingDTO<String>(LiveServicesInInventory, 0, SwingUtil.cellBlue)
            new GroupingDTO<String>(Builds, SwingUtil.cellGreen),
            new GroupingDTO<String>(Deletes, SwingUtil.cellRed) };
      GraphPanelBuilder<String> panelBuilder = new GraphPanelBuilder<String>(aggregate, dataSetAggregator, groupings);
      return panelBuilder.createDatasetAndChartFromTimeAggregator(chartTitle, yTitle);
   }

   /**
    * Starting point for the demonstration application.
    * 
    * @param args
    *           ignored.
    */
   public static void main(String[] args) {
      LLUServiceExcelStatChart demo = new LLUServiceExcelStatChart("ExcelStat Chart Tester");
      demo.setVisible(true);
   }

}

class DayFromExcelDataRetriever implements DateRetriever<String> {
   @Override
   public Day retrieveTimeLinePointFromObject(String cell) {
      System.out.println("Trying to parse to day: " + cell);
      String string = cell.toString();
      Day day = Day.parseDay(string);
      System.out.println(" ... and it became " + day);
      return day;
   }
}
