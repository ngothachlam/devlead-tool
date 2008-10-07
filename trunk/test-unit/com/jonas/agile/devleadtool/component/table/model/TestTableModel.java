package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;

public class TestTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.BoardStatus, Column.prio, Column.Note, Column.J_BuildNo, Column.J_Dev_Estimate, Column.isOpen };
   private Logger log = MyLogger.getLogger(JiraTableModel.class);

   public TestTableModel() {
      super(columns);
   }

   public TestTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   @Override
   public boolean isRed(Object value, int row, int column) {
      return false;
   }

}
