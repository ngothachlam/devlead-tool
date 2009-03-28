package com.jonas.agile.devleadtool.gui.component.dialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import com.jonas.common.swing.SwingUtil;

public class ProgressDialog extends JDialog {

   @Override
   public void setVisible(boolean b) {
      if (b) {
         this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         if (owner != null) {
            owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         }
         pack();
         if (owner != null) {
            SwingUtil.centreWindowWithinWindow(this, owner);
         }
         super.setVisible(b);
      }
   }

   private JLabel label;
   private JProgressBar progressBar;
   private final Frame owner;

   public ProgressDialog(Frame owner, String title, String note, int maxProgress, boolean isVisibleNow) {
      super(owner, title);
      this.owner = owner;
      progressBar = new JProgressBar(0, maxProgress);
      JPanel panel = new JPanel(new BorderLayout());
      JPanel innerPanel = new JPanel(new BorderLayout());
      label = new JLabel(note);
      Dimension preferredSize = new Dimension(200, 20);
      label.setPreferredSize(preferredSize);
      progressBar.setStringPainted(false);
      progressBar.setValue(0);

      panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      label.setBorder(BorderFactory.createEmptyBorder(2, 3, 5, 3));

      innerPanel.add(label, BorderLayout.NORTH);
      innerPanel.add(progressBar, BorderLayout.CENTER);
      panel.add(innerPanel, BorderLayout.NORTH);

      getContentPane().add(panel);
      setResizable(false);
      setVisible(isVisibleNow);
   }

   public void increseProgress() {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            progressBar.setIndeterminate(false);
            progressBar.setValue(progressBar.getValue() + 1);
         }
      });
   }

   public void increseProgress(final String string) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            progressBar.setIndeterminate(false);
            progressBar.setValue(progressBar.getValue() + 1);
            label.setText(string);
         }
      });
   }

   public void setCompleteWithDelay(final int millisecondsDelay) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            progressBar.setIndeterminate(false);
            progressBar.setValue(progressBar.getMaximum());
            try {
               Thread.sleep(millisecondsDelay);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            if (owner != null)
               owner.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            dispose();
         }
      });

   }

   public void setIndeterminate(final boolean increaseCount) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            progressBar.setIndeterminate(true);
            if (increaseCount)
               progressBar.setValue(progressBar.getValue() + 1);
         }
      });
   }

   public void setNote(final String string) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            label.setText(string);
         }
      });
   }

   public String getNote() {
      return label.getText();
   }

   public static void main(String args[]) {
      JFrame f = new JFrame("Blah..");
      f.setSize(1000, 100);
      f.setVisible(true);

      int count = 5;
      ProgressDialog progress2 = new ProgressDialog(f, "Working...", "Copying Messages to Panel...", 0, true);
      ProgressDialog progress = new ProgressDialog(f, "Working...", "Copying Messages to Panel...", count, true);
      for (int i = 1; i <= count; i++) {
         try {
            Thread.sleep(1000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         if (i > 1 && i < 4) {
            progress.setIndeterminate(true);
         } else {
            progress.increseProgress();
         }
      }
      progress.setCompleteWithDelay(300);
      progress2.setCompleteWithDelay(300);
   }

   public void increaseMax(final String string, final int length) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            progressBar.setMaximum(progressBar.getMaximum() + length);
            label.setText(string);
         }
      });

   }

}