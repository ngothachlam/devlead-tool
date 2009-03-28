package com.jonas.agile.devleadtool.data;

import java.util.ArrayList;
import java.util.List;
import com.jonas.agile.devleadtool.gui.listener.PlanFixVersionListener;
import com.jonas.jira.JiraProject;

public class PlanFixVersion {

   private final static List<PlanFixVersion> list = new ArrayList<PlanFixVersion>();
   private final static List<PlanFixVersionListener> listeners = new ArrayList<PlanFixVersionListener>();
   private JiraProject project;
   private String text;

   public PlanFixVersion(String text, JiraProject project) {
      this.text = text;
      this.project = project;
      list.add(this);
      notifyListenersFixVersionHasBeenAdded(this, project);
   }

   public static void addListener(PlanFixVersionListener listener) {
      listeners.add(listener);
   }

   public static List<PlanFixVersion> getList() {
      return list;
   }

   public static void remove(PlanFixVersion planFixVersion) {
      list.remove(planFixVersion);
      notifyListenersFixVersionHasBeenRemoved();
   }

   public static void removeListener(PlanFixVersionListener listener) {
      listeners.remove(listener);
   }

   private static void notifyListenersFixVersionHasBeenRemoved() {
      for (PlanFixVersionListener listener : listeners) {
         listener.planFixVersionRemoved();
      }
   }

   public String getText() {
      return text;
   }

   public void setProject(JiraProject project) {
      this.project = project;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append(text).append(" <").append(project).append(">");
      return sb.toString();
   }

   private void notifyListenersFixVersionHasBeenAdded(PlanFixVersion planFixVersion, JiraProject project) {
      for (PlanFixVersionListener listener : listeners) {
         listener.planFixVersionAdded(planFixVersion, project);
      }
   }
}
