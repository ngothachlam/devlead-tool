package com.jonas.agile.devleadtool.component.dialog;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.testHelpers.TryoutTester;

public class AlertDialogTestScreen  {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      frame.setLocation(100, 100);
      frame.setVisible(true);
      AlertDialog.alertException(frame, TryoutTester.getTestException());
   }
}
