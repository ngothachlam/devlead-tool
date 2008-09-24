package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.ClosePlannerDialog;

public final class MainFrameListener extends WindowAdapter {
   private JFrame frame;
   private final PlannerHelper plannerHelper;

   public MainFrameListener(JFrame frame, PlannerHelper plannerHelper) {
      super();
      this.frame = frame;
      this.plannerHelper = plannerHelper;
   }

   public void windowClosing(WindowEvent e) {
      new ClosePlannerDialog(frame, plannerHelper);
   }
}
