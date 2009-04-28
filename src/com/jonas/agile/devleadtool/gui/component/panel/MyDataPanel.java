package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.Color;
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
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.decorator.SearchPredicate;
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

      gbc.gridx++;
      gbc.weightx = 0;
      gbc.insets = new Insets(1, 3, 0, 0);
      addLabel(panel, "Highlight: ", gbc);

      gbc.insets = new Insets(1, 0, 0, 0);
      gbc.gridx++;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 0.5;
      JTextField highlightTextField = addTextField(panel, 10, gbc);

      PatternFilter patternFilter = new PatternFilterAcrossAllColumns(filterTextField.getText(), 0, table);
      
      Filter[] filterArray = { patternFilter };
      FilterPipeline filters = new FilterPipeline(filterArray);
      table.setFilters(filters);
      
      ColorHighlighter matchHighlighter = new ColorHighlighter(HighlightPredicate.NEVER, null, Color.MAGENTA);
      table.addHighlighter(matchHighlighter);
      
      RefreshFilterKeyListener listener = new RefreshFilterKeyListener(patternFilter, filterTextField);
      RefreshSearchKeyListener listener2 = new RefreshSearchKeyListener(matchHighlighter, highlightTextField);
      filterTextField.addKeyListener(listener);
      highlightTextField.addKeyListener(listener2);
      
      gbc.gridx++;
      gbc.weightx = 0;
      gbc.insets = new Insets(1, 1, 0, 0);
      addButton(panel, "Clear", new ClearAction("Clear", listener, listener2), gbc);
      

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

class ClearAction extends AbstractAction{

   private final Resetter[] resetters;

   public ClearAction(String name, Resetter... resetters) {
      super(name);
      this.resetters = resetters;
      putValue(Action.SHORT_DESCRIPTION, name);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      for (Resetter resetter : resetters) {
         resetter.setPattern("");
      }
   }
   
}

class RefreshSearchKeyListener extends KeyAdapter implements Resetter {

   private AbstractHighlighter matchHighlighter;
   private final JTextField highlightTextField;

   public RefreshSearchKeyListener(AbstractHighlighter matchHighlighter, JTextField textField) {
      this.matchHighlighter = matchHighlighter;
      this.highlightTextField = textField;
   }

   @Override
   public void keyReleased(KeyEvent e) {
      JTextField textField = (JTextField) e.getSource();
      Pattern pattern = Pattern.compile(textField.getText(), Pattern.CASE_INSENSITIVE); 
      HighlightPredicate predicate = new SearchPredicate(pattern, -1, -1);
      matchHighlighter.setHighlightPredicate(predicate);
   }
   
   public void setPattern(String text) {
      HighlightPredicate predicate = new SearchPredicate(text, -1, -1);
      matchHighlighter.setHighlightPredicate(predicate);
      highlightTextField.setText(text);
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

   public void setPattern(String text) {
      patternFilter.setPattern(text, Pattern.CASE_INSENSITIVE);
      filterTextField.setText("");
   }
}

interface Resetter {

   abstract void setPattern(String text);
   
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
