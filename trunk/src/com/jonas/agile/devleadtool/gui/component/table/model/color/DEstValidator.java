package com.jonas.agile.devleadtool.gui.component.table.model.color;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;

public class DEstValidator implements Validator {

   @Override
   public ValidatorResponse validate(Object value, IssueType type, BoardStatusValue boardStatus) {
      if(type == null){
         return ValidatorResponse.PASS;
      }
      switch (type) {
         case TBD:
         case BUG:
         case EXTERNAL:
         case PRODISSUE:
         case MERGE:
         case TEST:
            return ValidatorResponse.PASS;
         case DEV:
         case STORY:
            return ValidatorResponse.FAIL;sdfsdf
      }
      return null;
   }

}
