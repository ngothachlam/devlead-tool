package com.jonas.agile.devleadtool.component;

import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import com.jonas.testHelpers.TryoutTester;
import junit.framework.TestCase;

public class MyProgressMonitorTestScreen {

   public static void main(String args[]) {
      UIManager.put("ProgressMonitor.progressText", "This is progress?");
      UIManager.put("OptionPane.cancelButtonText", "Go Away");
      final JFrame frame = TryoutTester.getFrame();

      // Thread t = new Thread(new Runnable(){
      // public void run() {
      // for (int i = 0; i <= 10; i++) {
      // monitor.increaseProgress();
      // try {
      // Thread.sleep(1000);
      // } catch (InterruptedException e) {
      // e.printStackTrace();
      // }
      // }
      // }
      // });
      // t.start();
      frame.setVisible(true);

      SwingWorker worker = new SwingWorker() {
         public Object doInBackground() {
            final int j = 5;
            final MyProgressMonitor monitor = new MyProgressMonitor(frame, j);
            for (int i = 0; i <= j-1; i++) {
               monitor.increaseProgress();
               try {
                  System.out.println("sleeping...");
                  Thread.sleep(3000);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
            return "result";
         }
      };
      worker.execute();

      try {
         System.out.println("worker returns: \"" + worker.get() + "\"");
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (ExecutionException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
