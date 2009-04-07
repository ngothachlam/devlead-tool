package com.jonas.testing;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

public class GenericAction extends AbstractAction {
   protected Method methodToCall;
   protected final static Class[] signature = new Class[] { ActionEvent.class };
   protected Object target;
   protected Object[] params;

   public GenericAction(Object obj, String methodName, String name, Icon icon) {
      this(obj, methodName, name, icon, null);
   }

   public GenericAction(Object obj, String methodName, String name, Icon icon, String shortDescription) {
      super(name, icon);
      try {
         methodToCall = obj.getClass().getMethod(methodName, signature);
      } catch (Exception e) {
         e.printStackTrace();
      }
      target = obj;

      if (shortDescription != null)
         putValue(Action.SHORT_DESCRIPTION, shortDescription);
   }

   public void actionPerformed(ActionEvent ae) {
      if (params == null)
         params = new Object[1];
      params[0] = ae;
      try {
         methodToCall.invoke(target, params);
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      }
   }

}
