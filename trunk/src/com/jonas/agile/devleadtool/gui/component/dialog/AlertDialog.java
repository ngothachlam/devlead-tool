package com.jonas.agile.devleadtool.gui.component.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyPanel;
import com.jonas.common.swing.SwingUtil;

public class AlertDialog extends JDialog {
   private static Logger log = MyLogger.getLogger(AlertDialog.class);
   private JTextArea textArea;

   private AlertDialog(Frame parent, String title, String preTextboxText, String alertTextboxText) {
      super(parent, title, true);

      MyPanel panel = new MyPanel(new BorderLayout()).bordered(15, 15, 15, 15);
      textArea = new JTextArea(alertTextboxText);
      textArea.setEditable(false);
      JButton button = new JButton("Close");
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            dispose();
         }
      });

      panel.addNorth(new JLabel(preTextboxText));
      panel.addCenter(new JScrollPane(textArea));
      panel.addSouth(button);

      setContentPane(panel);

      pack();
      setSize(new Dimension(700, 350));
      if (parent != null)
         SwingUtil.centreWindowWithinWindow(this, parent);
      else
         setLocationRelativeTo(null);

      setVisible(true);
   }

   public static void alertException(Frame parentFrame, Throwable e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String stacktrace = sw.toString();
      alertMessage(parentFrame, stacktrace);
   }

   public static AlertDialog alertMessage(Frame parentFrame, String title, String preTextBoxText, String textBoxText) {
      log.debug("alert message");
      return new AlertDialog(parentFrame, title, preTextBoxText, textBoxText);
   }

   public static void alertMessage(Frame parentFrame, String e) {
      new AlertDialog(parentFrame, "Alert...", "Message:", e);
   }

   public void addText(String string) {
      textArea.setText(textArea.getText() + string);
   }
}
