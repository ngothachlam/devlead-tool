package com.jonas.agile.devleadtool.sprint.table;

import java.awt.Color;
import java.awt.Component;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.SprintTime;
import com.jonas.common.ColorUtil;
import com.jonas.common.swing.SwingUtil;

public class SprintsTableHighlighter extends AbstractHighlighter {

   private final JXTable sprintsTable;
   private SprintCache sprintCache;

   public SprintsTableHighlighter(JXTable sprintsTable) {
      this.sprintsTable = sprintsTable;
   }

   @Override
   protected Component doHighlight(Component component, ComponentAdapter adapter) {
      if (sprintCache == null) {
         return component;
      }
      int row = adapter.row;

      int modelRow = sprintsTable.convertRowIndexToModel(row);
      Sprint sprintFromRow = sprintCache.getSprintFromRow(modelRow);
      SprintTime calculateTime = sprintFromRow.calculateTime();
      if (calculateTime == SprintTime.currentSprint) {
         Color color = SwingUtil.cellLightBlue;
         if(adapter.isSelected()){
            color = ColorUtil.darkenColor(color, 25);
         }
         component.setBackground(color);
      } else if (calculateTime == SprintTime.beforeCurrentSprint) {
         Color color = SwingUtil.cellLightGreen;
         if(adapter.isSelected()){
            color = ColorUtil.darkenColor(color, -35);
         }
         component.setBackground(color);
      }
      
      

      return component;
   }

   public void setSprintCache(SprintCache sprintCache) {
      this.sprintCache = sprintCache;
   }

}
