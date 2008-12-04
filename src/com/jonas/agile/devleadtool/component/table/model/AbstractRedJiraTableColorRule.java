package com.jonas.agile.devleadtool.component.table.model;

import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class AbstractRedJiraTableColorRule extends AbstractColorRule {

   private Logger log = MyLogger.getLogger(AbstractRedJiraTableColorRule.class);
   private final Column otherColumn;
   private final MyTable otherTable;

   public AbstractRedJiraTableColorRule(MyTable thisTable, Column thisColumn, MyTable otherTable, Column otherColumn, boolean isRealTime) {
      super(thisColumn, thisTable, SwingUtil.cellRED, isRealTime);
      this.otherTable = otherTable;
      this.otherColumn = otherColumn;
   }

   @Override
   public boolean isTrue(Object value, int row) {
      log.debug("value: " + value + " for thisTable " + getThisTable().getTitle());
      Object est = otherTable.getValueAt(otherColumn, getJira());
      if (est == null) {
         log.warn(getJira() + " or column " + otherColumn + " does not exist in table " + otherTable.getTitle());
         return false;
      }
      Double boardValue = getDoubleOrNull(est);
      Double jiraValue = getDoubleOrNull(value);
      log.warn(getJira() + "'s board value = " + boardValue + " (" + otherColumn + ") and jira value = " + jiraValue + " (" + column + ")");
      if ((boardValue != null && !boardValue.equals(jiraValue)))
         return true;
      return false;
   }

   private Double getDoubleOrNull(Object est) {
      Double result = null;
      try {
         result = Double.parseDouble(est.toString());
      } catch (NumberFormatException exception) {
      }
      return result;
   }
}
