package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import com.jonas.agile.devleadtool.sprint.SprintCreationTarget;

public class AddSprintAction extends BasicAbstractGUIAction {

   private final SprintCreationTarget target;
   private final SprintCreationSource source;

   public AddSprintAction(Frame parentFrame, SprintCreationSource source, SprintCreationTarget target) {
      super("Add Sprint", "Adding a sprint to cache!", parentFrame);
      this.source = source;
      this.target = target;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {

      String name = source.getName();
      Date startDate = source.getStart();
      Date endDate = source.getEnd();
      int length = source.getLength();

      Sprint sprint = new Sprint(name, startDate, endDate, length);
      System.out.println("Adding: " + sprint);

      try {
         target.addSprint(sprint);
      } catch (IOException e1) {
         throw new RuntimeException(e1);
      }
   }

}
