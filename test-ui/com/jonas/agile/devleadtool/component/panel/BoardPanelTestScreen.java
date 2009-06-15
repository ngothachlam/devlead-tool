package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.action.SprintManagerGuiAction;
import com.jonas.agile.devleadtool.gui.component.MyInternalFrame;
import com.jonas.agile.devleadtool.gui.component.panel.MyDataPanel;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.testHelpers.TryoutTester;

public class BoardPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getEmptyFrame();
      PlannerHelper helper = new PlannerHelper(null, "planner helper");
      MyInternalFrame internalFrame = new MyInternalFrame(null, null, null, null, null, null, null);
      helper.setActiveInternalFrame(internalFrame);
      SprintManagerGuiAction panel = new SprintManagerGuiAction(frame, helper, null);
      panel.doActionPerformed(null);
   }

   private static void setTestData(MyDataPanel panel, SprintCache sprintCache) {
      panel.getModel().addJira("LLU-1");
      panel.getModel().addJira("LLU-2");
      panel.getModel().addJira("LLU-3");
      panel.getModel().addJira("LLU-4");
      panel.getModel().addJira("LLU-11");
      panel.getModel().addJira("LLU-12");
      panel.getModel().addJira("LLU-13");
      panel.getModel().addJira("LLU-14");
      
      addSprint(sprintCache, "1-1");
      addSprint(sprintCache, "1-2");
      addSprint(sprintCache, "1-3");
   }

   private static void addSprint(SprintCache sprintCache, String sprintName) {
      Sprint sprint = new Sprint(sprintName, null, null, 1);
      sprintCache.cache(sprint, false);
   }
}
