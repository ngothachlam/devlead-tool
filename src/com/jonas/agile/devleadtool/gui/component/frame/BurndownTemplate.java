package com.jonas.agile.devleadtool.gui.component.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
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
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class BurndownTemplate extends JFrame {

   private XYSeries ideal;
   // private XYSeries prognosisSeries;

   private JTextField lengthTextField;
   private JTextField totalTextField;
   private ValueAxis xAxis;
   private NumberAxis yAxis;

   private ChartPanel panel;
   private TextTitle source;

   private JTextField name;

   public static void main(String[] args) {
      BurndownTemplate template = new BurndownTemplate();

      template.pack();

      template.setLocationRelativeTo(null);
      template.setVisible(true);
   }

   public BurndownTemplate() {
      this.prepareBurndown();
      this.setContentPane(getMyPanel());
   }

   public void calculateAndPrintBurndown() {
      source.setText(name.getText());
      ideal.clear();

      double length = StringHelper.getDoubleOrZero(lengthTextField.getText());
      double total = StringHelper.getDoubleOrZero(totalTextField.getText());

      ideal.add(0d, total);
      ideal.add(length, 0);

      xAxis.setLowerBound(0d);
      xAxis.setUpperBound(length + 0.2d);

      yAxis.setAutoRange(true);
      yAxis.setLowerBound(0d);

   }

   public Container getMyPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(getTopPanel(), BorderLayout.NORTH);
      mainPanel.add(panel, BorderLayout.CENTER);
      return mainPanel;
   }

   private Component getTopPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
      JPanel inputPanel = getSubInputPanel();
      JPanel buttonPanel = getSubButtonPanel();
      panel.setBorder(BorderFactory.createTitledBorder("Sprint Info"));
      panel.add(inputPanel);
      panel.add(buttonPanel);
      return panel;
   }

   private JPanel getSubButtonPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 1, 3, 3));
      panel.add(new JButton(new UpdateAction(this)));
      return panel;
   }

   private JPanel getSubInputPanel() {
      JPanel panel = new JPanel(new GridLayout(3, 2, 3, 3));
      panel.add(new JLabel("Name:"));
      name = new JTextField(5);
      panel.add(name);

      panel.add(new JLabel("Total:"));
      totalTextField = new JTextField(5);
      panel.add(totalTextField);

      panel.add(new JLabel("Length:"));
      lengthTextField = new JTextField(5);
      panel.add(lengthTextField);

      return panel;
   }

   public void prepareBurndown() {
      ideal = new XYSeries("Ideal Progression");

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(ideal);

      // create the chart...
      JFreeChart chart = ChartFactory.createXYLineChart("Burndown", // chart title
            "Day", // x axis label
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

      renderer.setSeriesPaint(0, SwingUtil.cellGreen);

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

      private final BurndownTemplate template;

      public UpdateAction(BurndownTemplate template) {
         super("Update", "Updates the Burndown template", template);
         this.template = template;
      }

      @Override
      public void doActionPerformed(ActionEvent e) {
         template.calculateAndPrintBurndown();
      }

   }
}
