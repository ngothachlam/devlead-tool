/**
 * 
 */
package com.jonas.agile.devleadtool.component.dialog;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;

public class CombinedModelDTO{
   private final BoardTableModel boardModel;
   private final JiraTableModel jiraModel;
   public CombinedModelDTO(BoardTableModel boardModel, JiraTableModel jiraModel) {
      this.boardModel = boardModel;
      this.jiraModel = jiraModel;
   }
   public BoardTableModel getBoardModel() {
      return boardModel;
   }
   public JiraTableModel getJiraModel() {
      return jiraModel;
   }
}