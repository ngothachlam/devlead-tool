package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;

public class PlannerCommunicator {

   private final PlannerHelper plannerHelper;

   public PlannerCommunicator(PlannerHelper plannerHelper) {
      this.plannerHelper = plannerHelper;
   }

   public BoardStatusValue getJiraStatusFromBoard(String jira) {
      MyInternalFrame activeFrame = plannerHelper.getActiveInternalFrame();
      BoardTableModel boardModel = (BoardTableModel) activeFrame.getBoardModel();
      return boardModel.getStatus(jira);
   }

}
