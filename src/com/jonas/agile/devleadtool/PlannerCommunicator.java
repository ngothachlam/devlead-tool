package com.jonas.agile.devleadtool;

import java.util.List;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;

public class PlannerCommunicator {

   private static final String N_A = "N/A";
   private final PlannerHelper plannerHelper;

   public PlannerCommunicator(PlannerHelper plannerHelper) {
      this.plannerHelper = plannerHelper;
   }

   public List<BoardStatus> getJiraStatusFromBoard(String jira) {
      InternalFrame activeFrame = plannerHelper.getActiveInternalFrame();
      BoardTableModel boardModel = (BoardTableModel) activeFrame.getBoardModel();
      return boardModel.getStatus(jira);
   }

   public String getPlannedSprint(String jira) {
      return getColumnValue(jira, ColumnDataType.Planned_Sprint);
   }

   public String getResolvedSprint(String jira) {
      return getColumnValue(jira, ColumnDataType.Resolved_Sprint);
   }

   private String getColumnValue(String jira, ColumnDataType column) {
      InternalFrame activeFrame = plannerHelper.getActiveInternalFrame();
      PlanTableModel model = (PlanTableModel) activeFrame.getPlanModel();
      if (model.doesJiraExist(jira))
         return (String) model.getValueAt(column, jira);
      else
         return N_A;
   }

   public String getClosedSprint(String jira) {
      return getColumnValue(jira, ColumnDataType.Closed_Sprint);
   }

}
