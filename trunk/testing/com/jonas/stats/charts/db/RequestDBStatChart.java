package com.jonas.stats.charts.db;

import java.io.IOException;
import java.util.Vector;
import javax.swing.JPanel;
import org.apache.commons.httpclient.HttpException;
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
import com.jonas.stats.charts.common.dao.DBDao;
import com.jonas.stats.charts.common.dao.Dao;

public class RequestDBStatChart extends ApplicationFrame {

   private int dateCol = 0;

   // excel formula = TEXT(A2,"yyyy-MM-dd hh")
   public RequestDBStatChart(String title) {
      super(title);

      CommonTimeDenominatorStyle style = CommonTimeDenominatorStyle.hour;
      boolean aggregate = false;
      String chartTitle = "LLU SMPF Inventory Stats";
      String yTitle = "Services/Requests";

      try {
         Dao dao = new DBDao("SELECT created FROM copy_inventory..mlc_response_audit ORDER BY id DESC");
         // Dao dao = new DBDao("SELECT check_dt FROM copy_avail..llu_line_check_response WHERE id <= 9118267 and id >= 9028524"); // between 6th
         // - 24th of April

         // SELECT max(id) FROM copy_avail..llu_line_check_response
         // where datepart(day, check_dt) = 24
         // and
         // datepart(year, check_dt) = 2009
         // and
         // datepart(month, check_dt) = 4

         ContentsDto fileContentsDto = dao.loadContents();

         DateRetriever<String> timeRetriever = null;
         switch (style) {
         case day:
            timeRetriever = new DayFromDBDataRetriever();
            break;
         case hour:
            timeRetriever = new HourFromDBDataRetriever();
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
      for (Vector<Object> rowOfData : data) {
         String aRequest = rowOfData.get(dateCol).toString();
         RegularTimePeriod retrievedTime = dateRetriever.retrieveTimeLinePointFromObject(aRequest);
         LowestCommonDenominatorRegularTime denominator = new LowestCommonDenominatorRegularTime(retrievedTime, style);
         dataSetAggregator.addPointInTimeWithValue("Request", denominator, 1);
      }
      return dataSetAggregator;
   }

   public JPanel createChartPanel(PointsInTimeFacadeAbstract<String, ? extends RegularTimePeriod> dataSetAggregator, boolean aggregate,
         String chartTitle, String yTitle) {
      GroupingDTO<String>[] groupings = new GroupingDTO[] { new GroupingDTO<String>("Request", SwingUtil.cellBlue) };
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
      RequestDBStatChart demo = new RequestDBStatChart("ExcelStat Chart Tester");
      demo.setVisible(true);
   }

}
