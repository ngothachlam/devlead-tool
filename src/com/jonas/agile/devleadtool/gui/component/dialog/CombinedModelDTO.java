/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.dialog;

import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.sprint.SprintCache;

public class CombinedModelDTO{
   private final BoardTableModel boardModel;
   private final JiraTableModel jiraModel;
   private final SprintCache sprintCache;
   public CombinedModelDTO(BoardTableModel boardModel, JiraTableModel jiraModel, SprintCache sprintCache) {
      this.boardModel = boardModel;
      this.jiraModel = jiraModel;
      this.sprintCache = sprintCache;
   }
   public BoardTableModel getBoardModel() {
      return boardModel;
   }
   public JiraTableModel getJiraModel() {
      return jiraModel;
   }
   public SprintCache getSprintCache() {
      return sprintCache;
   }
}