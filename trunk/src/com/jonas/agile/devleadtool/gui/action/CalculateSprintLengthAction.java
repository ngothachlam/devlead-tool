package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Date;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.sprint.SprintLengthCalculationTarget;
import com.jonas.agile.devleadtool.sprint.SprintLengthSource;
import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class CalculateSprintLengthAction extends BasicAbstractGUIAction {

   private SprintLengthSource lengthSource;
   private SprintLengthCalculationTarget lengthTarget;
   private static final Logger log = MyLogger.getLogger(CalculateSprintLengthAction.class);

   public CalculateSprintLengthAction(Frame parentFrame) {
      super("Calculate", "Calculate Sprint Length based on start and end", parentFrame);
   }
   @Override
   public void doActionPerformed(ActionEvent e) {
      int days = getWorkingDaysBetweenStartAndEnd(lengthSource);
      lengthTarget.setWorkingDays(days);
   }

   public void setLengthSource(SprintLengthSource lengthSource) {
      this.lengthSource = lengthSource;
   }

   public void setLengthTarget(SprintLengthCalculationTarget lengthTarget) {
      this.lengthTarget = lengthTarget;
   }

   int getWorkingDaysBetweenStartAndEnd(SprintLengthSource source) {
      Date startDate = source.getStart();
      Date endDate = source.getEnd();
      
      return DateHelper.getWorkingDaysBetween(startDate, endDate);
   }
}
