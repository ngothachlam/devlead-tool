package com.jonas.agile.devleadtool.sprint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SprintCache {

   private final static Set<Sprint> sprints = new HashSet<Sprint>();
   private static final SprintCache instance = new SprintCache();

   public void cache(Sprint sprint) {
      sprints.add(sprint);
   }

   public static SprintCache getInstance() {
      return instance;
   }

   public int getColumnCount() {
      return 4;
   }

   public String getColumnName(int colCount) {
      switch (colCount) {
      case 0:
         return "Name";
      case 1:
         return "Start";
      case 2:
         return "End";
      case 3:
         return "Length";
      }
      return null;
   }

   public int getRowCount() {
      return sprints.size();
   }

   public Set<Sprint> getSprints() {
      return sprints;
   }

   public void setValueAt(Object value, Sprint sprint, short colCount) {
      switch (colCount) {
      case 0:
         sprint.setName(value.toString());
         break;
      case 1:
         sprint.setStartDate(getDate(value));
         break;
      case 2:
         sprint.setEndDate(getDate(value));
         break;
      case 3:
         sprint.setLength(new Integer(value.toString()));
         break;
      }

   }

   Date getDate(Object value) {
      DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
      try {
         return df.parse(value.toString());
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return null;
   }

   public Object getValueAt(Sprint sprint, short colCount) {
      switch (colCount) {
      case 0:
         return sprint.getName();
      case 1:
         return sprint.getStartDate();
      case 2:
         return sprint.getEndDate();
      case 3:
         return sprint.getLength();
      }
      return null;
   }

}
