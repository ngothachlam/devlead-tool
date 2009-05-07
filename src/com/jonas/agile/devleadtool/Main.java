package com.jonas.agile.devleadtool;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.guice.DevLeadToolModule;
import com.jonas.common.logging.MyLogger;

public class Main {

   public static void main(String[] args) {
      if (args.length > 0) {
         MyLogger.setup(args[0]);
      }

      Injector injector = Guice.createInjector(new DevLeadToolModule());
      DevLeadTool tool = injector.getInstance(DevLeadTool.class);

      try {
         tool.start();
      } catch (Throwable e) {
         AlertDialog.alertException(tool.getHelper().getParentFrame(), e);
      }
   }

   // TODO add type of issue to board: story, test, dev, bug, merge.
   // TODO using property files (an lludevsup.sprinterini for example creates a lludevsup menuitem) to dynamically add/remove rearrange columns menuitems

   // TODO add a status bar at the bottom showing the total of 1) selected, 2) table entries and 3) model entries for 4)each table
   // TODO drag'n'drop table rows = all tables!!
   // TODO handle 'issue not found' when syncing to jira and getting info (LLU-2)
   // TODO handle issue moved to other project (LLU-6 that has become LLUOLD-1 and LLU-4410 that has moved to TTGCONFIG)

   // TODO:
   //      
   // JXBusyLabel label = new JXBusyLabel(new Dimension(32,33));
   // BusyPainter painter = new BusyPainter(
   // new RoundRectangle2D.Float(0, 0,11.0f,2.8f,10.0f,10.0f),
   // new Ellipse2D.Float(5.0f,5.0f,23.0f,23.0f));
   // painter.setTrailLength(4);
   // painter.setPoints(15);
   // painter.setFrame(1);
   // label.setPreferredSize(new Dimension(32,33));
   // label.setIcon(new EmptyIcon(32,33));
   // label.setBusyPainter(painter);

   // Todo: JXTaskPane for sprint stats?
   // Shadows around JPanels
   // Datepicker
   // Addition of filter text
   // Introduce tip of the day!
   // decorator with beige text
   // JXStatusbar
   // FIXME 1 - http://www.jfree.org/jfreechart/samples.html
   
   //FIXME 1- Add print to pdf function
   
//   package com.jrefinery.chart.demo;
//   import java.awt.Graphics2D;
//   import java.awt.geom.Rectangle2D;
//   import java.io.File;
//   import java.io.OutputStream;
//   import java.io.BufferedOutputStream;
//   import java.io.DataOutputStream;
//   import java.io.FileOutputStream;
//   import java.io.IOException;
//   import com.lowagie.text.Document;
//   import com.lowagie.text.Rectangle;
//   import com.lowagie.text.DocumentException;
//   import com.lowagie.text.pdf.PdfWriter;
//   import com.lowagie.text.pdf.PdfContentByte;
//   import com.lowagie.text.pdf.PdfTemplate;
//   import com.lowagie.text.pdf.FontMapper;
//   import com.lowagie.text.pdf.DefaultFontMapper;
//   import com.lowagie.text.pdf.BaseFont;
//   import com.jrefinery.data.XYDataset;
//   5
//   import com.jrefinery.chart.JFreeChart;
//   import com.jrefinery.chart.ChartFactory;
//   import com.jrefinery.chart.demo.DemoDatasetFactory;
//   /**
//   * A simple demonstration showing how to write a chart to PDF format using
//   * JFreeChart and iText.
//   * <P>
//   * You can download iText from http://www.lowagie.com/iText.
//   */
//   public class ChartToPDFDemo1 {
//   /**
//   * Saves a chart to a PDF file.
//   *
//   * @param file The file.
//   * @param chart The chart.
//   * @param width The chart width.
//   * @param height The chart height.
//   */
//   public static void saveChartAsPDF(File file,
//   JFreeChart chart,
//   int width, int height,
//   FontMapper mapper) throws IOException {
//   OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
//   writeChartAsPDF(out, chart, width, height, mapper);
//   out.close();
//   }
//   /**
//   * Writes a chart to an output stream in PDF format.
//   *
//   * @param out The output stream.
//   * @param chart The chart.
//   * @param width The chart width.
//   * @param height The chart height.
//   */
//   public static void writeChartAsPDF(OutputStream out,
//   JFreeChart chart,
//   int width, int height,
//   FontMapper mapper) throws IOException {
//   Rectangle pagesize = new Rectangle(width, height);
//   Document document = new Document(pagesize, 50, 50, 50, 50);
//   try {
//   PdfWriter writer = PdfWriter.getInstance(document, out);
//   document.addAuthor("JFreeChart");
//   document.addSubject("Demonstration");
//   document.open();
//   PdfContentByte cb = writer.getDirectContent();
//   PdfTemplate tp = cb.createTemplate(width, height);
//   Graphics2D g2 = tp.createGraphics(width, height, mapper);
//   Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
//   chart.draw(g2, r2D, null);
//   g2.dispose();
//   cb.addTemplate(tp, 0, 0);
//   }
//   catch(DocumentException de) {
//   System.err.println(de.getMessage());
//   }
//   document.close();
//   }
//   /**
//   * Starting point for the demonstration application.
//   */
//   6
//   public static void main(String[] args) {
//   try {
//   // create a chart...
//   XYDataset data = DemoDatasetFactory.createSampleXYDataset();
//   JFreeChart chart = ChartFactory.createXYChart("PDF Test Chart 1",
//   "X", "Y",
//   data, true);
//   // write the chart to a PDF file...
//   File fileName = new File("/home/dgilbert/jfreechart1.pdf");
//   saveChartAsPDF(fileName, chart, 400, 300, new DefaultFontMapper());
//   }
//   catch (IOException e) {
//   System.out.println(e.getMessage());
//   }
//   }
//   }
   
   
}
