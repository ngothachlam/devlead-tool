package com.jonas.agile.devleadtool.data;

import java.util.ArrayList;
import java.util.List;
import com.jonas.agile.devleadtool.component.listener.PlanFixVersionListener;
import com.jonas.jira.JiraProject;

public class PlanFixVersion {

   private final String text;
   private final JiraProject project;
   private final static List<PlanFixVersion> list = new ArrayList<PlanFixVersion>();
   private final static List<PlanFixVersionListener> listeners = new ArrayList<PlanFixVersionListener>();

   public PlanFixVersion(String text, JiraProject project) {
      this.text = text;
      this.project = project;
      list.add(this);
      notifyListenersFixVersionHasBeenAdded(this, project);
   }

   public String toString() {
      return text;
   }

   public static void remove(PlanFixVersion planFixVersion) {
      list.remove(planFixVersion);
      notifyListenersFixVersionHasBeenRemoved();
   }

   private static void notifyListenersFixVersionHasBeenRemoved() {
      for (PlanFixVersionListener listener : listeners) {
         listener.planFixVersionRemoved();
      }
   }

   private void notifyListenersFixVersionHasBeenAdded(PlanFixVersion planFixVersion, JiraProject project) {
      for (PlanFixVersionListener listener : listeners) {
         listener.planFixVersionAdded(planFixVersion, project);
      }
   }

   public static void addListener(PlanFixVersionListener listener) {
      listeners.add(listener);
   }

   public static void removeListener(PlanFixVersionListener listener) {
      listeners.remove(listener);
   }
}
