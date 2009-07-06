package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.swing.MyComponentPanel;

public abstract class MyDataPanel extends MyComponentPanel {

   protected MyTable table;

   public MyDataPanel(LayoutManager borderLayout) {
      super(borderLayout);
   }

   protected Component getFilterPanel() {
      JXPanel panel = new JXPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.insets = new Insets(1, 2, 0, 0);
      addLabel(panel, "Filter: ", gbc);

      gbc.insets = new Insets(1, 0, 0, 0);
      gbc.gridx++;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 0.5;
      JTextField filterTextField = addTextField(panel, 10, gbc);

      PatternFilter patternFilter = new PatternFilterAcrossAllColumns(filterTextField.getText(), 0, table);
      
      Filter[] filterArray = { patternFilter };
      FilterPipeline filters = new FilterPipeline(filterArray);
      table.setFilters(filters);
      
      RefreshFilterKeyListener listener = new RefreshFilterKeyListener(patternFilter, filterTextField);
      filterTextField.addKeyListener(listener);
      
      return panel;
   }

   public MyTable getTable() {
      return table;
   }

   public MyTableModel getModel() {
      return table.getMyModel();
   }

   public void setEditable(boolean selected) {
      table.getMyModel().setEditable(selected);
   }
}

class RefreshFilterKeyListener extends KeyAdapter implements Resetter{

   private final PatternFilter patternFilter;
   private final JTextField filterTextField;

   public RefreshFilterKeyListener(PatternFilter patternFilter, JTextField filterTextField) {
      this.patternFilter = patternFilter;
      this.filterTextField = filterTextField;
   }

   @Override
   public void keyReleased(KeyEvent e) {
      JTextField textField = (JTextField) e.getSource();
      patternFilter.setPattern(textField.getText(), Pattern.CASE_INSENSITIVE);
   }

   public void resetPattern() {
      patternFilter.setPattern("", Pattern.CASE_INSENSITIVE);
      filterTextField.setText("");
   }
}

interface Resetter {

   abstract void resetPattern();
   
}


class PatternFilterAcrossAllColumns extends PatternFilter {
   private final MyTable table;

   public PatternFilterAcrossAllColumns(String searchText, int matchFlags, MyTable table) {
      super(searchText, matchFlags, 0); // Default to first column for ease.
      this.table = table;

   }

   @Override
   public boolean test(int row) {
      Pattern thePattern = getPattern();
      if (thePattern == null)
         return false;

      for (int i = 0; i < table.getColumnCount(); i++) {
         String text = getInputString(row, i);
         if (text == null || text.length() == 0)
            continue;

         if (thePattern.matcher(".*" + text + ".*").find())
            return true;
      }

      return false;
   }
}
