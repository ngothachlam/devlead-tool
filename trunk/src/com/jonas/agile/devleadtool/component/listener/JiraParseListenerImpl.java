package com.jonas.agile.devleadtool.component.listener;

import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;
import com.jonas.agile.devleadtool.component.tree.xml.JiraParseListener;
import com.jonas.common.logging.MyLogger;

public class JiraParseListenerImpl implements JiraParseListener {

   private Logger log = MyLogger.getLogger(JiraParseListenerImpl.class);

   private DnDTree tree;
   private int count;
   private final int maxResults;
   private Frame frame;

   public JiraParseListenerImpl(DnDTree tree, int maxResults, Frame frame) {
      super();
      this.tree = tree;
      this.maxResults = maxResults;
      this.frame = frame;
   }

   @Override
   public void notifyParsed(final JiraDTO jira) {
      runInEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            log.debug("notifyParsed " + jira.getKey());
            StringBuffer sb = new StringBuffer("Building Table... (");
            sb.append(++count);
            sb.append(")");
            MyStatusBar.getInstance().setMessage(sb.toString(), false);
            tree.addJira(jira);
         }
      });
   }

   @Override
   public void notifyParsingFinished() {
      runInEventDispatchThread(new Runnable() {

         @Override
         public void run() {
            StringBuffer sb = new StringBuffer("Jiras Loaded: ");
            sb.append(count);
            MyStatusBar.getInstance().setMessage(sb.toString(), true);
            if (count >= maxResults) {
               AlertDialog.alertMessage(frame, "Downloaded " + count + ", which is the max obtainable result.\nRecommend to sync per sprint!");
            }
         }
      });
   }

   @Override
   public void notifyParsingStarted() {
      count = 0;
   }

   private void runInEventDispatchThread(Runnable runnableInEventDispatchThread) {
      if (SwingUtilities.isEventDispatchThread()) {
         runnableInEventDispatchThread.run();
      } else {
         try {
            SwingUtilities.invokeAndWait(runnableInEventDispatchThread);
         } catch (InterruptedException e) {
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         }
      }
   }
}
