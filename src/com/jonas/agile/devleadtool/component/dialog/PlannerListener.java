package com.jonas.agile.devleadtool.component.dialog;

import com.jonas.agile.devleadtool.component.MyInternalFrame;

public interface PlannerListener {

   public void frameWasCreated(MyInternalFrame internalFrame);
   public void frameWasClosed(MyInternalFrame internalFrame);
   public void frameChangedTitle(MyInternalFrame internalFrame);

}
