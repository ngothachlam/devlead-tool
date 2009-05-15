package com.jonas.agile.devleadtool.burndown;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagLayout;
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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.frame.AbstractBasicFrame;
import com.jonas.common.DateHelper;
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnDownFrame extends AbstractBasicFrame {

   private ValueAxis xAxis;
   private NumberAxis yAxis;

   private ChartPanel panel;
   private DateHelper dateHelper;
   private TextTitle source;
   private JTextField name;
   private XYSeriesCollection seriesCollection;

   private final BurnDownDataRetriever retriever;

   public static void main(String[] args) {
      ManualBurnDownFrame frame = new ManualBurnDownFrame(null, null, new BurnDownDataRetriever() {

         BurnDownData data;

         @Override
         public BurnDownData getBurnDownData() {
            return data;
         }

         @Override
         public void calculateBurndownData() {
            data = new BurnDownData();
            data.add("Real Progression", 0d, 15d + 7d);
            data.add("Real Progression", 1d, 16d + 7d);
            data.add("Real Progression", 2d, 16d + 5d);
            data.add("Real Progression", 3d, 13d + 3d);
            data.add("Real Progression", 4d, 13d + 1.5d);
            data.add("Real Progression", 5d, 13d + 0d);
            data.add("Real Progression", 6d, 4d + 7d);
            data.add("Real Progression", 7d, 2d + 2d);
            data.add("Real Progression", 8d, 2d + 4d);
            data.add("Real Progression", 9d, 1.75d + 3d);
            data.add("Real Progression", 10d, 1.75d + 2d);

            data.add("Ideal Progression", 0d, 15d + 7d);
            data.add("Ideal Progression", 10d, 0d);

            double dataFixes = 0d;
            data.add("Datafixes completed", 0d, dataFixes += 2d);
            data.add("Datafixes completed", 2d, dataFixes += 1d);
            data.add("Datafixes completed", 5d, dataFixes += 2d);
            data.add("Datafixes completed", 7d, dataFixes += 2d);
            data.add("Datafixes completed", 10d, dataFixes);
         }

      }, true);
      frame.setVisible(true);
   }

   public ManualBurnDownFrame(Component parent, DateHelper dateHelper, BurnDownDataRetriever retriever) {
      this(parent, dateHelper, retriever, false);
   }

   public ManualBurnDownFrame(Component parent, DateHelper dateHelper, BurnDownDataRetriever retriever, boolean closeOnExit) {
      super(parent, null, null, closeOnExit);
      this.dateHelper = dateHelper;
      this.retriever = retriever;
      this.prepareBurndown();
   }

   public void updateBurndown() {
      source.setText(name.getText());

      BurnDownData data = retriever.getBurnDownData();

      Set<String> categoryNames = data.getCategoryNames();
      List<BurnDownDay> burndownDays = null;

      double lengthOfSprint = 0d;
      Double totalEstimate = 0d;

      seriesCollection.removeAllSeries();

      for (String categoryName : categoryNames) {
         XYSeries newSeries = new XYSeries(categoryName);
         newSeries.setKey(categoryName);
         seriesCollection.addSeries(newSeries);

         burndownDays = data.getDataForCategory(categoryName);

         Collections.sort(burndownDays);
         for (BurnDownDay burnDownDay : burndownDays) {
            newSeries.add(burnDownDay.getX(), burnDownDay.getY());
         }

         lengthOfSprint = Math.max(lengthOfSprint, StringHelper.getDoubleOrZero(burndownDays.get(burndownDays.size() - 1).getX()));
         totalEstimate = Math.max(totalEstimate, burndownDays.get(0).getY());
      }

      xAxis.setAutoRange(true);
      xAxis.setLowerBound(0d);

      yAxis.setAutoRange(true);
      yAxis.setLowerBound(0d);

   }

   @Override
   public Container getMyPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(getTopPanel(), BorderLayout.NORTH);
      mainPanel.add(panel, BorderLayout.CENTER);
      return mainPanel;
   }

   private Component getTopPanel() {
      JPanel panel = new JPanel(new GridBagLayout());
      JPanel inputPanel = getSubInputPanel();
      JPanel buttonPanel = getSubButtonPanel();
      panel.setBorder(BorderFactory.createTitledBorder("Sprint Info"));
      panel.add(inputPanel);
      panel.add(buttonPanel);
      return panel;
   }

   private JPanel getSubButtonPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 1, 3, 3));
      panel.add(new JButton(new UpdateAction(this, this, retriever)));
      return panel;
   }

   private JPanel getSubInputPanel() {
      JPanel panel = new JPanel(new GridLayout(3, 2, 3, 3));

      panel.add(new JLabel("Sprint Name:"));
      name = new JTextField(5);
      panel.add(name);

      return panel;
   }

   public void prepareBurndown() {
      seriesCollection = new XYSeriesCollection();

      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart("Sprint Burndown" + (dateHelper != null ? " - " + dateHelper.getTodaysDateAsString() : ""), // chart title
            "Day in Sprint", // x axis label
            "Outstanding Points", // y axis label
            seriesCollection, // data
            PlotOrientation.VERTICAL, true, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      xAxis = plot.getDomainAxis();
      xAxis.setLowerBound(0);
      xAxis.setUpperBound(10);

      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

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

      source = new TextTitle();
      chart.addSubtitle(source);

      yAxis = (NumberAxis) plot.getRangeAxis();
      xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      panel = new ChartPanel(chart);
   }

   private class UpdateAction extends BasicAbstractGUIAction {

      private final ManualBurnDownFrame frame;
      private final BurnDownDataRetriever retriever;

      public UpdateAction(Frame parentFrame, ManualBurnDownFrame frame, BurnDownDataRetriever retriever) {
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

}
