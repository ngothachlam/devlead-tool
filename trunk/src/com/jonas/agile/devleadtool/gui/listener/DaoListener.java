package com.jonas.agile.devleadtool.gui.listener;

import com.jonas.agile.devleadtool.data.DaoListenerEvent;

public interface DaoListener {

   public void notify(DaoListenerEvent event, String message);

}
