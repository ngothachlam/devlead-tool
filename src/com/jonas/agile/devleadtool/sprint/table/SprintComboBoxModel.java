package com.jonas.agile.devleadtool.sprint.table;

import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;

public class SprintComboBoxModel extends AbstractListModel implements ComboBoxModel {

   private Vector<Sprint> vector;
   private Object selectedItem;

   public SprintComboBoxModel() {
      vector = SprintCache.getInstance().getSprints();
   }

   @Override
   public Object getSelectedItem() {
      return selectedItem;
   }

   @Override
   public void setSelectedItem(Object anItem) {
      selectedItem = anItem;
   }

   @Override
   public Object getElementAt(int index) {
      if (index == 0)
         return null;
      return vector.get(index - 1);
   }

   @Override
   public int getSize() {
      return vector.size() + 1;
   }
}
