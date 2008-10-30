package com.jonas.testing.tree.fromScratch;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.DnDTree;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;
import com.jonas.testing.tree.fromScratch.xml.JiraParseListener;


public class JiraParseListenerImpl implements JiraParseListener {

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
            StringBuffer sb = new StringBuffer("Building Table... (");
            sb.append(++count);
            sb.append(")");
            MyStatusBar.getInstance().setMessage(sb.toString(), false);
            tree.createJira(jira);
         }
      });
   }

   @Override
   public void notifyParsingFinished() {
      runInEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            StringBuffer sb = new StringBuffer("Jiras Loaded: ");
            sb.append(++count);
            MyStatusBar.getInstance().setMessage(sb.toString(), true);
         }
      });
   }

   @Override
   public void notifyParsingStarted() {
      count = 0;
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
