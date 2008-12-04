package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class ModelDto {

   private final MyTableModel boardModel;
   private final JiraTableModel jiraModel;

   public ModelDto(MyTableModel boardModel, JiraTableModel jiraModel) {
      this.boardModel = boardModel;
      this.jiraModel = jiraModel;
   }

}
