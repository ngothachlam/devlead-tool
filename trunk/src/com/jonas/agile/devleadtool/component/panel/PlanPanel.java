package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyComboBox;
import com.jonas.agile.devleadtool.component.listener.CopyToTableListener;
import com.jonas.agile.devleadtool.component.listener.DestinationRetriever;
import com.jonas.agile.devleadtool.component.listener.HyperLinkOpenerAdapter;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.ComboTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class PlanPanel extends MyComponentPanel {

   private final PlannerHelper helper;
   private Logger log = MyLogger.getLogger(PlanPanel.class);
   private JFrame planVersionsFrame;
   private MyTable table;

   public PlanPanel(PlannerHelper client) {
      this(client, new PlanTableModel());
   }

   public PlanPanel(PlannerHelper helper, PlanTableModel planModel) {
      super(new BorderLayout());
      this.helper = helper;
      table = new MyTable(planModel);

      JScrollPane scrollpane = new JScrollPane(table);

      // table.addMouseListener(new HyperLinkOpenerAdapter(helper, ColumnDataType.URL, ColumnDataType.Jira));
      this.addCenter(scrollpane);
      this.addSouth(getBottomPanel());
      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
   }

   public boolean doesJiraExist(String jira) {
      return ((PlanTableModel) table.getModel()).doesJiraExist(jira);
   }

   protected JPanel getBottomPanel() {
      MyPanel buttonPanel = new MyPanel(new BorderLayout());
//      JPanel buttonPanelOne = getButtonPanelNorth();
//      buttonPanel.addNorth(buttonPanelOne);
      JPanel buttonPanelTwo = getButtonPanelSouth();
      buttonPanel.addSouth(buttonPanelTwo);
      return buttonPanel;
   }

   private JPanel getButtonPanelNorth() {
      JPanel buttonPanel = new JPanel();
      addFilter(buttonPanel, table, Column.Jira, Column.Description);
      return buttonPanel;
   }

   private JPanel getButtonPanelSouth() {
      JPanel buttons = new JPanel();

      addPanelWithAddAndRemoveOptions(table, buttons);
      SyncWithJiraActionListener listener = new SyncWithJiraActionListener(table, helper);
      listener.addListener(new SyncWithJiraActionListenerListener() {
         public void jiraAdded(JiraIssue jiraIssue) {
         }

         public void jiraSynced(JiraIssue jira, int tableRowSynced) {
            table.setValueAt(jira.getSummary(), tableRowSynced, Column.Description);
            table.setValueAt(jira.getType(), tableRowSynced, Column.J_Type);
         }

         public void jiraSyncedCompleted() {
         }
      });
      addButton(buttons, "Sync", listener);
      addButton(buttons, "Open Jiras", new OpenJirasListener(table, helper));
      addButton(buttons, "Copy to Board", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getBoardTable();
         }
      }, helper));
      addButton(buttons, "Copy to Jira", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getJiraTable();
         }
      }, helper));

      setupPlanVersionsFrame();
      addButton(buttons, "PlanVersions", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            SwingUtil.centreWindowWithinWindow(planVersionsFrame, helper.getParentFrame());
            planVersionsFrame.setVisible(true);
         }
      });
      return buttons;
   }

   public PlanTableModel getPlanModel() {
      return ((PlanTableModel) table.getModel());
   }

   public MyTable getTable() {
      return table;
   }

   public void setEditable(boolean selected) {
      ((PlanTableModel) table.getModel()).setEditable(selected);
   }

   private void setupPlanVersionsFrame() {
      planVersionsFrame = new JFrame();
      JPanel contentPanel = new FixVersionsPanel();
      planVersionsFrame.setContentPane(contentPanel);
      planVersionsFrame.setSize(450, 210);
   }

}
