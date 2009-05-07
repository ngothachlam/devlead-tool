package com.jonas.agile.devleadtool.gui.component.table.model;

import java.util.Set;

import org.apache.derby.tools.sysinfo;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;

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
      if (value == null) {
         return validateOnEmptyOrNull(row, valueGetter);
      }
      String stringValue = value.toString();
      if (isEmptyString(stringValue)) {
         return validateOnEmptyOrNull(row, valueGetter);
      } else {
         Object boardStatus = valueGetter.getBoardStatus(row);
         if (isBoardValueEither(row, cellColorHelper.getRequiredDevEstimates(), boardStatus)) {
            return ValidatorResponse.FAIL;
         }
      }
      return null;
   }

   private ValidatorResponse validateOnEmptyOrNull(int row, ValueGetter valueGetter) {
      IssueType type = valueGetter.getType(row);
      switch (type) {
         case BUG:
         case EXTERNAL:
         case PRODISSUE:
         case MERGE:
         case TEST:
            return ValidatorResponse.PASS;
         case DEFAULT:
         case DEV:
         case STORY:
            return ValidatorResponse.FAIL;
      }
      return null;
   }

   private boolean isEmptyString(String stringValue) {
      return stringValue == null || stringValue.trim().length() <= 0;
   }

   boolean isBoardValueEither(int row, Set<BoardStatusValue> set, Object boardStatus) {
      return set.contains(boardStatus);
   }

   private String getObjectAsNonNull(Object object) {
      return object == null ? "<NULL>" : object.toString();
   }
}
