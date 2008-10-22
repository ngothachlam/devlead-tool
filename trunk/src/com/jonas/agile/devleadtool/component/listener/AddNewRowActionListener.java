/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.text.JTextComponent;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.string.MyStringParser;

public class AddNewRowActionListener implements ActionListener {
   private MyTable table;
   private final JTextComponent jiraPrefix;
   private final JTextComponent jiraCommas;

   public AddNewRowActionListener(MyTable table, JTextComponent jiraPrefix, JTextComponent jiraCommas) {
      this.table = table;
      this.jiraPrefix = jiraPrefix;
      this.jiraCommas = jiraCommas;
   }

   public void actionPerformed(@SuppressWarnings("unused")
   ActionEvent e) {
      MyStringParser parser = new MyStringParser();
      List<String> jiras = parser.separateString(jiraCommas.getText(), " ,;.\t\n");
      for (String jiraNumber : jiras) {
         String prefix = jiraPrefix.getText();
         String jiraString = getJiraString(prefix, jiraNumber).trim();

         table.addJira(jiraString);
      }
   }

   protected String getJiraString(String jiraPrefix, String jiraNumber) {
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