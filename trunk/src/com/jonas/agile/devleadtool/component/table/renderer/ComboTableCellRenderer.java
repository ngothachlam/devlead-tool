package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.Serializable;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class ComboTableCellRenderer extends JPanel implements TableCellRenderer {

   private static final Logger log = MyLogger.getLogger(ComboTableCellRenderer.class);

   private JComboBox combo;

   public ComboTableCellRenderer() {
      super(new BorderLayout());
      // FIXME hardcoded!!
      JiraVersion[] fixVersions = JiraProject.getProjectByKey("LLU").getFixVersions(false);
      combo = new JComboBox(fixVersions);
      this.add(combo, SwingUtilities.CENTER);
      updateFixVersionsAvailable();
   }

   @SuppressWarnings("unchecked")
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

      log.debug("Combo for column: " + column + " with value: " + value + " (class: " + debugClassOfValue(value) + ")");
      setFont(table.getFont());

      if (hasFocus)
         combo.setBackground(SwingUtil.getTableCellFocusBackground());
      else if (isSelected)
         combo.setBackground(table.getSelectionBackground());
      else
         combo.setBackground(table.getBackground());

      if (hasFocus) {
         setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
      } else {
         setBorder(UIManager.getBorder("Table.focusSelectedCellHighlightBorder"));
      }

      if (!table.isCellEditable(row, column)) {
         combo.setEnabled(false);
      } else
         combo.setEnabled(true);

      setComboValue((List<JiraVersion>) value);
      return this;
   }

   private void setComboValue(List<JiraVersion> jiraVersions) {
      if (jiraVersions.size() > 1) {
         throw new RuntimeException("Cannot handle multiple Fix versions yet!!");
      }
      for (int i = 0; i < jiraVersions.size(); i++) {
         JiraVersion fixVersion = jiraVersions.get(i);
         combo.setSelectedItem(fixVersion);
      }
   }

   public void setBorder(Border border) {
      super.setBorder(border);
   }

   private Serializable debugClassOfValue(Object value) {
      return (value != null ? value.getClass() : "null");
   }

   private void updateFixVersionsAvailable() {
      // FIXME : LLU Hardcoded!!!
      JiraVersion[] versions = JiraProject.getProjectByKey("LLU").getFixVersions(false);
      for (JiraVersion jiraVersion : versions) {
         combo.addItem(jiraVersion);
      }
   }


}
