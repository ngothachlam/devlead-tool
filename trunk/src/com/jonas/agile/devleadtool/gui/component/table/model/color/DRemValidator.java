package com.jonas.agile.devleadtool.gui.component.table.model.color;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.agile.devleadtool.gui.component.table.model.ValidatorResponse;

public class DRemValidator extends AbstractValidator {

   @Override
   public ValidatorResponse validate(Object value, IssueType type, Object boardStatus) {
      switch (type) {
         case TBD:
         case BUG:
         case EXTERNAL:
         case PRODISSUE:
         case MERGE:
         case TEST:
            return passOnEmpty(value, new MessageDTO(type, true), new MessageDTO(false));
         case DEV:
         case STORY:
            BoardStatusValue boardStatusValue = (BoardStatusValue) boardStatus;
            switch (boardStatusValue) {
               case InProgress:
                  return passOnNumeric(value, type, boardStatusValue);
               case UnKnown:
               case NA:
               case Open:
               case Failed:
               case Resolved:
               case Complete:
               case ForShowCase:
               case Approved:
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
