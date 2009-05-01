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
}
