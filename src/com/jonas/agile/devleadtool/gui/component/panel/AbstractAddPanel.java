package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Enumeration;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.jonas.agile.devleadtool.gui.component.TableRadioButton;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.swing.MyPanel;

public abstract class AbstractAddPanel extends MyPanel {

   private ButtonGroup group;
   private JTextField jiraPrefix;
   private JTextArea jiraCommas;
   private JTextField defaultRelease;
   private JComboBox statusCombo;
   
   public ButtonGroup getTablesButtonGroup() {
      return group;
   }

   public JTextField getJiraPrefixTextField() {
      return jiraPrefix;
   }

   public JTextArea getJiraCommasTextField() {
      return jiraCommas;
   }

   public JTextField getDefaultReleaseTextField() {
      return defaultRelease;
   }

   public JComboBox getStatusComboBox() {
      return statusCombo;
   }

   public AbstractAddPanel(BorderLayout borderLayout, Frame frame, MyTable... tables) {
      super(borderLayout);
      
      
      MyPanel panel = new MyPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();

      c.insets = new Insets(5, 5, 5, 5);
      c.fill = GridBagConstraints.BOTH;
      c.gridy = -1;

      if (tables.length > 0) {
         setNewRow(c);
         panel.add(new JLabel("Table:"), c);
         set2ndCol(c);
         panel.add(getTableRadios(tables), c);
      }

      setNewRow(c);
      JLabel prefixLabel = addLabel(panel, "Prefix:", c);
      set2ndCol(c);
      jiraPrefix = panel.addTextField(panel, 10, c, prefixLabel, 'p');

      setNewRow(c);
      JLabel releaseLabel = addLabel(panel, "Release:", c);
      set2ndCol(c);
      defaultRelease = panel.addTextField(panel, 10, c, releaseLabel, 'r');

      setNewRow(c);
      JLabel statusLabel = addLabel(panel, "Status:", c);
      set2ndCol(c);
      statusCombo = panel.addComboBox(panel, BoardStatusValue.values(), c, statusLabel, 's');

      setNewRow(c);
      c.fill = GridBagConstraints.NONE;
      c.anchor = GridBagConstraints.NORTHWEST;
      JLabel numberLabel = addLabel(panel, "Numbers:", c);
      set2ndCol(c);
      c.fill = GridBagConstraints.BOTH;
      c.weighty = 1;
      jiraCommas = panel.addTextArea(panel, 4, 10, c, numberLabel, 'n');
      jiraCommas.setLineWrap(true);
      jiraCommas.setWrapStyleWord(true);

      this.add(panel, BorderLayout.CENTER);
      this.add(getButtonPanel(), BorderLayout.PAGE_END);

//      setRequestFocusEnabled(true);
   }
   
   private void set2ndCol(GridBagConstraints c) {
      c.gridx = 1;
      c.weightx = 1;
   }

   private Component getTableRadios(MyTable... tables) {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      group = new ButtonGroup();

      for (MyTable myTable : tables) {
         TableRadioButton tableRadioButton = new TableRadioButton(myTable.getTitle(), myTable);
         tableRadioButton.setMnemonic(myTable.getTitle().charAt(0));
         group.add(tableRadioButton);
         panel.add(tableRadioButton);
      }

      return panel;
   }
   
   private void setNewRow(GridBagConstraints c) {
      c.gridx = 0;
      c.gridy++;
      c.weightx = 0;
   }

   protected abstract Component getButtonPanel();

   public void focusPrefix() {
      getJiraPrefixTextField().requestFocus();
   }

   public void setTarget(MyTable target) {
      Enumeration<?> elements = getTablesButtonGroup().getElements();
      while (elements.hasMoreElements()) {
         TableRadioButton button = (TableRadioButton) elements.nextElement();
         if (button.getTable().equals(target)) {
            button.setSelected(true);
            return;
         }
      }
   }
}
