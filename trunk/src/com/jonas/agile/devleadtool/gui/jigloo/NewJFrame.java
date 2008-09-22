package com.jonas.agile.devleadtool.gui.jigloo;
import java.awt.BorderLayout;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class NewJFrame extends javax.swing.JFrame {
   private JMenuBar jMenuBar1;
   private JDesktopPane jDesktopPane1;
   private JMenuItem jMenuItem1;
   private JMenu jMenu2;
   private JMenu jMenu1;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


   /**
   * Auto-generated main method to display this JFrame
   */
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            NewJFrame inst = new NewJFrame();
            inst.setLocationRelativeTo(null);
            inst.setVisible(true);
         }
      });
   }
   
   public NewJFrame() {
      super();
      initGUI();
   }
   
   private void initGUI() {
      try {
         setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
         {
            jDesktopPane1 = new JDesktopPane();
            getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
         }
         {
            jMenuBar1 = new JMenuBar();
            setJMenuBar(jMenuBar1);
            {
               jMenu1 = new JMenu();
               jMenuBar1.add(jMenu1);
               jMenu1.setText("File");
                            
            }
         }
         pack();
         this.setSize(648, 382);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
