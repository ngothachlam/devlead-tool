package com.jonas.testing.jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class StackedBarChartDemo3 extends ApplicationFrame {

   /**
    * Creates a new demo.
    *
    * @param title  the frame title.
    */
   public StackedBarChartDemo3(final String title) {

       super(title);
       final CategoryDataset dataset = createDataset();
       final JFreeChart chart = createChart(dataset);
       final ChartPanel chartPanel = new ChartPanel(chart);
       chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
       setContentPane(chartPanel);

   }
   
   /**
    * Creates a sample dataset.
    * 
    * @return a sample dataset.
    */
   private CategoryDataset createDataset() {
       return DemoDatasetFactory.createCategoryDataset();
   }
   
   /**
    * Creates a sample chart.
    * 
    * @param dataset  the dataset for the chart.
    * 
    * @return a sample chart.
    */
   private JFreeChart createChart(final CategoryDataset dataset) {

       final JFreeChart chart = ChartFactory.createStackedBarChart(
           "Stacked Bar Chart Demo 3",  // chart title
           "Category",                  // domain axis label
           "Value",                     // range axis label
           dataset,                     // data
           PlotOrientation.VERTICAL,    // the plot orientation
           true,                        // legend
           false,                       // tooltips
           false                        // urls
       );
       final CategoryPlot plot = chart.getCategoryPlot();
       
       final ValueAxis rangeAxis = plot.getRangeAxis();
       rangeAxis.setLowerMargin(0.15);
       rangeAxis.setUpperMargin(0.15);
       return chart;
       
   }

   // ****************************************************************************
   // * JFREECHART DEVELOPER GUIDE                                               *
   // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
   // * to purchase from Object Refinery Limited:                                *
   // *                                                                          *
   // * http://www.object-refinery.com/jfreechart/guide.html                     *
   // *                                                                          *
   // * Sales are used to provide funding for the JFreeChart project - please    * 
   // * support us so that we can continue developing free software.             *
   // ****************************************************************************
   
   /**
    * Starting point for the demonstration application.
    *
    * @param args  ignored.
    */
   public static void main(final String[] args) {

       final StackedBarChartDemo3 demo = new StackedBarChartDemo3("Stacked Bar Chart Demo 3");
       demo.pack();
       RefineryUtilities.centerFrameOnScreen(demo);
       demo.setVisible(true);

   }

}