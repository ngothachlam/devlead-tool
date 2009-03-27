/**
 * 
 */
package com.jonas.agile.devleadtool.controller.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.common.logging.MyLogger;

public class SaveKeyListener extends KeyAdapter {
   private PlannerHelper helper;
   private boolean pressed = false;
   private SavePlannerDialog savePlannerDialog;
   private Logger log = MyLogger.getLogger(SaveKeyListener.class);

   public SaveKeyListener(PlannerHelper helper, SavePlannerDialog savePlannerDialog) {
      super();
      this.helper = helper;
      this.savePlannerDialog = savePlannerDialog;
   }

   @Override
   public void keyPressed(KeyEvent e) {
      log.debug("Pressed: KeyCode : " + e.getKeyCode() + " ModifiersEx : " + e.getModifiersEx() + " Pressed : " + pressed);

      if (!pressed && (e.getModifiersEx() & e.CTRL_DOWN_MASK) == e.CTRL_DOWN_MASK) {
         if (e.getKeyCode() == e.VK_S) {
            pressed = true;
            log.debug("***");
            savePlannerDialog.save(helper.getActiveInternalFrame(), false);
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == e.VK_S || e.getKeyCode() == e.VK_CONTROL) {
         pressed = false;
      }
   }
}