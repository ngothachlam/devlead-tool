package com.jonas.agile.devleadtool.burndown;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.gui.component.frame.BoardStatsFrame;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintTime;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.StringHelper;

public class JiraBurndownProgressionStatImpl implements JiraBurnDownStat {
   private String jira;
   private double totalEstimate = 0d;
   private double remainingEstimate = 0d;
   private Logger log = MyLogger.getLogger(BoardStatsFrame.class);

   public JiraBurndownProgressionStatImpl(String jira, MyTable boardTable, int row) {
      this.jira = jira;

      Sprint sprint = (Sprint) boardTable.getValueAt(ColumnType.Sprint, row);
      SprintTime time = sprint.calculateTime();

      BoardStatusValue boardStatus = (BoardStatusValue) boardTable.getValueAt(ColumnType.BoardStatus, row);

      switch (time) {
         case currentSprint:
            this.totalEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DEst, row));
            this.totalEstimate += StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QEst, row));

            switch (boardStatus) {
               case Failed:
               case InProgress:
                  this.remainingEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DRem, row));
                  this.remainingEstimate += StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QRem, row));
                  break;
               case Resolved:
                  this.remainingEstimate += StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QRem, row));
                  break;
               case NA:
               case Open:
               case UnKnown:
                  this.remainingEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DEst, row));
                  this.remainingEstimate += StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QEst, row));
                  break;
            }
            break;
      }
   }

   public String getJira() {
      return jira;
   }

   public double getTotalEstimate() {
      return totalEstimate;
   }

   public double getRemainingEstimate() {
      return remainingEstimate;
   }

}