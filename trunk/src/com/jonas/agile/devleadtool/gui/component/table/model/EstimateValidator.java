package com.jonas.agile.devleadtool.gui.component.table.model;

import java.util.Set;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;

public class EstimateValidator {

   private final BoardCellColorHelper cellColorHelper;

   public EstimateValidator(BoardCellColorHelper cellColorHelper) {
      this.cellColorHelper = cellColorHelper;
   }

   public ValidatorResponse getColor(Object value, int row, ColumnType column, ValueGetter boardTableModel) {
      switch (column) {
         case DEst:
            return validateDEst(value, row, boardTableModel);
      }
      return null;
   }

   ValidatorResponse validateDEst(Object value, int row, ValueGetter valueGetter) {
      if (value == null){
         Object type = valueGetter.getType(row);
         return ValidatorResponse.FAILURE;
      }
      String stringValue = value.toString();
      if (isEmptyString(stringValue)) {
         Object type = valueGetter.getType(row);
         if (isTypeValueEither(row, cellColorHelper.getRequiredDevEstimates(), type)) {

         }
         Object boardStatus = valueGetter.getBoardStatus(row);
         if (isBoardValueEither(row, cellColorHelper.getRequiredDevEstimates(), boardStatus)) {
            return ValidatorResponse.FAILURE;
         }
      }
      return null;
   }

   private boolean isEmptyString(String stringValue) {
      return stringValue == null || stringValue.trim().length() <= 0;
   }

   private boolean isTypeValueEither(int row, Set<BoardStatusValue> requiredDevEstimates, Object type) {
      // TODO Auto-generated method stub
      throw new RuntimeException("Method not implemented yet!");
   }

   boolean isBoardValueEither(int row, Set<BoardStatusValue> set, Object boardStatus) {
      return set.contains(boardStatus);
   }

   private String getObjectAsNonNull(Object object) {
      return object == null ? "<NULL>" : object.toString();
   }
}
