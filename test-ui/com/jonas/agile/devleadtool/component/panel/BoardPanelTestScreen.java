package com.jonas.agile.devleadtool.component.panel;

import javax.swing.JFrame;
import com.jonas.agile.devleadtool.gui.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.gui.component.panel.MyDataPanel;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.testHelpers.TryoutTester;

public class BoardPanelTestScreen {

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getEmptyFrame();
      SprintCache sprintCache = new SprintCache();
      MyDataPanel panel = new BoardPanel(sprintCache);
      frame.setContentPane(panel);
      
      setTestData(panel, sprintCache);
      
      frame.pack();
      frame.setVisible(true);
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
