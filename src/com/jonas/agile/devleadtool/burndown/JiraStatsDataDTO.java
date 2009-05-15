package com.jonas.agile.devleadtool.burndown;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public  class JiraStatsDataDTO {

      private Set<String> duplicateJiras;
      private Map<String, JiraBurndownStat> jiras;
      private final MyTable sourceTable;

      public JiraStatsDataDTO(MyTable sourceTable) {
         this.sourceTable = sourceTable;
      }

      public void calculateJiraStats() {
         jiras = new HashMap<String, JiraBurndownStat>();
         duplicateJiras = new HashSet<String>();
         for (int row = 0; row < sourceTable.getRowCount(); row++) {
            String jira = (String) sourceTable.getValueAt(ColumnType.Jira, row);
            if (!jiras.containsKey(jira)) {
               JiraBurndownStat statRow = new JiraBurndownStat(jira, sourceTable, row);
               jiras.put(jira, statRow);
            } else {
               duplicateJiras.add(jira);
            }
         }
      }

      public Set<String> getDuplicateJiras() {
         return duplicateJiras;
      }

      public Map<String, JiraBurndownStat> getJiras() {
         return jiras;
      }

   }