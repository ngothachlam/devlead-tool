package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;

public class ModelDto {

   private final BoardTableModel boardModel;
   private final JiraTableModel jiraModel;

   public ModelDto(BoardTableModel boardModel, JiraTableModel jiraModel) {
      this.boardModel = boardModel;
      this.jiraModel = jiraModel;
   }

}
