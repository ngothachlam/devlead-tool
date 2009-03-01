package com.jonas.agile.devleadtool.component.frame.main;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MyFrame extends JFrame {

   @Inject
   public MyFrame(@Named("mainFrame.frame") String title) {
      super(title);
      try {
         String laf = UIManager.getCrossPlatformLookAndFeelClassName();
         laf = UIManager.getSystemLookAndFeelClassName();
         UIManager.setLookAndFeel(laf);
      } catch (Exception e) {
      }
      JFrame.setDefaultLookAndFeelDecorated(true);
      JDialog.setDefaultLookAndFeelDecorated(true);
   }

   @Override
   public void setJMenuBar(JMenuBar menubar) {
      // TODO Auto-generated method stub
      throw new RuntimeException("Method not implemented yet!");
   }
   
   

}
