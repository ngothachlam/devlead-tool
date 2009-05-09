package com.jonas.agile.devleadtool.gui.component.table.model.color;


public class MessageDTO {

   private final boolean isToIncludeInMessage;
   private Object object;

   public MessageDTO(boolean isToIncludeInMessage) {
      this.isToIncludeInMessage = isToIncludeInMessage;
   }

   public MessageDTO(Object object, boolean isToIncludeInMessage) {
      this.object = object;
      this.isToIncludeInMessage = isToIncludeInMessage;
   }

   public boolean isToIncludeInMessage() {
      return isToIncludeInMessage;
   }

   public Object getObject() {
      return object;
   }

}
