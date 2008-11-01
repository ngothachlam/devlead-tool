/**
 * 
 */
package com.jonas.agile.devleadtool.component;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.data.PlannerDAO;

public class SaveKeyListener implements KeyListener {
   private final PlannerDAO dao;
   private Component frame;
   private PlannerHelper helper;
   private boolean pressed = false;

   public SaveKeyListener(PlannerDAO dao, Component frame, PlannerHelper helper) {
      super();
      this.dao = dao;
      this.frame = frame;
      this.helper = helper;
   }

   @Override
   public void keyPressed(KeyEvent e) {
      MyInternalFrame.log.debug("Pressed: KeyCode : " + e.getKeyCode() + " ModifiersEx : " + e.getModifiersEx() + " Pressed : " + pressed);

      if (!pressed && (e.getModifiersEx() & e.CTRL_DOWN_MASK) == e.CTRL_DOWN_MASK) {
         if (e.getKeyCode() == e.VK_S) {
            pressed = true;
            MyInternalFrame.log.debug("***");
            new SavePlannerDialog(dao, helper.getParentFrame(), helper.getActiveInternalFrame(), false);
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      // log.debug("Released: KeyCode : " + e.getKeyCode() + " ModifiersEx : " + e.getModifiersEx() + " Pressed : " + pressed);
      if (e.getKeyCode() == e.VK_S || e.getKeyCode() == e.VK_CONTROL) {
         // log.debug("Release!");
         pressed = false;
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }
}