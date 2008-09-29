package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;

public class JiraTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.BoardStatus, Column.J_Type, Column.Planned_Sprint, Column.Resolved_Sprint,
         Column.Closed_Sprint, Column.J_FixVersion, Column.J_Status, Column.J_Resolution, Column.J_BuildNo, Column.Dev_Estimate, Column.Dev_Actual,
         Column.J_Dev_Estimate, Column.J_Dev_Spent };
   private Logger log = MyLogger.getLogger(JiraTableModel.class);

   public JiraTableModel() {
      super(columns);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }
}
