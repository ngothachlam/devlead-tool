package com.jonas.agile.devleadtool.burndown;

import java.util.HashMap;
import java.util.Map;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintTime;

public class BurnUpCalculator {

   private final MyTable sourceTable;
   private Map<String, Double> boardStatuses = new HashMap<String, Double>();

   public BurnUpCalculator(MyTable sourceTable) {
      this.sourceTable = sourceTable;
   }

   public void calculateBurnData() {
      BoardStatusValue[] boardStatuses = BoardStatusValue.values();
      for (BoardStatusValue boardStatus : boardStatuses) {
         MyTableModel myModel = sourceTable.getMyModel();
         for (int row = 0; row < myModel.getRowCount(); row++) {
            Sprint sprint = (Sprint) myModel.getValueAt(ColumnType.Sprint, row);
            if (sprint.calculateTime() != SprintTime.currentSprint) {
               continue;
            }
            BoardStatusValue theBoardStatus = (BoardStatusValue) myModel.getValueAt(ColumnType.BoardStatus, row);
            if (theBoardStatus != boardStatus) {
               continue;
            }
            
            Double adouble = getValue(theBoardStatus);
            Double thisValueIncrement = getThisValueIncrement(theBoardStatus, row, myModel);
            if (thisValueIncrement != null) {
               adouble += thisValueIncrement;
               saveValue(theBoardStatus, adouble);
            }
         }

      }
   }

   private Double getThisValueIncrement(BoardStatusValue theBoardStatus, int row, MyTableModel sourceTable) {
      Double valueAt = 0d;
      Object dEst = sourceTable.getValueAt(ColumnType.DEst, row);
      Object qEst = sourceTable.getValueAt(ColumnType.QEst, row);
      Object dRem = sourceTable.getValueAt(ColumnType.DRem, row);
      Object qRem = sourceTable.getValueAt(ColumnType.QRem, row);
      switch (theBoardStatus) {
         case Open:
         case Resolved:
         case Complete:
            valueAt = getDouble(dEst);
            valueAt += getDouble(qEst);
            return valueAt;
         case Failed:
         case InProgress:
            valueAt = getDouble(dRem);
            valueAt += getDouble(qRem);
            return valueAt;
      }
      return null;
   }

   private double getDouble(Object dEst) {
      double parseDouble;
      try {
         parseDouble = Double.parseDouble(dEst.toString());
      } catch (NumberFormatException e) {
         return 0d;
      }
      return parseDouble;
   }

   private void saveValue(BoardStatusValue theBoardStatus, Double value) {
      boardStatuses.put(theBoardStatus.getName(), value);
   }

   private Double getValue(BoardStatusValue theBoardStatus) {
      Double value = boardStatuses.get(theBoardStatus.getName());
      if (value == null) {
         value = new Double("0d");
         boardStatuses.put(theBoardStatus.getName(), value);
      }
      return value;
   }

   public double getCurrent(String string) {
      Double value = boardStatuses.get(string);
      return value == null ? 0d : value;
   }

   public double getCurrent(BoardStatusValue complete) {
      return getCurrent(complete.getName());
   }

}
