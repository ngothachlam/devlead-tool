package com.jonas.testing.tree.fromScratch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.DnDTree;
import com.jonas.testing.tree.fromScratch.tree.model.DnDTreeModel;
import com.jonas.testing.tree.fromScratch.xml.DnDTreeBuilder;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;
import com.jonas.testing.tree.fromScratch.xml.JiraParseListener;
import com.jonas.testing.tree.fromScratch.xml.JiraSaxHandler;
import com.jonas.testing.tree.fromScratch.xml.XmlParser;
import com.jonas.testing.tree.fromScratch.xml.XmlParserAtlassain;
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
         XmlParser parser = new XmlParserAtlassain(saxHandler);
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
         MyStatusBar.getInstance().setMessage("Starting to Build Table...", false);
         dndTreeBuilder.buildTree(tree);
      }
   }
}


class JiraParseListenerImpl implements JiraParseListener {

   private Logger log = MyLogger.getLogger(JiraParseListenerImpl.class);

   private DnDTree tree;
   private int count;

   public JiraParseListenerImpl(DnDTree tree) {
      super();
      this.tree = tree;
   }

   @Override
   public void notifyParsed(final JiraDTO jira) {
      runInEventDispatchThread(new Runnable() {

         @Override
         public void run() {
            StringBuffer sb = new StringBuffer("Starting to Build Table... (");
            sb.append(++count);
            sb.append(")");
            MyStatusBar.getInstance().setMessage(sb.toString(), false);
            tree.createJira(jira);
         }
      });
   }

   @Override
   public void notifyParsingFinished() {
   }

   @Override
   public void notifyParsingStarted() {
      count = 0;
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
