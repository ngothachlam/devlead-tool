package com.jonas.agile.devleadtool.gui.component.table.model.color;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.agile.devleadtool.gui.component.table.model.ValidatorResponse;
import com.jonas.common.string.StringHelper;

public abstract class AbstractValidator implements Validator {

   public abstract ValidatorResponse validate(Object value, IssueType type, Object boardStatus);

   protected ValidatorResponse passOnNumeric(Object value, IssueType type, BoardStatusValue boardStatusValue) {
      if (StringHelper.isDouble(value))
         return new ValidatorResponse(ValidatorResponseType.PASS, "");
      StringBuffer sb = new StringBuffer(100);
      sb.append("need to be numeric as type is ").append(type);
      sb.append(" and board status is ").append(boardStatusValue);
      return new ValidatorResponse(ValidatorResponseType.FAIL, sb.toString());
   }

   protected ValidatorResponse passOnEmpty(Object value, MessageDTO type, MessageDTO boardStatusValue) {
      if (StringHelper.isEmpty(value))
         return new ValidatorResponse(ValidatorResponseType.PASS, "");
      StringBuffer sb = new StringBuffer(100);
      sb.append("Cannot be filled out as");
      if (type.isToIncludeInMessage()) {
         sb.append(" type is ").append(type.getObject());
         if (boardStatusValue.isToIncludeInMessage())
            sb.append(" and");
      }
      if (boardStatusValue.isToIncludeInMessage()) {
         sb.append(" board status is ").append(boardStatusValue.getObject());
      }
      return new ValidatorResponse(ValidatorResponseType.FAIL, sb.toString());
   }
}
