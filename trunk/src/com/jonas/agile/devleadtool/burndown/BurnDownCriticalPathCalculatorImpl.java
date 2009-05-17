package com.jonas.agile.devleadtool.burndown;

import java.util.Map;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintTime;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.StringHelper;

public class BurnDownCriticalPathCalculatorImpl implements BurnCalculator {
   double remainingEstimates = 0d;
   double totalEstimates = 0d;
   Logger log = MyLogger.getLogger(BurnDownCriticalPathCalculatorImpl.class);
   private final MyTable sourceTable;

   public BurnDownCriticalPathCalculatorImpl(MyTable sourceTable) {
      this.sourceTable = sourceTable;
   }

   public void calculateBurnData() {
      sourceTable.getRowCount();
      MyTable boardTable = sourceTable;
      for (int row = 0; row < sourceTable.getRowCount(); row++) {

         Sprint sprint = (Sprint) boardTable.getValueAt(ColumnType.Sprint, row);
         SprintTime time = sprint.calculateTime();

         BoardStatusValue boardStatus = (BoardStatusValue) boardTable.getValueAt(ColumnType.BoardStatus, row);

         double dEst = 0d;
         double qEst = 0d;
         double dRem = 0d;
         double qRem = 0d;
         switch (time) {
            case currentSprint:
               dEst = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DEst, row));
               qEst = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QEst, row));

               this.totalEstimates += Math.max(dEst, qEst);

               switch (boardStatus) {
                  case Failed:
                  case InProgress:
                     dRem = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DRem, row));
                     qRem = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QRem, row));
                     break;
                  case Resolved:
                     qRem = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QRem, row));
                     break;
                  case NA:
                  case Open:
                  case UnKnown:
                     dRem = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DEst, row));
                     qRem = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QEst, row));
                     break;
               }
               this.remainingEstimates += Math.max(dRem, qRem);
               break;
         }

         if (log.isDebugEnabled()) {
            log.debug("\t row: " + row + " remaining Estimate: " + remainingEstimates + " total Estimate: " + totalEstimates);
         }
      }
   }

   public double getRemainingEstimates() {
      return remainingEstimates;
   }

   public double getTotalEstimates() {
      return totalEstimates;
   }

   public Comparable getKey() {
      return "Critical Path Progression";
   }

}