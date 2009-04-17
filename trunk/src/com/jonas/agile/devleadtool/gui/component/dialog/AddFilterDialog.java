package com.jonas.agile.devleadtool.gui.component.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import com.jonas.agile.devleadtool.gui.component.TableRadioButton;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.swing.MyPanel;
import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.access.JiraClient;

public class AddFilterDialog extends JFrame {

   public AddFilterDialog(Window frame, MyTable... tables) {
      super();
      this.setContentPane(new AddFilterPanel(this, tables));
      this.pack();

      SwingUtil.centreWindowWithinWindow(this, frame);
      setVisible(true);
   }

   private class AddFilterPanel extends MyPanel {
      private ButtonGroup group;

      public AddFilterPanel(final JFrame frame, MyTable... tables) {
         super(new BorderLayout());
         MyPanel panel = new MyPanel(new GridBagLayout());
         GridBagConstraints c = new GridBagConstraints();

         c.insets = new Insets(5, 5, 5, 5);
         c.fill = GridBagConstraints.BOTH;
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
         jiraCommas.setSelectedItem(JiraFilter.DevsupportPrioFilter_UnClosed);

         MyPanel buttonPanel = new MyPanel(new GridLayout(1, 2, 5, 5));
         buttonPanel.bordered();
         this.addButton(buttonPanel, "Add", new AddFilterFromRadioButtons(group, jiraCommas, frame));
         this.addButton(buttonPanel, "Close", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               frame.dispose();
            }
         });

         this.add(panel, BorderLayout.CENTER);
         this.add(buttonPanel, BorderLayout.PAGE_END);
      }

      private Component getTableRadios(MyTable... tables) {
         JPanel panel = new JPanel();
         panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
         group = new ButtonGroup();

         for (MyTable myTable : tables) {
            TableRadioButton tableRadioButton = new TableRadioButton(myTable.getTitle(), myTable);
            group.add(tableRadioButton);
            panel.add(tableRadioButton);
         }

         return panel;
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
   }

   private class AddFilterFromRadioButtons extends SwingWorker<JiraIssue[], String> implements ActionListener {

      private final ButtonGroup group;
      private final JComboBox filters;
      private final JFrame parentFrame;

      private MyTable table;
      private JiraFilter filter;
      private ProgressDialog progressDialog;

      public AddFilterFromRadioButtons(ButtonGroup group, JComboBox filters, JFrame frame) {
         this.group = group;
         this.filters = filters;
         this.parentFrame = frame;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         progressDialog = new ProgressDialog(this.parentFrame, "Getting Jiras from Filter!", "Getting Jiras!", 7, true);
         table = getTableToAddJirasTo();
         if (table == null) {
            progressDialog.setCompleteWithDelay(300);
            AlertDialog.alertMessage(parentFrame, "Select a table first!");
            return;
         }
         filter = ((JiraFilter) filters.getSelectedItem());
         try {
            this.execute();
         } catch (Throwable e1) {
            AlertDialog.alertException(parentFrame, e1);
         }
      }

      @Override
      protected void done() {
         super.done();
         JiraIssue[] jiras = null;
         try {
            jiras = get();
         } catch (Throwable e) {
            if (!isCancelled()) {
               AlertDialog.alertException(parentFrame, e);
               return;
            }
            return;
         }
         int i = 0;
         publish("Adding Jiras to table!");
         for (JiraIssue jiraIssue : jiras) {
            table.addJiraAndMarkIfNew(jiraIssue);
            i++;
         }
         publish(i + "Jiras Added to table finished!");
         publish("Done!");
         parentFrame.dispose();
      }

      @Override
      public JiraIssue[] doInBackground() {
         JiraClient client = filter.getProject().getJiraClient();
         try {
            publish("Logging into Jira!");
            client.login();
            publish("Logged in!");
            publish("Downloading Jiras!");
            JiraIssue[] jirasFromFilter = client.getJirasFromFilter(filter);
            publish("Jiras downloaded!");
            return jirasFromFilter;
         } catch (Throwable e) {
            AlertDialog.alertException(parentFrame, e);
         }
         return null;
      }

      @Override
      protected void process(List<String> chunks) {
         progressDialog.setNote(chunks.get(chunks.size() - 1));
      }

      private MyTable getTableToAddJirasTo() {
         MyTable table = null;
         Enumeration<AbstractButton> elements = group.getElements();
         while (elements.hasMoreElements()) {
            TableRadioButton button = (TableRadioButton) elements.nextElement();
            if (button.isSelected()) {
               table = button.getTable();
            }
         }
         return table;
      }
   }
}
