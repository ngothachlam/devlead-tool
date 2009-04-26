package com.jonas.agile.devleadtool.sprint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class SprintCache {

   private final static Vector<Sprint> sprints = new Vector<Sprint>();
   private static final SprintCache instance = new SprintCache();
   private final static Set<String> sprintNames = new HashSet<String>();

   public boolean cache(Sprint sprint, boolean allowDupes) {
      if (!allowDupes && !sprintNames.contains(sprint.getName())) {
         sprints.add(sprint);
         sprintNames.add(sprint.getName());
         return true;
      } else if (allowDupes) {
         sprints.add(sprint);
         sprintNames.add(sprint.getName());
         return true;
      }
      return false;

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

   public Vector<Sprint> getSprints() {
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

   public Sprint getSprintFromRow(int row) {
      return sprints.get(row);
   }

   public Sprint getSprintWithName(String name) {
      for (Sprint sprint : sprints) {
         if (sprint.getName().equals(name.trim())) {
            return sprint;
         }
      }
      return null;
   }

   public void removeSprint(Sprint sprint) {
      sprints.remove(sprint);
      sprintNames.remove(sprint.getName());
   }

   public SprintTime getSprintTime(Sprint sprint) {
      Date startDate = sprint.getStartDate();
      Date endDate = sprint.getEndDate();

      Calendar calendar = Calendar.getInstance();
      Date today = calendar.getTime();

      boolean startDatePreToday = today.compareTo(startDate) >= 0;
      boolean endDatePostToday = today.compareTo(endDate) <= 0;

      System.out.println("Today: " + today);
      System.out.println("Start: " + sprint.getStartDate());
      System.out.println("End  : " + sprint.getEndDate());
      System.out.println("startDatePreToday: " + startDatePreToday + " today.compareTo(startDate): " + today.compareTo(startDate));
      System.out.println("endDatePostToday : " + endDatePostToday + " today.compareTo(endDate)  : " + today.compareTo(endDate));

      if (startDatePreToday && endDatePostToday) {
         return SprintTime.currentSprint;
      } else if (startDatePreToday && !endDatePostToday) {
         return SprintTime.beforeCurrentSprint;
      } else if (!startDatePreToday && endDatePostToday) {
         return SprintTime.afterCurrentSprint;
      }
      return null;
   }

}
