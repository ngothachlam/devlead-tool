package com.jonas.agile.devleadtool.gui.listener;

import com.jonas.agile.devleadtool.data.PlanFixVersion;
import com.jonas.jira.JiraProject;

public interface PlanFixVersionListener {

   public void planFixVersionRemoved();

   public void planFixVersionAdded(PlanFixVersion fixVersion, JiraProject project);

}
