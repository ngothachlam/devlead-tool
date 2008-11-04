package com.jonas.agile.devleadtool.component.panel;

import javax.swing.ListSelectionModel;

public interface SelectionModifier {

   public void clearSelection();
//   public ListSelectionModel getSelectionModel();
   public boolean addSelection(String key);
   public void scrollToSelection();

}
