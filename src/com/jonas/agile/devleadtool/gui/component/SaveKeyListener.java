/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.dialog.SavePlannerDialog;

public class SaveKeyListener extends KeyAdapter {
   private PlannerHelper helper;
   private boolean pressed = false;
   private SavePlannerDialog savePlannerDialog;

   public SaveKeyListener(PlannerHelper helper, SavePlannerDialog savePlannerDialog) {
      super();
      this.helper = helper;
      this.savePlannerDialog = savePlannerDialog;
   }

   @Override
   public void keyPressed(KeyEvent e) {
      MyInternalFrame.log.debug("Pressed: KeyCode : " + e.getKeyCode() + " ModifiersEx : " + e.getModifiersEx() + " Pressed : " + pressed);

      if (!pressed && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK) {
         if (e.getKeyCode() == KeyEvent.VK_S) {
            pressed = true;
            MyInternalFrame.log.debug("***");
            savePlannerDialog.save(helper.getActiveInternalFrame(), false);
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      // log.debug("Released: KeyCode : " + e.getKeyCode() + " ModifiersEx : " + e.getModifiersEx() + " Pressed : " + pressed);
      if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_CONTROL) {
         // log.debug("Release!");
         pressed = false;
      }
   }
}