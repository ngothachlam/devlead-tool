package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.common.logging.MyLogger;

public class JiraTableModel extends MyTableModel {

   private static final ColumnDataType[] columns = { ColumnDataType.Jira, ColumnDataType.Description, ColumnDataType.J_Type, ColumnDataType.Planned_Sprint, ColumnDataType.Resolved_Sprint,
         ColumnDataType.Closed_Sprint, ColumnDataType.J_FixVersion, ColumnDataType.J_Status, ColumnDataType.J_Resolution, ColumnDataType.J_BuildNo, ColumnDataType.Dev_Estimate, ColumnDataType.Dev_Actual,
         ColumnDataType.J_Dev_Estimate, ColumnDataType.J_Dev_Spent };
   private Logger log = MyLogger.getLogger(JiraTableModel.class);

   public JiraTableModel() {
      super(columns);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<ColumnDataType> header) {
      super(columns, contents, header);
   }
}
