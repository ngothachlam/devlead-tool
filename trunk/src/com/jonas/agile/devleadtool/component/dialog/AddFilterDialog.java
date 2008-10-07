package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import com.jonas.agile.devleadtool.component.TableRadioButton;
import com.jonas.agile.devleadtool.component.dnd.TableAndTitleDTO;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;

public class AddFilterDialog extends JFrame {

   public AddFilterDialog(Window frame, TableAndTitleDTO... tables) {
      super();
      this.setContentPane(new AddFilterPanel(this, tables));
      this.pack();

      SwingUtil.centreWindowWithinWindow(this, frame);
      setVisible(true);
   }
}


class AddFilterPanel extends MyPanel {
   private ButtonGroup group;

   public AddFilterPanel(final JFrame frame, TableAndTitleDTO... tables) {
      super(new BorderLayout());
      MyPanel panel = new MyPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();

      c.insets = new Insets(5, 5, 5, 5);
      c.fill = c.BOTH;
      c.gridy = -1;
      setNewRow(c);
      panel.add(new JLabel("Table:"), c);
      set2ndCol(c);
      panel.add(getTableRadios(tables), c);

      setNewRow(c);
      panel.add(new JLabel("Numbers:"), c);
      set2ndCol(c);
      c.weighty = 1;
      Object[] filters = new Object[JiraFilter.getFilters().size()];
      for (int i = 0; i < filters.length; i++) {
         filters[i] = JiraFilter.getFilters().get(i);
      }
      JComboBox jiraCommas = panel.addComboBox(panel, filters, c);

      MyPanel buttonPanel = new MyPanel(new GridLayout(1, 2, 5, 5));
      buttonPanel.bordered();
      this.addButton(buttonPanel, "Add", new AddFilterFromRadioButtons(frame, group, jiraCommas));
      this.addButton(buttonPanel, "Close", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            frame.dispose();
         }
      });

      this.add(panel, BorderLayout.CENTER);
      this.add(buttonPanel, BorderLayout.PAGE_END);
   }

   private void set2ndCol(GridBagConstraints c) {
      c.gridx = 1;
      c.weightx = 1;
   }

   private void setNewRow(GridBagConstraints c) {
      c.gridx = 0;
      c.gridy++;
      c.weightx = 0;
   }

   private Component getTableRadios(TableAndTitleDTO... tables) {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      group = new ButtonGroup();

      for (TableAndTitleDTO myTable : tables) {
         TableRadioButton tableRadioButton = new TableRadioButton(myTable.getTitle(), myTable.getTable());
         group.add(tableRadioButton);
         panel.add(tableRadioButton);
      }

      return panel;
   }
}


class AddFilterFromRadioButtons implements ActionListener {

   private ButtonGroup group;
   private final JComboBox filters;

   public AddFilterFromRadioButtons(JFrame frame, ButtonGroup group, JComboBox filters) {
      this.group = group;
      this.filters = filters;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      MyTable table = null;
      Enumeration elements = group.getElements();
      while (elements.hasMoreElements()) {
         TableRadioButton button = (TableRadioButton) elements.nextElement();
         if (button.isSelected()) {
            table = button.getTable();
         }
      }

      if (table == null)
         return;

      JiraFilter filter = ((JiraFilter) filters.getSelectedItem());

      final JiraClient client = filter.getProject().getJiraClient();

      JiraIssue[] jiras = null;
      try {
         client.login();
         jiras = client.getJirasFromFilter();
         // FIXME the following won't catch errors!
      } catch (HttpException e1) {
         e1.printStackTrace();
         return;
      } catch (IOException e1) {
         e1.printStackTrace();
         return;
      } catch (JiraException e1) {
         e1.printStackTrace();
         return;
      } catch (JDOMException e1) {
         e1.printStackTrace();
         return;
      }
      for (JiraIssue jiraIssue : jiras) {
         table.addJira(jiraIssue);
      }
   }
}
