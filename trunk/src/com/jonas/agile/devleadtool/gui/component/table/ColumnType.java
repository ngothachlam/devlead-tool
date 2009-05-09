/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.table;

import java.util.HashMap;
import java.util.Map;

public enum ColumnType {

   Jira,
   Description,
   Merge,
   Resolved_Sprint,
   Closed_Sprint,
   DEst,
   QEst,
   DRem,
   QRem,
   DAct,
   QAct,
   Note,
   Release,
   BoardStatus,
   Sprint,
   Status,
   Resolution,
   BuildNo,
   FixVersion,
   Type,
   J_DevEst,
   J_DevAct,
   J_Sprint,
   Project,
   Delivery,
   Owner,
   Environment,
   prio,
   isParked,
   J_Type,
   Old;

   private final Map<String, ColumnType> mapTypes = new HashMap<String, ColumnType>();

   private ColumnType() {
      mapTypes.put(this.toString(), this);
   }

   public ColumnType get(String typeName) {
      return mapTypes.get(typeName);
   }
}
