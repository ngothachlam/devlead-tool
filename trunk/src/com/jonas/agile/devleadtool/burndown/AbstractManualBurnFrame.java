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
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.xy.XYDataset;

import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.frame.AbstractBasicFrame;
import com.jonas.common.DateHelper;

public abstract class AbstractManualBurnFrame extends AbstractBasicFrame{

   protected ValueAxis xAxis;
   protected NumberAxis yAxis;

   protected ChartPanel panel;
   protected DateHelper dateHelper;
   protected TextTitle source;
   protected JTextField name;

   private final BurnDataRetriever retriever;

   public AbstractManualBurnFrame(Component parent, DateHelper dateHelper, BurnDataRetriever retriever, boolean closeOnExit) {
      super(parent, null, null, closeOnExit);
      this.dateHelper = dateHelper;
      this.retriever = retriever;
      this.prepareBurndown();
   }

   public void updateBurndown() {
      source.setText(name.getText());

      BurnData data = retriever.getBurnData();

      Set<String> categoryNames = data.getCategoryNames();
      List<BurnDataColumn> burndownDays = null;

      clearAllSeries();

      for (String categoryName : categoryNames) {
         burndownDays = data.getDataForCategory(categoryName);
         Collections.sort(burndownDays);
         
         createNewSeriesAndAddToCollection(categoryName, burndownDays);

      }

      xAxis.setAutoRange(true);
      xAxis.setLowerBound(0d);

      yAxis.setAutoRange(true);
      yAxis.setLowerBound(0d);

   }

   public abstract void clearAllSeries() ;

   public abstract void createNewSeriesAndAddToCollection(String categoryName, List<BurnDataColumn> burndownDays);

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
      JFreeChart chart = getChart();

      XYPlot plot = chart.getXYPlot();
      xAxis = plot.getDomainAxis();
      xAxis.setLowerBound(0);
      xAxis.setUpperBound(10);

      setRendererPaints((AbstractRenderer)plot.getRenderer());

      source = new TextTitle();
      chart.addSubtitle(source);

      yAxis = (NumberAxis) plot.getRangeAxis();
      xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      panel = new ChartPanel(chart);
   }

   private class UpdateAction extends BasicAbstractGUIAction {

      private final AbstractManualBurnFrame frame;
      private final BurnDataRetriever retriever;

      public UpdateAction(Frame parentFrame, AbstractManualBurnFrame frame, BurnDataRetriever retriever) {
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

   public abstract void setRendererPaints(AbstractRenderer renderer);

   public abstract JFreeChart getChart();

   public JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel, XYDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
   
      NumberAxis xAxis = new NumberAxis(xAxisLabel);
      NumberAxis yAxis = new NumberAxis(yAxisLabel);
      
      XYItemRenderer renderer = getRenderer();
      if (tooltips) {
         renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
      }
      if (urls) {
         renderer.setURLGenerator(new StandardXYURLGenerator());
      }
      
      XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
      plot.setOrientation(orientation);
   
      JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
      return chart;
   }

   public abstract XYItemRenderer getRenderer();

}
