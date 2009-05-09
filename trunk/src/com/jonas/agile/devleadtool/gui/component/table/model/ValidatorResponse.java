package com.jonas.agile.devleadtool.gui.component.table.model;

import com.jonas.agile.devleadtool.gui.component.table.model.color.ValidatorResponseType;

public class ValidatorResponse {

   private final ValidatorResponseType type;
   private final String message;

   public ValidatorResponse(ValidatorResponseType type, String message){
      this.type = type;
      this.message = message;
   }

   public ValidatorResponseType getType() {
      return type;
   }

   public String getMessage() {
      return message;
   }
   
   
   
}
