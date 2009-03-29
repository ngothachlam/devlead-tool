package com.jonas.agile.devleadtool.gui.listener;

import java.awt.Frame;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.tree.SprintTree;
import com.jonas.agile.devleadtool.gui.component.tree.xml.JiraDTO;
import com.jonas.agile.devleadtool.gui.component.tree.xml.JiraParseListener;
import com.jonas.common.logging.MyLogger;

public class JiraParseListenerImpl implements JiraParseListener {

   public void setTree(SprintTree tree) {
      this.tree = tree;
   }

   private Logger log = MyLogger.getLogger(JiraParseListenerImpl.class);

   private SprintTree tree;
   private int count;
   private final int maxResults;
   private Frame frame;

   public JiraParseListenerImpl(int maxResults, Frame frame) {
      super();
      this.maxResults = maxResults;
      this.frame = frame;
   }

   @Override
   public void notifyParsed(final JiraDTO jira) {
      log.debug("notifyParsed " + jira.getKey());
      StringBuffer sb = new StringBuffer("Building Table... (");
      sb.append(++count);
      sb.append(")");
      MyStatusBar.getInstance().setMessage(sb.toString(), false);
      tree.addJira(jira);
   }

   @Override
   public void notifyParsingFinished() {
      StringBuffer sb = new StringBuffer("Jiras Loaded: ");
      sb.append(count);
      MyStatusBar.getInstance().setMessage(sb.toString(), true);
      if (count >= maxResults) {
         AlertDialog.alertMessage(frame, "Downloaded " + count + ", which is the max obtainable result.\nRecommend to sync per sprint!");
      }
   }

   @Override
   public void notifyParsingStarted() {
      count = 0;
   }

}
