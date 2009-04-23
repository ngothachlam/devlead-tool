package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import com.jonas.agile.devleadtool.sprint.SprintCreationTarget;

public class AddSprintAction extends BasicAbstractGUIAction {

   private static final Set<SprintCacheListener> listeners = new HashSet<SprintCacheListener>();

   private SprintCreationSource source;
   private SprintCreationTarget target;

   public AddSprintAction(Frame parentFrame, SprintCreationSource source, SprintCreationTarget target) {
      super("Add Sprint", "Adding sprint to cache!", parentFrame);
      this.source = source;
      this.target = target;
   }

   AddSprintAction() {
      super("Add Sprint", "Adding sprint to cache!", null);
   }

   public void addListener(SprintCacheListener listener) {
      listeners.add(listener);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      notifyListeners(SprintCacheNotification.ADDINGTOCACHE);
      try {
         String name = source.getName();
         Date startDate = source.getStart();
         Date endDate = source.getEnd();
         Integer length = source.getLength();

         if(startDate == null || endDate == null || length == null || name == null){
            notifyListeners(SprintCacheNotification.REQUIREDFIELDMISSING);
            return;
         }
         
         Sprint sprint = new Sprint(name, startDate, endDate, length);
         try {
            target.addSprint(sprint);
         } catch (IOException e1) {
            throw new RuntimeException(e1);
         }
      } finally {
         notifyListeners(SprintCacheNotification.ADDEDTOCACHE);
      }
   }

   public void notifyListeners(SprintCacheNotification notification) {
      for (SprintCacheListener listener : listeners) {
         listener.notify(notification);
      }
   }

   public void removeListener(SprintCacheListener listener) {
      listeners.remove(listener);
   }
}


interface SprintCacheListener {

   public void notify(SprintCacheNotification notification);

}


enum SprintCacheNotification {
   ADDEDTOCACHE, ADDINGTOCACHE, REQUIREDFIELDMISSING
}