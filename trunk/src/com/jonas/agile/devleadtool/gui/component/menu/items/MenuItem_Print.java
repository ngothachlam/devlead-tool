/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.menu.items;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import javax.swing.JTable.PrintMode;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.DateHelper;

public class MenuItem_Print extends MyMenuItem {
   private final MyTable source;
   private DateHelper dateHelper;

   public MenuItem_Print(Frame parent, String string, final MyTable source) {
      super(parent, string);
      this.source = source;
      dateHelper = new DateHelper();
   }

   @Override
   public void myActionPerformed(ActionEvent e) throws Throwable {
      Runnable runner = new Runnable() {

         public void run() {
            try {
               MessageFormat headerFormat = new MessageFormat(source.getTitle() + " - " + dateHelper.getTodaysDateAsString());
               MessageFormat footerFormat = new MessageFormat("- Page {0} - ");
               source.print(PrintMode.FIT_WIDTH, headerFormat, footerFormat);
            } catch (PrinterException pe) {
               System.err.println("Error printing: " + pe.getMessage());
            }
         }
      };
      // FIXME - this is quite slow!
      EventQueue.invokeLater(runner);
   }
}