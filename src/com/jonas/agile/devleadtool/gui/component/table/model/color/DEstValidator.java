package com.jonas.agile.devleadtool.gui.component.table.model.color;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.common.string.StringHelper;

public class DEstValidator implements Validator {

   @Override
   public ValidatorResponse validate(Object value, IssueType type, Object boardStatus) {
      switch (type) {
         case TBD:
         case BUG:
         case EXTERNAL:
         case PRODISSUE:
         case MERGE:
         case TEST:
            if (StringHelper.isEmpty(value))
               return ValidatorResponse.PASS;
            return ValidatorResponse.FAIL;
         case DEV:
         case STORY:
            BoardStatusValue boardStatusValue = (BoardStatusValue) boardStatus;
            switch (boardStatusValue) {
               case Approved:
               case Complete:
               case ForShowCase:
               case InProgress:
               case Open:
               case Resolved:
               case Failed:
                  if (StringHelper.isDouble(value))
                     return ValidatorResponse.PASS;
                  return ValidatorResponse.FAIL;
               case NA:
               case UnKnown:
                  if (StringHelper.isEmpty(value))
                     return ValidatorResponse.PASS;
                  return ValidatorResponse.FAIL;
            }
            throw new RuntimeException(getExceptionString(value, type, boardStatus));
      }
      throw new RuntimeException(getExceptionString(value, type, boardStatus));
   }

   private String getExceptionString(Object value, IssueType type, Object boardStatus) {
      return "Could not figure out if value \"" + value + "\" with to issue type " + type + " and board status " + boardStatus + " should pass or fail validation";
   }

}
