package com.jonas.agile.devleadtool.component.menu;

import java.awt.Component;
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
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.MyStatusBar;
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
import com.jonas.jira.access.JiraClient;

public class MyTablePopupMenu extends MyPopupMenu {

   private final static Logger log = MyLogger.getLogger(MyTablePopupMenu.class);
   private final MyTable sourceTable;

   public MyTablePopupMenu(final MyTable source, PlannerHelper helper, MyTable... tables) {
      super(source);
      this.sourceTable = source;
      JFrame parentFrame = helper.getParentFrame();

      add(new MenuItem_Mark("Mark Selected Rows", source));
      add(new MenuItem_UnMark("unMark Selected Rows", source));
      addSeparator();
      add(new MenuItem_Add("Add Jiras", source, parentFrame, tables));
      add(new MenuItem_Remove("Remove Jiras", sourceTable, helper.getParentFrame()));
      addMenuItem_Copys(source, parentFrame, tables);
      addSeparator();
      add(new MenuItem_Default("Open in Browser", new OpenJirasListener(sourceTable, helper)));
      add(new MenuItem_Sync(sourceTable, helper));
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

   private class MenuItem_CreateMerge extends JMenuItem {

      public MenuItem_CreateMerge(String string, final MyTable source, JFrame parent) {
         super(string);
         addActionListener(new ActionListener() {
            private Component parent;

            @Override
            public void actionPerformed(ActionEvent e) {

               int[] selectedRows = source.getSelectedRows();
               returnIfNoneSelected(selectedRows);

               List<String> jirasToGetMergeJiras = new ArrayList<String>();
               StringBuffer sb = new StringBuffer("Do you want to create merge Jiras for the following?\n");
               for (int aSelectedRow : selectedRows) {
                  String aJiraToBeMerged = (String) source.getValueAt(Column.Jira, aSelectedRow);
                  sb.append(aJiraToBeMerged).append(" ");
                  jirasToGetMergeJiras.add(aJiraToBeMerged);
               }

               JiraProject project = JiraProject.getProjectByJira(jirasToGetMergeJiras.get(0));
               JiraClient client = project.getJiraClient();

               try {
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
                           AlertDialog.alertException((Frame) parent, e1);
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
               } catch (Throwable e1) {
                  AlertDialog.alertException((Frame) parent, e1);
               }
            }

            private void returnIfNoneSelected(int[] rows) {
               if (rows.length == 0) {
                  JOptionPane.showMessageDialog(parent, "No Jiras Selected!");
                  return;
               }
            }
         });
      }
   }

   private abstract class MenuItem_Marking_Abstract extends JMenuItem {
      protected MyTable source;

      public MenuItem_Marking_Abstract(String string, final MyTable source) {
         super(string);
         init(source);
      }

      public MenuItem_Marking_Abstract(String string, final MyTable source, int mnemonic) {
         super(string, mnemonic);
         init(source);
      }

      private void init(final MyTable source) {
         this.source = source;
         if (source.isMarkingAllowed()) {
            setEnabled(true);
         } else {
            setEnabled(false);
         }
         addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               doAction();
               MyStatusBar.getInstance().setMessage("Rows in " + source.getTitle() + " where marked/unmarked!", true);
            }

         });
      }

      protected abstract void doAction();
   }

   private class MenuItem_Mark extends MenuItem_Marking_Abstract {
      public MenuItem_Mark(String string, final MyTable source) {
         super(string, source);
         setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));

      }

      @Override
      protected void doAction() {
         source.markSelected();
      }
   }

   private class MenuItem_UnMark extends MenuItem_Marking_Abstract {
      public MenuItem_UnMark(String string, final MyTable source) {
         super(string, source);
      }

      @Override
      protected void doAction() {
         source.unMarkSelection();
      }
   }

   private class MenuItem_Add extends JMenuItem {

      public MenuItem_Add(String string, final MyTable source, final JFrame frame, final MyTable... dtos) {
         super(string);
         addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               AddManualDialog addManualDialog = new AddManualDialog(frame, dtos);
               addManualDialog.setSourceTable(source);
            }
         });
      }
   }

   private class MenuItem_Copy extends JMenuItem implements ActionListener {

      private final MyTable destinationTable;
      private JFrame frame;
      private MyTable sourceTable;

      public MenuItem_Copy(JFrame frame, String string, MyTable source, MyTable table) {
         super(string);
         this.frame = frame;
         this.sourceTable = source;
         this.destinationTable = table;
         addActionListener(this);
      }

      public void actionPerformed(ActionEvent e) {
         final int[] selectedRows = sourceTable.getSelectedRows();
         if (selectedRows.length == 0) {
            AlertDialog.alertMessage(frame, "No rows selected!");
         }
         final ProgressDialog dialog = new ProgressDialog(frame, "Copying...", "Copying selected messages from Board to Plan...", selectedRows.length);
         try {
            for (int i = 0; i < selectedRows.length; i++) {
               String jiraString = (String) sourceTable.getValueAt(Column.Jira, selectedRows[i]);
               addJira(jiraString, destinationTable);
               dialog.increseProgress();
            }
            dialog.setCompleteWithDelay(300);
         } catch (Exception ex) {
            AlertDialog.alertException(null, ex);
            ex.printStackTrace();
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
   }

   private class MenuItem_Default extends JMenuItem {

      public MenuItem_Default(String string, ActionListener actionListener) {
         super(string);
         addActionListener(actionListener);
      }
   }

   private class MenuItem_Remove extends JMenuItem implements ActionListener {
      private MyTable sourceTable;
      private final Frame parent;

      public MenuItem_Remove(String string, MyTable sourceTable, Frame parent) {
         super(string);
         this.sourceTable = sourceTable;
         this.parent = parent;
         addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         int[] selectedRows = sourceTable.getSelectedRows();

         StringBuffer sb = new StringBuffer("Do you want to remove the following jiras from ");
         sb.append(sourceTable.getTitle()).append("?");

         for (int aSelectedRow : selectedRows) {
            sb.append("\n").append(sourceTable.getValueAt(Column.Jira, aSelectedRow));
         }
         int result = JOptionPane.showConfirmDialog(parent, sb.toString(), "Remove jiras?", JOptionPane.YES_NO_OPTION);
         log.debug(result);
         if (result == JOptionPane.YES_OPTION) {

            final ProgressDialog dialog = new ProgressDialog(parent, "Removing...", "Removing selected Jiras...", 0);
            if (sourceTable.getSelectedRowCount() <= 0) {
               dialog.setNote("Nothing selected!!");
               dialog.setCompleteWithDelay(2000);
            }

            sourceTable.removeSelectedRows();
            dialog.setCompleteWithDelay(300);
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
}
