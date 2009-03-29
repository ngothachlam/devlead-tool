package com.jonas.agile.devleadtool.gui.listener;

import com.jonas.agile.devleadtool.gui.component.MyInternalFrame;

public interface PlannerListener {

   public void frameWasCreated(MyInternalFrame internalFrame);
   public void frameWasClosed(MyInternalFrame internalFrame);
   public void frameChangedTitle(MyInternalFrame internalFrame);

}
