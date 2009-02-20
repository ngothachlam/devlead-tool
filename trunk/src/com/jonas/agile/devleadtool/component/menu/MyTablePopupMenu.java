package com.jonas.agile.devleadtool.component.menu;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.listener.OpenJirasListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.HyperLinker;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.ClientConstants;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.rollforwardapp.RollforwardParser;
import com.jonas.testHelpers.JiraXMLHelper;

public class MyTablePopupMenu extends MyPopupMenu {

   private final static Logger log = MyLogger.getLogger(MyTablePopupMenu.class);
   private final MyTable sourceTable;

   public MyTablePopupMenu(final MyTable source, PlannerHelper helper, MyTable... tables) {
      super(source);
      this.sourceTable = source;
      JFrame parentFrame = helper.getParentFrame();

      MenuItem_Mark menuItem = new MenuItem_Mark(parentFrame, "Mark Selected Rows", source);
      log.debug("menuItem Parent: " + menuItem.getParent());
      add(menuItem);
      add(new MenuItem_UnMark(parentFrame, "unMark Selected Rows", source));
      addSeparator();
      add(new MenuItem_Add("Add Jiras", source, parentFrame, tables));
      add(new MenuItem_Remove("Remove Jiras", sourceTable, helper.getParentFrame()));
      addMenuItem_Copys(source, parentFrame, tables);
      addSeparator();
      add(new MenuItem_Default("Open in Browser", new OpenJirasListener(sourceTable, helper)));
      add(new MenuItem_Sync(sourceTable, helper));
      addSeparator();
      add(new MenuItem_Rollforwards(sourceTable, helper));
      addSeparator();
      add(new MenuItem_CreateMerge("Create Merge", source, helper.getParentFrame()));
   }

   private void addMenuItem_Copys(MyTable source, JFrame parentFrame, MyTable... tables) {
      for (MyTable tableDTO : tables) {
         if (!tableDTO.equals(source)) {
            String title = "Copy to other Table: " + tableDTO.getTitle();
            add(new MenuItem_Copy(parentFrame, title, sourceTable, tableDTO));
         }
      }
   }

   private class MenuItem_CreateMerge extends MyMenuItem {

      private Frame parent;
      private final MyTable source;

      public MenuItem_CreateMerge(String string, final MyTable source, JFrame parent) {
         super(string);
         this.source = source;
         this.parent = parent;

      }

      @Override
      public void executeOnFinal() {
      }

      @Override
      public Frame getParentFrame() {
         return parent;
      }

      @Override
      public void myActionPerformed(ActionEvent e) throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
            java.rmi.RemoteException, NotJiraException {
         int[] selectedRows = source.getSelectedRows();
         if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(parent, "No Jiras Selected!");
            return;
         }

         List<String> jirasToGetMergeJiras = new ArrayList<String>();
         StringBuffer sb = new StringBuffer("Do you want to create merge Jiras for the following?\n");
         for (int aSelectedRow : selectedRows) {
            String aJiraToBeMerged = (String) source.getValueAt(Column.Jira, aSelectedRow);
            sb.append(aJiraToBeMerged).append(" ");
            jirasToGetMergeJiras.add(aJiraToBeMerged);
         }

         JiraProject project = JiraProject.getProjectByJira(jirasToGetMergeJiras.get(0));
         JiraClient client = project.getJiraClient();

         JiraVersion[] fixVersions = client.getFixVersionsFromProject(project, false);
         JiraVersion fixVersionToCreateMergesAgainst = (JiraVersion) JOptionPane.showInputDialog(parent, sb.toString(),
               "What Fix Version for the merges?", JOptionPane.QUESTION_MESSAGE, null, fixVersions, null);
         log.debug("result " + fixVersionToCreateMergesAgainst);
         if (fixVersionToCreateMergesAgainst != null) {
            sb = new StringBuffer("Created Merge Jiras:\n");
            List<String> jirasMerged = new ArrayList<String>();
            for (String aJiraToGetMerge : jirasToGetMergeJiras) {
               project = JiraProject.getProjectByJira(aJiraToGetMerge);
               try {
                  JiraClient jiraClient = project.getJiraClient();
                  String fixVersionName = fixVersionToCreateMergesAgainst.getName();
                  String mergeJiraCreated = jiraClient.createMergeJira(aJiraToGetMerge, fixVersionName);
                  sb.append(mergeJiraCreated).append(" ");
                  jirasMerged.add(mergeJiraCreated);
                  source.setValueAt(mergeJiraCreated, aJiraToGetMerge, Column.Merge);
                  source.addJira(new JiraIssue(mergeJiraCreated, fixVersionName));
               } catch (Throwable e1) {
                  AlertDialog.alertException(parent, e1);
               }
            }
            sb.append("\nOpen them in browser?");
            int shallOpenInBrowser = JOptionPane.showConfirmDialog(parent, sb.toString());
            if (shallOpenInBrowser == JOptionPane.YES_OPTION) {
               try {
                  for (String aJiraMerged : jirasMerged) {
                     String jira_url = PlannerHelper.getJiraUrl(aJiraMerged);
                     HyperLinker.displayURL(jira_url + "/browse/" + aJiraMerged);
                  }
               } catch (URISyntaxException e1) {
                  e1.printStackTrace();
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
            }
         }
      }
   }

   private abstract class MenuItem_Marking_Abstract extends MyMenuItem {
      protected MyTable source;
      private Frame parentFrame;

      public MenuItem_Marking_Abstract(Frame parentFrame, String string, final MyTable source) {
         super(string);
         this.parentFrame = parentFrame;
         init(source);
      }

      private void init(final MyTable source) {
         this.source = source;
         if (source.isMarkingAllowed()) {
            setEnabled(true);
         } else {
            setEnabled(false);
         }
      }

      public void myActionPerformed(ActionEvent e) {
         doAction();
         MyStatusBar.getInstance().setMessage("Rows in " + source.getTitle() + " where marked/unmarked!", true);
      }

      protected abstract void doAction();

      @Override
      public void executeOnFinal() {
      }

      @Override
      public Frame getParentFrame() {
         return parentFrame;
      }
   }

   private class MenuItem_Mark extends MenuItem_Marking_Abstract {

      public MenuItem_Mark(Frame parentFrame, String string, final MyTable source) {
         super(parentFrame, string, source);
         setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));

      }

      @Override
      protected void doAction() {
         source.markSelected();
      }

   }

   private class MenuItem_UnMark extends MenuItem_Marking_Abstract {
      public MenuItem_UnMark(Frame parent, String string, final MyTable source) {
         super(parent, string, source);
      }

      @Override
      protected void doAction() {
         source.unMarkSelection();
      }
   }

   private class MenuItem_Add extends MyMenuItem {

      private final MyTable source;
      private final JFrame frame;
      private final MyTable[] dtos;

      public MenuItem_Add(String string, final MyTable source, final JFrame frame, final MyTable... dtos) {
         super(string);
         this.source = source;
         this.frame = frame;
         this.dtos = dtos;
      }

      @Override
      public void myActionPerformed(ActionEvent e) {
         AddManualDialog addManualDialog = new AddManualDialog(frame, dtos);
         addManualDialog.setSourceTable(source);
      }

      @Override
      public void executeOnFinal() {
      }

      @Override
      public Frame getParentFrame() {
         return frame;
      }
   }

   private class MenuItem_Copy extends MyMenuItem {

      private final MyTable destinationTable;
      private JFrame parent;
      private MyTable sourceTable;
      private ProgressDialog dialog;

      public MenuItem_Copy(JFrame frame, String string, MyTable source, MyTable table) {
         super(string);
         this.parent = frame;
         this.sourceTable = source;
         this.destinationTable = table;
      }

      public void myActionPerformed(ActionEvent e) {
         final int[] selectedRows = sourceTable.getSelectedRows();
         if (selectedRows.length == 0) {
            AlertDialog.alertMessage(parent, "No rows selected!");
         }
         dialog = new ProgressDialog(parent, "Copying...", "Copying selected messages from Board to Plan...", selectedRows.length, true);
         for (int i = 0; i < selectedRows.length; i++) {
            String jiraString = (String) sourceTable.getValueAt(Column.Jira, selectedRows[i]);
            addJira(jiraString, destinationTable);
            dialog.increseProgress();
         }
      }

      void addJira(String jiraString, MyTable table) {
         Map<Column, Object> map = new HashMap<Column, Object>();
         Column[] columns = sourceTable.getColumns();
         for (Column column : columns) {
            MyTableModel model = (MyTableModel) sourceTable.getModel();
            int row = model.getRowWithJira(jiraString);
            map.put(column, model.getValueAt(column, row));
         }
         table.addJira(jiraString, map);
      }

      @Override
      public void executeOnFinal() {
         if (dialog != null)
            dialog.setCompleteWithDelay(300);
      }

      @Override
      public Frame getParentFrame() {
         return parent;
      }
   }

   private class MenuItem_Default extends JMenuItem {

      public MenuItem_Default(String string, ActionListener actionListener) {
         super(string);
         addActionListener(actionListener);
      }
   }

   private class MenuItem_Remove extends MyMenuItem {
      private MyTable sourceTable;
      private final Frame parent;
      private ProgressDialog dialog;

      public MenuItem_Remove(String string, MyTable sourceTable, Frame parent) {
         super(string);
         this.sourceTable = sourceTable;
         this.parent = parent;
      }

      @Override
      public void myActionPerformed(ActionEvent e) {
         int[] selectedRows = sourceTable.getSelectedRows();

         StringBuffer sb = new StringBuffer("Do you want to remove the following jiras from ");
         sb.append(sourceTable.getTitle()).append("?");

         for (int aSelectedRow : selectedRows) {
            sb.append("\n").append(sourceTable.getValueAt(Column.Jira, aSelectedRow));
         }
         int result = JOptionPane.showConfirmDialog(parent, sb.toString(), "Remove jiras?", JOptionPane.YES_NO_OPTION);
         log.debug(result);
         if (result == JOptionPane.YES_OPTION) {
            dialog = new ProgressDialog(parent, "Removing...", "Removing selected Jiras...", 0, true);
            if (sourceTable.getSelectedRowCount() <= 0) {
               dialog.setNote("Nothing selected!!");
               dialog.setCompleteWithDelay(2000);
            }

            sourceTable.removeSelectedRows();
         }
      }

      @Override
      public Frame getParentFrame() {
         return parent;
      }

      @Override
      public void executeOnFinal() {
         if (dialog != null)
            dialog.setCompleteWithDelay(300);
      }
   }

   private abstract class MenuItem_Abstract extends JMenuItem implements ActionListener {
      @Override
      public abstract void actionPerformed(ActionEvent e);

      public MenuItem_Abstract(String title) {
         super(title);
         addActionListener(this);
      }
   }

   private class MenuItem_Rollforwards extends MenuItem_Abstract {
      private final MyTable sourceTable;
      private final PlannerHelper helper;
      private JiraXMLHelper xmlHelper;
      private RollforwardParser parser;
      private JTextArea textArea;
      private JFrame newFrame;

      public MenuItem_Rollforwards(MyTable sourceTable, PlannerHelper helper) {
         super("Show Rollforwards");
         this.sourceTable = sourceTable;
         this.helper = helper;
         xmlHelper = new JiraXMLHelper(ClientConstants.JIRA_URL_AOLBB);
         parser = new RollforwardParser();

         newFrame = new JFrame();
         textArea = new JTextArea();
         newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         newFrame.setResizable(true);
         JPanel panel = new JPanel(new BorderLayout());
         JScrollPane scroll = new JScrollPane(textArea);
         scroll.setPreferredSize(new Dimension(300, 200));
         panel.add(scroll, BorderLayout.CENTER);
         newFrame.getContentPane().add(panel, BorderLayout.CENTER);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Getting information from Jira....", "Starting...", 0, false);
         dialog.setIndeterminate(true);
         SwingWorkerImpl swingWorkerImpl = new SwingWorkerImpl(dialog);
         swingWorkerImpl.execute();

      }

      private final class SwingWorkerImpl extends SwingWorker<Object, Object> {
         private final ProgressDialog dialog;

         private SwingWorkerImpl(ProgressDialog dialog) {
            this.dialog = dialog;
         }

         public Object doInBackground() {
            dialog.setVisible(true);
            int[] selectedRows = sourceTable.getSelectedRows();
            dialog.increaseMax("Syncing...", selectedRows.length);
            StringBuffer output = new StringBuffer();
            try {
               xmlHelper.loginToJira();
               dialog.setNote("Logging in to Jira...");
               output.append("Rollforwards\n----------------------\n");
               for (int i : selectedRows) {
                  Jira jira = new Jira(((String) sourceTable.getValueAt(Column.Jira, i)));
                  try {

                     String html = xmlHelper.getXML(jira.getUrl());
                     dialog.increseProgress("Getting Info from " + jira);

                     List<String> rollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(html);
                     for (String rollforward : rollforwards) {
                        jira.addRollforward(rollforward);
                     }
                     output.append(jira.getOutput());
                  } catch (IOException e2) {
                     e2.printStackTrace();
                  }
               }
               textArea.setText(output.toString());
               newFrame.pack();
               newFrame.setVisible(true);
            } catch (HttpException e1) {
               e1.printStackTrace();
            } catch (IOException e1) {
               e1.printStackTrace();
            } catch (JiraException e1) {
               e1.printStackTrace();
            }
            return null;
         }

         public void done() {
            log.debug("Syncing Finished!");
            dialog.setCompleteWithDelay(300);
         }
      }

      private final class Jira {
         private final String jiraStr;
         private List<String> rollforwards = new ArrayList<String>();
         public Jira(String jiraStr) {
            this.jiraStr = jiraStr;
         }
         public Object getOutput() {
            StringBuffer sb = new StringBuffer();
            sb.append("  ").append(jiraStr).append("\n");
            for (String rollforward : rollforwards) {
               sb.append("     ").append(rollforward).append("\n");
            }
            return sb.toString();
         }
         public void addRollforward(String string) {
            rollforwards.add(string);
         }
         protected String getUrl() {
            StringBuffer url = new StringBuffer();
            url.append("browse/").append(jiraStr).append("?page=com.atlassian.jira.plugin.ext.subversion:subversion-commits-tabpanel");
            return url.toString();
         }
      }
   }

   private class MenuItem_Sync extends JMenuItem {
      public MenuItem_Sync(final MyTable sourceTable, PlannerHelper helper) {
         super("Dowload Jira Info");
         addActionListener(getActionListener(sourceTable, helper));
      }

      private SyncWithJiraListener getActionListener(final MyTable sourceTable, PlannerHelper helper) {
         SyncWithJiraListener actionListener = new SyncWithJiraListener(sourceTable, helper);
         SyncWithJiraActionListenerListener syncWithJiraActionListenerListener = new SyncWithJiraActionListenerListener() {
            public void jiraAdded(JiraIssue jiraIssue) {
               sourceTable.addJira(jiraIssue);
            }

            public void jiraSynced(JiraIssue jiraIssue, int tableRowSynced) {
               sourceTable.syncJira(jiraIssue, tableRowSynced);
            }
         };
         actionListener.addListener(syncWithJiraActionListenerListener);
         return actionListener;
      }
   }
};
