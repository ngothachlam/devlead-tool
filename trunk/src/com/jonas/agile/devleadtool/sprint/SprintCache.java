package com.jonas.agile.devleadtool.sprint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.data.Cache;
import com.jonas.common.logging.MyLogger;

public class SprintCache implements Cache {

   public static final Sprint EMPTYSPRINT = new Sprint("", null, null, 0).setForCombobox();
   
   private final Vector<Sprint> sprints = new Vector<Sprint>();
   private final Set<String> sprintNames = new HashSet<String>();
   private static final Logger log = MyLogger.getLogger(SprintCache.class);

   public boolean cache(Sprint sprint, boolean allowDupes) {
      if (sprint.isForCombobox()) {
         log.debug("Sprint " + sprint + " is for the combobox only! cannot be cached! - likely a bug!");
         return false;
      }

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

   // FIXME 1 Remove all static methods in SprintCache
   // public static SprintCache getInstance() {
   // return instance;
   // }

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
            if (log.isDebugEnabled())
               log.debug("got " + sprint + " from name " + name);
            return sprint;
         }
      }
      Vector<Sprint> comboSprints = Sprint.getComboSprints();
      if (log.isDebugEnabled())
         log.debug("getComboSprint: length " + comboSprints.size());
      for (Sprint sprint : comboSprints) {
         if (log.isDebugEnabled())
            log.debug("getComboSprint: " + sprint + " has name: \"" + sprint.getName() + "\"");
         if (sprint.getName().equals(name.trim())) {
            if (log.isDebugEnabled())
               log.debug("got " + sprint + " from name " + name);
            return sprint;
         }
      }
      log.warn("did not get a sprint from name \"" + name + "\"");
      return null;
   }

   public void removeSprint(Sprint sprint) {
      sprints.remove(sprint);
      sprintNames.remove(sprint.getName());
   }

   public Sprint getCurrentSprint() {
      for (Sprint sprint : sprints) {
         if (sprint.calculateTime() == SprintTime.currentSprint)
            return sprint;
      }
      return null;
   }

   public SprintTime calculateSprintTime(Sprint sprint) {
      return sprint.calculateTime();
   }

}
