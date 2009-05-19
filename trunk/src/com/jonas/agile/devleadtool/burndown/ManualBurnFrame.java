package com.jonas.agile.devleadtool.burndown;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.frame.AbstractBasicFrame;
import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnFrame extends AbstractBasicFrame {

   protected DateHelper dateHelper;
   protected JTextField name;

   protected ChartPanel panel;
   private final BurnDataRetriever retriever;
   protected TextTitle source;

   private DefaultTableXYDataset seriesCollectionForBurnUp;
   private XYSeriesCollection seriesCollectionForBurnDown;
   private XYPlot xyPlot;
   private int dataSetCount = 0;

   public ManualBurnFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever) {
      this(parent, dateHelper, retriever, false);
   }

   ManualBurnFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever, boolean closeOnExit) {
      super(parent, null, null, closeOnExit);
      this.dateHelper = dateHelper;
      this.retriever = retriever;
      this.prepareBurndown();
   }

   public JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
      NumberAxis xAxis = new NumberAxis(xAxisLabel);
      NumberAxis yAxis = new NumberAxis(yAxisLabel);

      xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      xyPlot = new XYPlot(null, xAxis, yAxis, null);
      xyPlot.setOrientation(orientation);
      xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

      return new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, xyPlot, legend);
   }

   public final void createNewSeriesAndAddToCollection(BurnType burnType, String categoryName, List<BurnDataColumn> burndownDays) {
      XYSeries newSeries = new XYSeries(categoryName, true, false);

      for (BurnDataColumn burnDownDay : burndownDays) {
         newSeries.add(burnDownDay.getX(), burnDownDay.getY());
      }

      switch (burnType) {
         case BurnDown:
            seriesCollectionForBurnDown = getBurnDownAndinitialiseIfRequired();
            seriesCollectionForBurnDown.addSeries(newSeries);
            break;
         case BurnUp:
            seriesCollectionForBurnUp = getBurnUpAndInitialiseIfRequired();
            seriesCollectionForBurnUp.addSeries(newSeries);
            break;
      }
   }

   private DefaultTableXYDataset getBurnUpAndInitialiseIfRequired() {
      if (seriesCollectionForBurnUp == null) {
         seriesCollectionForBurnUp = new DefaultTableXYDataset();
         xyPlot.setDataset(dataSetCount, seriesCollectionForBurnUp);
         XYItemRenderer renderer = new StackedXYAreaRenderer2();

         int row = 0;
         renderer.setSeriesPaint(row++, SwingUtil.cellGreen);
         renderer.setSeriesPaint(row++, SwingUtil.cellBlue);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightBlue);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightRed);
         renderer.setSeriesPaint(row++, SwingUtil.cellWhite);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightGreen);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);

         xyPlot.setRenderer(dataSetCount++, renderer);
      }

      return seriesCollectionForBurnUp;
   }

   private XYSeriesCollection getBurnDownAndinitialiseIfRequired() {
      if (seriesCollectionForBurnDown == null) {
         seriesCollectionForBurnDown = new XYSeriesCollection();
         xyPlot.setDataset(dataSetCount, seriesCollectionForBurnDown);
         XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);

         int row = 0;
         renderer.setSeriesPaint(row++, SwingUtil.cellBlue);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightBlue);
         renderer.setSeriesPaint(row++, SwingUtil.cellRed);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightRed);
         renderer.setSeriesPaint(row++, SwingUtil.cellGreen);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightGreen);
         renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);
         renderer.setShapesVisible(true);
         renderer.setShapesFilled(true);

         xyPlot.setRenderer(dataSetCount++, renderer);
      }

      return seriesCollectionForBurnDown;
   }

   @Override
   public Container getMyPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(getTopPanel(), BorderLayout.NORTH);
      mainPanel.add(panel, BorderLayout.CENTER);
      return mainPanel;
   }

   private JPanel getSubButtonPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 1, 3, 3));
      panel.add(new JButton(new UpdateAction(this, this, retriever)));
      return panel;
   }

   private JPanel getSubInputPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 2, 3, 3));

      panel.add(new JLabel("Sprint Name:"));
      name = new JTextField(5);
      panel.add(name);

      return panel;
   }

   private Component getTopPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 2, 3, 3));
      JPanel inputPanel = getSubInputPanel();
      JPanel buttonPanel = getSubButtonPanel();
      panel.setBorder(BorderFactory.createTitledBorder("Sprint Info"));
      panel.add(inputPanel);
      panel.add(buttonPanel);
      return panel;
   }

   public void prepareBurndown() {
      JFreeChart chart = createChart("Sprint Burndown" + (dateHelper != null ? " - " + dateHelper.getTodaysDateAsString() : ""), // chart title
            "Day in Sprint", // x axis label
            "Completed Points", // y axis label
            PlotOrientation.VERTICAL, // data
            true, true, // include legend
            false // urls
      );

      source = new TextTitle();
      chart.addSubtitle(source);

      panel = new ChartPanel(chart);
   }

   public void updateBurndown() {
      source.setText(name.getText());

      BurnData data = retriever.getBurnData();

      Set<Category> categoryNames = data.getCategoryNames();
      List<BurnDataColumn> burndownDays = null;

      clearAllSeries();

      for (Category categoryName : categoryNames) {
         burndownDays = data.getDataForCategory(categoryName);
         Collections.sort(burndownDays);

         createNewSeriesAndAddToCollection(data.getBurnType(), categoryName.getName(), burndownDays);
      }

      ValueAxis xAxis = xyPlot.getDomainAxis();
      Integer length = data.getLength();
      if (length != null)
         xAxis.setUpperBound(length.doubleValue());
      else
         xAxis.setAutoRange(true);
      xAxis.setLowerBound(0d);

      ValueAxis yAxis = xyPlot.getRangeAxis();
      yAxis.setAutoRange(true);
      yAxis.setLowerBound(0d);

   }

   private void clearAllSeries() {
      if (seriesCollectionForBurnDown != null) {
         seriesCollectionForBurnDown.removeAllSeries();
      }
      if (seriesCollectionForBurnUp != null) {
         seriesCollectionForBurnUp.removeAllSeries();
      }
   }

   private class UpdateAction extends BasicAbstractGUIAction {

      private final ManualBurnFrame frame;
      private final BurnDataRetriever retriever;

      public UpdateAction(Frame parentFrame, ManualBurnFrame frame, BurnDataRetriever retriever) {
         super("Update", "Update graph!", parentFrame);
         this.frame = frame;
         this.retriever = retriever;
      }

      @Override
      public void doActionPerformed(ActionEvent e) {
         retriever.calculateBurndownData();
         frame.updateBurndown();
      }

   }

   public void setChartText(String string) {
      name.setText(string);
   }

}
