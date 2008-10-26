package com.jonas.testing.tree.fromScratch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.DnDTree;
import com.jonas.testing.tree.fromScratch.tree.DnDTreeModel;
import com.jonas.testing.tree.fromScratch.xml.DnDTreeBuilder;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;
import com.jonas.testing.tree.fromScratch.xml.JiraParseListener;
import com.jonas.testing.tree.fromScratch.xml.JiraSaxHandler;
import com.jonas.testing.tree.fromScratch.xml.XmlParser;
import com.jonas.testing.tree.fromScratch.xml.XmlParserLargeMock;
import com.jonas.testing.tree.fromScratch.xml.XmlParserSmallMock;

public class DnDTreeMain extends JFrame {

   private Logger log = MyLogger.getLogger(DnDTreeMain.class);
   
   private DnDTree tree;
   private DnDTreeBuilder dndTreeBuilder;

   private DnDTreeMain() {
      super("DnDTree");

      try {
         DnDTreeModel model = new DnDTreeModel("LLU");
         tree = new DnDTree(model);

         JiraSaxHandler saxHandler = new JiraSaxHandler();
         XmlParser parser = new XmlParserSmallMock(saxHandler);
         dndTreeBuilder = new DnDTreeBuilder(parser);

         saxHandler.addJiraParseListener(new JiraParseListenerImpl(tree));

         makeUI();

      } catch (SAXException e) {
         e.printStackTrace();
      }
   }

   public static void main(String... args) {
      DnDTreeMain main = new DnDTreeMain();
   }

   private Component getButtonPanel() {
      JPanel panel = new JPanel(new BorderLayout());

      JButton refreshButton = new RefreshButton("Refresh", this, dndTreeBuilder, tree);
      panel.add(refreshButton, BorderLayout.CENTER);
      panel.add(MyStatusBar.getInstance(), BorderLayout.SOUTH);
      return panel;
   }

   private void makeUI() {
      setSize(300, 200);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
      getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
      setVisible(true);
   }

   private final class RefreshButton extends JButton implements ActionListener {
      private final DnDTreeBuilder dndTreeBuilder;
      private final Component parent;
      private final DnDTree tree;

      private RefreshButton(String text, Component parent, DnDTreeBuilder dnDTreeBuilder, DnDTree tree) {
         super(text);

         this.parent = parent;
         this.dndTreeBuilder = dnDTreeBuilder;
         this.tree = tree;

         this.addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         log.debug("Refresh Button Pressed!");
         MyStatusBar.getInstance().setMessage("Starting to Build Table...");
         dndTreeBuilder.buildTree(tree);
         MyStatusBar.getInstance().setMessage("Finished Building Table!");
      }
   }
}


class JiraParseListenerImpl implements JiraParseListener {

   private Logger log = MyLogger.getLogger(JiraParseListenerImpl.class);

   private DnDTree tree;

   public JiraParseListenerImpl(DnDTree tree) {
      super();
      this.tree = tree;
   }


   @Override
   public void notifyParsed(final JiraDTO jira) {
      runInEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            tree.createJira(jira);
         }
      });
   }

   @Override
   public void notifyParsingFinished() {
      runInEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            tree.reload();
         }
      });
   }

   @Override
   public void notifyParsingStarted() {
      runInEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            tree.removeAllChildren();
         }
      });
   }

   private void runInEventDispatchThread(Runnable runnableInEventDispatchThread) {
      if (SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(runnableInEventDispatchThread);
         } catch (InterruptedException e) {
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         }
      } else {
         runnableInEventDispatchThread.run();
      }
   }
}
