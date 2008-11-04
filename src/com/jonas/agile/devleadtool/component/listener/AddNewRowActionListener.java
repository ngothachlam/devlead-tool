/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.MyStringParser;
import com.jonas.jira.JiraIssue;

public class AddNewRowActionListener implements ActionListener {
   private MyTable table;
   private final JTextComponent jiraPrefix;
   private final JTextComponent jiraCommas;
   private Logger log = MyLogger.getLogger(AddNewRowActionListener.class);
   private MyStringParser parser;

   public AddNewRowActionListener(MyTable table, JTextComponent jiraPrefix, JTextComponent jiraCommas) {
      this.table = table;
      this.jiraPrefix = jiraPrefix;
      this.jiraCommas = jiraCommas;
      parser = new MyStringParser();
   }

   public void actionPerformed(@SuppressWarnings("unused")
   ActionEvent e) {
      List<String> jiras = parser.separateString(jiraCommas.getText(), " ,;.\t\n");
      for (String jiraNumber : jiras) {
         String prefix = jiraPrefix.getText();
         String jiraString = getJiraString(prefix, jiraNumber).trim();
         String estimate = getEstimateString(jiraNumber).trim();
         String actual = getAtualString(jiraNumber).trim();
         JiraIssue jiraIssue = getJiraIssue(jiraString);
         if (jiraIssue == null) {
            log.debug("jiraIssue is null!");
            table.addJira(jiraString);
         } else {
            log.debug("jiraIssue is NOT null!");
            table.addJira(jiraIssue);
         }
         jiraAdded(jiraString, table, estimate, actual);
      }
   }

   public String getAtualString(String jiraNumber) {
      List<String> split = parser.separateString(jiraNumber, "'");
      return getIndexInSplit(split, 2);
   }

   public String getEstimateString(String jiraNumber) {
      List<String> split = parser.separateString(jiraNumber, "'");
      return getIndexInSplit(split, 1);
   }

   private String getIndexInSplit(List<String> split, int i) {
      if (split.size() > i)
         return split.get(i);
      return "";
   }

   /**
    * Overide as required
    * 
    * @param actual
    * @param estimate
    */
   public void jiraAdded(String jiraStringm, MyTable table, String estimate, String actual) {
   }

   /**
    * Overide as required
    */
   public JiraIssue getJiraIssue(String jira) {
      return null;
   }

   protected String getJiraString(String jiraPrefix, String jiraNumber) {
      List<String> split = parser.separateString(jiraNumber, "'");
      jiraNumber = getIndexInSplit(split, 0);
      jiraPrefix = (jiraPrefix != null && jiraPrefix.trim().length() > 0 ? jiraPrefix : "");
      jiraNumber = (jiraNumber != null ? jiraNumber : "");
      return jiraPrefix + (isHyphenRequired(jiraPrefix, jiraNumber) ? "-" : "") + jiraNumber;
   }

   protected boolean isHyphenRequired(String jiraPrefix, String jiraNumber) {
      return !isEmpty(jiraPrefix) && !isEmpty(jiraNumber);
   }

   private boolean isEmpty(String jiraPrefix) {
      return (jiraPrefix == null || jiraPrefix.trim().length() == 0);
   }

   public void setTable(MyTable table) {
      this.table = table;
   }
}