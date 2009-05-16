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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer2;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.frame.AbstractBasicFrame;
import com.jonas.common.DateHelper;
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class ManualBurnUpFrame extends AbstractBasicFrame {

   private ValueAxis xAxis;
   private NumberAxis yAxis;

   private ChartPanel panel;
   private DateHelper dateHelper;
   private TextTitle source;
   private JTextField name;
   private DefaultTableXYDataset seriesCollection;

   private final BurnDownDataRetriever retriever;

   public static void main(String[] args) {
      ManualBurnUpFrame frame = new ManualBurnUpFrame(null, null, new BurnDownDataRetriever() {

         BurnDownData data;

         @Override
         public BurnDownData getBurnDownData() {
            return data;
         }

         @Override
         public void calculateBurndownData() {
            data = new BurnDownData();
            data.add("Closed", 0d, 0d);
            data.add("Closed", 1d, 1d);
            data.add("Closed", 2d, 2d);

            data.add("Resolved", 0d, 0d);
            data.add("Resolved", 1d, 1d);
            data.add("Resolved", 2d, 2d);

            data.add("In-Progress", 0d, 0d);
            data.add("In-Progress", 1d, 1d);
            data.add("In-Progress", 2d, 1d);

            data.add("Failed", 0d, 0d);
            data.add("Failed", 1d, 1d);
            data.add("Failed", 2d, 0d);

            data.add("Open", 0d, 3d);
            data.add("Open", 1d, 2d);
            data.add("Open", 2d, 1d);

            double dataFixes = 0d;
            data.add("Datafixes completed", 0d, dataFixes += 2d);
            data.add("Datafixes completed", 1d, dataFixes += 1d);
            data.add("Datafixes completed", 2d, dataFixes += 2d);
         }

      }, true);
      frame.setVisible(true);
   }

   public ManualBurnUpFrame(Component parent, DateHelper dateHelper, BurnDownDataRetriever retriever) {
      this(parent, dateHelper, retriever, false);
   }

   public ManualBurnUpFrame(Component parent, DateHelper dateHelper, BurnDownDataRetriever retriever, boolean closeOnExit) {
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
         System.out.println("Category: " + categoryName);
         XYSeries newSeries = new XYSeries(categoryName, true, false);

         burndownDays = data.getDataForCategory(categoryName);

         Collections.sort(burndownDays);
         for (BurnDownDay burnDownDay : burndownDays) {
            System.out.println(" x: " + burnDownDay.getX() + " y: " + burnDownDay.getY());
            newSeries.add(burnDownDay.getX(), burnDownDay.getY());
         }
         seriesCollection.addSeries(newSeries);

         lengthOfSprint = Math.max(lengthOfSprint, StringHelper.getDoubleOrZero(burndownDays.get(burndownDays.size() - 1).getX()));
         totalEstimate = Math.max(totalEstimate, burndownDays.get(0).getY());
      }
   }

   @Override
   public Container getMyPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(getTopPanel(), BorderLayout.NORTH);
      mainPanel.add(panel, BorderLayout.CENTER);
      return mainPanel;
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

   public void prepareBurndown() {
      seriesCollection = new DefaultTableXYDataset();

      // create the chart...
      JFreeChart chart = ChartFactory.createStackedXYAreaChart("Sprint Burndown" + (dateHelper != null ? " - " + dateHelper.getTodaysDateAsString() : ""), // chart title
            "Day in Sprint", // x axis label
            "Outstanding Points", // y axis label
            seriesCollection, // data
            PlotOrientation.VERTICAL, true, // include legend
            true, // tooltips
            false // urls
            );

      XYPlot plot = chart.getXYPlot();
      xAxis = plot.getDomainAxis();
      xAxis.setUpperMargin(0);
      xAxis.setLowerMargin(0);

      StackedXYAreaRenderer2 renderer = (StackedXYAreaRenderer2) plot.getRenderer();
      //
      int row = 0;
      renderer.setSeriesPaint(row++, SwingUtil.cellGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightBlue);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightRed);
      renderer.setSeriesPaint(row++, SwingUtil.cellWhite);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightGreen);
      renderer.setSeriesPaint(row++, SwingUtil.cellLightYellow);
      //
      // renderer.setShapesVisible(true);
      // renderer.setShapesFilled(true);

      source = new TextTitle();
      chart.addSubtitle(source);

      yAxis = (NumberAxis) plot.getRangeAxis();
      xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      panel = new ChartPanel(chart);
   }

   private class UpdateAction extends BasicAbstractGUIAction {

      private final ManualBurnUpFrame frame;
      private final BurnDownDataRetriever retriever;

      public UpdateAction(Frame parentFrame, ManualBurnUpFrame frame, BurnDownDataRetriever retriever) {
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
