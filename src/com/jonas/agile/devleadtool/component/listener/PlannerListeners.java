package com.jonas.agile.devleadtool.component.listener;

import java.util.ArrayList;
import java.util.List;
import com.jonas.agile.devleadtool.component.MyInternalFrame;

public class PlannerListeners {
   
   private final static List<PlannerListener> listeners = new ArrayList<PlannerListener>();
   
   public static void addListener(PlannerListener listener){
      listeners.add(listener);
   }
   
   public static void removeListener(PlannerListener listener){
      listeners.remove(listener);
   }
   
   public static void notifyListenersThatFrameWasCreated(MyInternalFrame internalFrame) {
      for (PlannerListener listener : listeners) {
         listener.frameWasCreated(internalFrame);
      }
   }
   
   public static void notifyListenersThatFrameWasClosed(MyInternalFrame internalFrame) {
      for (PlannerListener listener : listeners) {
         listener.frameWasClosed(internalFrame);
      }
   }

   public static void notifyListenersThatFrameChangedTitle(MyInternalFrame internalFrame) {
      for (PlannerListener listener : listeners) {
         listener.frameChangedTitle(internalFrame);
      }
   }

}
