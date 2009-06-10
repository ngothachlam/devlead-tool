package com.jonas.agile.devleadtool.gui.component.table.column;

import java.util.HashSet;
import java.util.Set;
import com.jonas.agile.devleadtool.gui.component.table.TBDEnum;

public enum Owner implements TBDEnum<Owner> {

   TBD("<TBD>"), BUSINESS("Business"), Technology("Technology");

   private Set<Owner> statuses = new HashSet<Owner>();
   private final String toString;

   private Owner(String toString) {
      this.toString = toString;
      statuses.add(this);
   }

   public String toString() {
      return toString;
   }

   public static Owner get(String toString) {
      for (Owner issueType : Owner.values()) {
         if (issueType.toString().equals(toString))
            return issueType;
      }
      return TBD;
   }

   @Override
   public Owner getTBD() {
      return TBD;
   }

}
