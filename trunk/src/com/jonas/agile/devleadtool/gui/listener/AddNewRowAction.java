/**
 * 
 */
package com.jonas.agile.devleadtool.gui.listener;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.dto.JiraStringDTO;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.JiraRegexParser;
import com.jonas.jira.JiraIssue;

public abstract class AddNewRowAction extends AbstractAction {
   private final JTextComponent jiraCommas;
   private final JTextComponent jiraPrefix;
   private Logger log = MyLogger.getLogger(AddNewRowAction.class);
   private JiraRegexParser parser;
   private final Frame parentFrame;
   private List<JiraToBeReconciledListener> listeners = new ArrayList<JiraToBeReconciledListener>();

   public AddNewRowAction(JTextComponent jiraPrefix, JTextComponent jiraCommas, Frame parentFrame) {
      this.jiraPrefix = jiraPrefix;
      this.jiraCommas = jiraCommas;
      this.parentFrame = parentFrame;
      parser = new JiraRegexParser();
   }

   public void addJiraToTable(MyTable table) {
      TableModelListenerAlerter tableModelListenerAlerter = table.getTableModelListenerAlerter();
      if (tableModelListenerAlerter != null) {
         synchronized (tableModelListenerAlerter) {
            tableModelListenerAlerter.activate();
            addJiras(table);
            tableModelListenerAlerter.setParent(parentFrame);
            tableModelListenerAlerter.deActivateAndAlert();
         }
      } else {
         addJiras(table);
      }
   }

   private void addJiras(MyTable table) {
      List<String> jiras = parser.separateIntoJiras(jiraCommas.getText());

      for (String jira : jiras) {
         String prefix = parser.getPrefixWithHyphen(jiraPrefix.getText());

         JiraStringDTO dto = parser.separateJira(jira);

         String jiraString = prefix + dto.getJira();
         String devEst = dto.getDevEstimate();
         String actual = dto.getDevActual();
         String qaEst = dto.getQAEstimate();
         String remainder = dto.getDevRemainder();
         JiraIssue jiraIssue = getJiraIssue(jiraString);
         if (jiraIssue == null) {
            log.warn("jiraIssue is null!");
            table.addJira(jiraString);
         } else {
            log.debug("jiraIssue is NOT null!");
            table.addJira(jiraIssue);
         }
         String release = jiraIssue.getRelease().trim();
         log.debug("added jira " + jiraString);
         
         notifyJirasAdded(jiraString, table, devEst, actual, release, remainder, qaEst);
      }
   }

   private void notifyJirasAdded(String jira, MyTable table, String devEst, String actual, String release, String remainder, String qaEst) {
      for (JiraToBeReconciledListener jiraToBeReconciledListener : listeners) {
         jiraToBeReconciledListener.jiraAdded(jira, table, devEst, actual, release, remainder, qaEst);
      }
   }

   public void addJiraToBeReconciledListener(JiraToBeReconciledListener jiraToBeReconciledListener){
      listeners.add(jiraToBeReconciledListener);
   }
   
   public abstract JiraIssue getJiraIssue(String jira);

}
