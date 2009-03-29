/**
 * 
 */
package com.jonas.agile.devleadtool.gui.listener;

import java.awt.Frame;
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

   public AddNewRowAction(JTextComponent jiraPrefix, JTextComponent jiraCommas, Frame parentFrame) {
      this.jiraPrefix = jiraPrefix;
      this.jiraCommas = jiraCommas;
      this.parentFrame = parentFrame;
      parser = new JiraRegexParser();
   }

   public void addJiraToTable(MyTable table) {
      TableModelListenerAlerter tableModelListenerAlerter = table.getTableModelListenerAlerter();
      synchronized (tableModelListenerAlerter) {
         tableModelListenerAlerter.activate();
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
            jiraAdded(jiraString, table, devEst, actual, release, remainder, qaEst);
         }
         tableModelListenerAlerter.setParent(parentFrame);
         tableModelListenerAlerter.deActivateAndAlert();
      }
   }


   public abstract JiraIssue getJiraIssue(String jira);

   /**
    * gets called after the jira has been added to the table.
    * 
    * @param estimate
    * @param actual
    * @param release
    * @param qaEst
    * @param remainder
    */
   public abstract void jiraAdded(String jiraKey, MyTable table, String estimate, String actual, String release, String remainder, String qaEst);
}
