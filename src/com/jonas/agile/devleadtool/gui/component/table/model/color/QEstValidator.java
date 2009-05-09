package com.jonas.agile.devleadtool.gui.component.table.model.color;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.agile.devleadtool.gui.component.table.model.ValidatorResponse;

public class QEstValidator extends AbstractValidator {

   @Override
   public ValidatorResponse validate(Object value, IssueType type, Object boardStatus) {
      switch (type) {
         case TBD:
         case BUG:
         case EXTERNAL:
         case PRODISSUE:
         case MERGE:
            return passOnEmpty(value, new MessageDTO(type, true), new MessageDTO(false));
         case TEST:
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
                  return passOnNumeric(value, type, boardStatusValue);
               case NA:
               case UnKnown:
                  return passOnEmpty(value, new MessageDTO(type, true), new MessageDTO(boardStatusValue, true));
            }
            throw new RuntimeException(getExceptionString(value, type, boardStatus));
      }
      throw new RuntimeException(getExceptionString(value, type, boardStatus));
   }

   private String getExceptionString(Object value, IssueType type, Object boardStatus) {
      return "Could not figure out if value \"" + value + "\" with to issue type " + type + " and board status " + boardStatus + " should pass or fail validation";
   }

}
