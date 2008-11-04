package com.jonas.agile.devleadtool.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingWorker;

public abstract class SwingWorkerCompletionWaiter implements PropertyChangeListener {

   @Override
   public void propertyChange(PropertyChangeEvent event) {
      if ("state".equals(event.getPropertyName()) && SwingWorker.StateValue.DONE == event.getNewValue()) {
         done();
      }
   }

   abstract public void done();

}
