package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.listener.CopyToTableListener;
import com.jonas.agile.devleadtool.component.listener.DestinationRetriever;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.component.listener.TabCheckButtonActionListener;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.MyPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

public class BoardPanel extends MyComponentPanel {

   private PlannerHelper helper;
   private Logger log = MyLogger.getLogger(BoardPanel.class);
   private TableRowSorter<TableModel> sorter;
   public MyTable table;

   BoardPanel() {
      super(null);
   }

   public BoardPanel(PlannerHelper client) {
      this(client, new BoardTableModel());
   }

   public BoardPanel(PlannerHelper helper, MyTableModel boardModel) {
      super(new BorderLayout());
      this.helper = helper;
      makeContent(boardModel);
      setButtons();
      initialiseTableHeader();
   }

   private JPanel getButtonPanelNorth() {
      JPanel buttonPanel = new JPanel();
      addFilter(buttonPanel, table, sorter, ColumnDataType.Jira, ColumnDataType.Description);
      return buttonPanel;
   }

   private JPanel getButtonPanelSouth() {
      JPanel buttonPanel = new JPanel();

      addPanelWithAddAndRemoveOptions(table, buttonPanel);
      SyncWithJiraActionListener listener = new SyncWithJiraActionListener(table, helper);
      listener.addListener(new SyncWithJiraActionListenerListener() {
         public void jiraAdded(JiraIssue jiraIssue) {
         }

         public void jiraSynced(JiraIssue jira, int tableRowSynced) {
            table.setValueAt(jira.getSummary(), tableRowSynced, ColumnDataType.Description);
            table.setValueAt(jira.getLLUListPriority(), tableRowSynced, ColumnDataType.ListPrio);
         }

         public void jiraSyncedCompleted() {
         }
      });
      addButton(buttonPanel, "Sync", listener);
      addButton(buttonPanel, "Open Jiras", new OpenJirasListener(table, helper));
      addButton(buttonPanel, "Unsort", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // table.clearSorting();
         }
      });
      addButton(buttonPanel, "Copy to Plan", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getPlanTable();
         }
      }, helper));
      addButton(buttonPanel, "Copy to Jira", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getJiraTable();
         }
      }, helper));
      addButton(buttonPanel, "TabCheck", new TabCheckButtonActionListener(helper, table, helper.getPlannerCommunicator()));
      return buttonPanel;
   }

   public MyTableModel getModel() {
      return ((MyTableModel) table.getModel());
   }

   public MyTable getTable() {
      return table;
   }

   private void initialiseTableHeader() {
      JTableHeader header = table.getTableHeader();
      header.setReorderingAllowed(true);
   }

   protected void makeContent(MyTableModel boardTableModel) {
      table = new MyTable(boardTableModel);

      sorter = new TableRowSorter<TableModel>(table.getModel());
      table.setRowSorter(sorter);

      table.setDefaultRenderer(String.class, new StringTableCellRenderer());
      table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());
      table.setDefaultEditor(Boolean.class, new CheckBoxTableCellEditor());

      JScrollPane scrollPane = new MyScrollPane(table);

      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
      addCenter(scrollPane);
   }

   protected void setButtons() {
      MyPanel buttonPanel = new MyPanel(new BorderLayout());
      JPanel buttonPanelOne = getButtonPanelNorth();
      JPanel buttonPanelTwo = getButtonPanelSouth();
      buttonPanel.addNorth(buttonPanelOne);
      buttonPanel.addSouth(buttonPanelTwo);
      addSouth(buttonPanel);
   }

   public void setEditable(boolean selected) {
      ((MyTableModel) table.getModel()).setEditable(selected);
   }
}
