/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.MyStringParser;
import com.jonas.jira.JiraIssue;

public abstract class AddNewRowActionListener implements ActionListener {
   private final JTextComponent jiraCommas;
   private final JTextComponent jiraPrefix;
   private Logger log = MyLogger.getLogger(AddNewRowActionListener.class);
   private MyStringParser parser;
   private MyTable table;
   private final Frame parentFrame;

   public AddNewRowActionListener(MyTable table, JTextComponent jiraPrefix, JTextComponent jiraCommas, Frame parentFrame) {
      this.table = table;
      this.jiraPrefix = jiraPrefix;
      this.jiraCommas = jiraCommas;
      this.parentFrame = parentFrame;
      parser = new MyStringParser();
   }

   public void addJiraToTable() {
      TableModelListenerAlerter tableModelListenerAlerter = table.getTableModelListenerAlerter();
      synchronized (tableModelListenerAlerter) {
         tableModelListenerAlerter.activate();
         List<String> jiras = parser.separateString(jiraCommas.getText(), " ;\t\n");

         for (String jira : jiras) {
            String prefix = jiraPrefix.getText();
            String jiraString = getJiraString(prefix, jira).trim();
            String estimate = getEstimateString(jira).trim();
            String actual = getActualString(jira).trim();
            JiraIssue jiraIssue = getJiraIssue(jiraString);
            if (jiraIssue == null) {
               log.debug("jiraIssue is null!");

               table.addJira(jiraString);
            } else {
               log.debug("jiraIssue is NOT null!");
               table.addJira(jiraIssue);
            }
            log.debug("added jira " + jiraString);
            jiraAdded(jiraString, table, estimate, actual);
         }
         tableModelListenerAlerter.setParent(parentFrame);
         tableModelListenerAlerter.deActivateAndAlert();
      }
   }

   public String getActualString(String jiraNumber) {
      return getJiraSplit(jiraNumber, 2);
   }

   public String getEstimateString(String jiraNumber) {
      return getJiraSplit(jiraNumber, 1);
   }

   private String getIndexInSplit(List<String> split, int i) {
      if (split.size() > i)
         return split.get(i);
      return "";
   }

   public abstract JiraIssue getJiraIssue(String jira);

   private String getJiraSplit(String jiraNumber, int i) {
      if (jiraNumber == null)
         return "";
      return parser.getRegexGroup(jiraNumber, i);
   }

   protected String getJiraString(String jiraPrefix, String jiraNumber) {
      List<String> split = parser.separateString(jiraNumber, "'/");
      jiraNumber = getIndexInSplit(split, 0);
      jiraPrefix = (jiraPrefix != null && jiraPrefix.trim().length() > 0 ? jiraPrefix : "");
      jiraNumber = (jiraNumber != null ? jiraNumber : "");
      return jiraPrefix + (isHyphenRequired(jiraPrefix, jiraNumber) ? "-" : "") + jiraNumber;
   }

   private boolean isEmpty(String jiraPrefix) {
      return (jiraPrefix == null || jiraPrefix.trim().length() == 0);
   }

   protected boolean isHyphenRequired(String jiraPrefix, String jiraNumber) {
      return !isEmpty(jiraPrefix) && !isEmpty(jiraNumber);
   }

   /**
    * gets called after the jira has been added to the table. 
    * @param estimate
    * @param actual
    */
   public abstract void jiraAdded(String jiraKey, MyTable table, String estimate, String actual);

   public void setTable(MyTable table) {
      this.table = table;
   }
}
