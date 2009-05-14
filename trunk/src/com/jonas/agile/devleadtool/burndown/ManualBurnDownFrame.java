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

   private XYSeries idealProgression;

   private ValueAxis xAxis;

   private ChartPanel panel;
   private DateHelper dateHelper;
   private TextTitle source;
   private NumberAxis yAxis;
   private JTextField name;
   private XYSeriesCollection dataset;

   public static void main(String[] args) {
      ManualBurnDownFrame frame = new ManualBurnDownFrame(null, new DateHelper());
      frame.setVisible(true);
   }

   public ManualBurnDownFrame(Component parent, DateHelper dateHelper) {
      super(parent, null, null);
      this.dateHelper = dateHelper;
      this.prepareBurndown();
   }

   public void updateBurndown() {
      source.setText(name.getText());
      idealProgression.clear();

      BurnDownData data = getBurnDownData();

      Set<String> categoryNames = data.getCategoryNames();
      List<BurnDownDay> burndownDays = null;

      double lengthOfSprint = 0d;
      Double totalEstimate = 0d;
      for (String categoryName : categoryNames) {
         XYSeries estimateProgression = new XYSeries(categoryName);
         burndownDays = data.getDataForCategory(categoryName);
         estimateProgression.setKey(categoryName);

         Collections.sort(burndownDays);
         for (BurnDownDay burnDownDay : burndownDays) {
            estimateProgression.add(burnDownDay.getX(), burnDownDay.getY());
         }

         lengthOfSprint = Math.max(lengthOfSprint, StringHelper.getDoubleOrZero(burndownDays.get(burndownDays.size() - 1).getX()));
         totalEstimate = Math.max(totalEstimate, burndownDays.get(0).getY());

         dataset.addSeries(estimateProgression);
      }

      idealProgression.add(0, totalEstimate);
      idealProgression.add(lengthOfSprint, 0);

      xAxis.setAutoRange(true);
      xAxis.setLowerBound(0d);

      yAxis.setAutoRange(true);
      yAxis.setLowerBound(0d);

   }

   private BurnDownData getBurnDownData() {
      BurnDownData data = new BurnDownData();

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
      data.add("Critical Path", 1d, 22d);
      data.add("Critical Path", 4d, 3d);

      return data;
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
      panel.add(new JButton(new UpdateAction(this, this)));
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
      idealProgression = new XYSeries("Ideal Progression");

      dataset = new XYSeriesCollection();
      dataset.addSeries(idealProgression);

      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart("Sprint Burndown - " + dateHelper.getTodaysDateAsString(), // chart title
            "Day in Sprint", // x axis label
            "Outstanding Points", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL, true, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      xAxis = plot.getDomainAxis();
      xAxis.setLowerBound(0);
      xAxis.setUpperBound(10);

      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

      renderer.setSeriesPaint(0, SwingUtil.cellRed);
      renderer.setSeriesPaint(1, SwingUtil.cellBlue);
      renderer.setSeriesPaint(2, SwingUtil.cellLightRed);
      renderer.setSeriesPaint(3, SwingUtil.cellLightBlue);

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

      public UpdateAction(Frame parentFrame, ManualBurnDownFrame frame) {
         super("Update", "Update graph!", parentFrame);
         this.frame = frame;
      }

      @Override
      public void doActionPerformed(ActionEvent e) {
         frame.updateBurndown();
      }

   }

}
