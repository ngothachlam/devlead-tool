package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
import com.ProgressDialog;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.listener.HyperLinkOpenerAdapter;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.ComboTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.SwingWorker;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.MyStringParser;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraIssueNotFoundException;
import com.jonas.jira.access.JiraListener;
import com.jonas.testHelpers.TryoutTester;

public class PlanPanel extends MyComponentPanel {

   private final PlannerHelper helper;
   private JTextField jiraCommas;
   private JTextField jiraPrefix;
   private Logger log = MyLogger.getLogger(PlanPanel.class);
   private ComboBoxTable table;

   public PlanPanel(PlannerHelper client) {
      this(client, new PlanTableModel());
   }

   public PlanPanel(PlannerHelper helper, PlanTableModel planModel) {
      super(new BorderLayout());
      this.helper = helper;
      PlanTableModel model = planModel;

      table = new ComboBoxTable();
      JScrollPane scrollpane = new JScrollPane(table);

      table.setModel(model);

      table.setDefaultRenderer(String.class, new StringTableCellRenderer());
      table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());

      table.addMouseListener(new HyperLinkOpenerAdapter(table, helper, PlanTableModel.COLUMNNAME_HYPERLINK, 0));
      table.setAutoCreateRowSorter(true);

      this.addCenter(scrollpane);
      this.addSouth(getBottomPanel());
   }

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      JPanel panel = new PlanPanel(new PlannerHelper(frame, "test"));
      frame.setContentPane(panel);
      frame.setVisible(true);
   }

   public boolean doesJiraExist(String jira) {
      return ((PlanTableModel) table.getModel()).doesJiraExist(jira);
   }

   private Component getBottomPanel() {
      JPanel buttons = new JPanel();
      JButton refreshFixVersions = new JButton("Refresh FixVersions");
      JLabel jiraPrefixLabel = new JLabel("Prefix:");
      jiraPrefix = new JTextField(5);
      jiraCommas = new JTextField(20);
      JButton addJira = new JButton("Add");
      JButton syncSelectedWithJiraButton = new JButton("sync With Jira");

      refreshFixVersions.addActionListener(new RefreshFixVersionsListener(table));
      addJira.addActionListener(new AddNewRowActionListener(table));
      syncSelectedWithJiraButton.addActionListener(new SyncWithJiraActionListener());

      buttons.add(refreshFixVersions);
      buttons.add(jiraPrefixLabel);
      buttons.add(jiraPrefix);
      buttons.add(jiraCommas);
      buttons.add(addJira);
      buttons.add(syncSelectedWithJiraButton);
      return buttons;
   }

   public PlanTableModel getPlanModel() {
      return ((PlanTableModel) table.getModel());
   }

   public void setEditable(boolean selected) {
      ((PlanTableModel) table.getModel()).setEditable(selected);
   }

   private final class AddNewRowActionListener implements ActionListener {
      private final MyTable table;

      public AddNewRowActionListener(MyTable table) {
         this.table = table;
      }

      public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
         MyStringParser parser = new MyStringParser();
         List<String> jiras = parser.separateString(jiraCommas.getText());
         for (String string : jiras) {
            Vector<String> vector = new Vector<String>();
            String prefix = jiraPrefix.getText();
            vector.add((prefix.trim().length() > 0 ? prefix + "-" : "") + string);
            ((MyTableModel) table.getModel()).addRow(vector);
         }
      }
   }

   private final class ComboBoxTable extends MyTable {

      Map<String, EditorProjectPair> map = new HashMap<String, EditorProjectPair>();

      ComboBoxTable() {
         JComboBox llu_comboBox = new JComboBox(JiraProject.LLU_SYSTEMS_PROVISIONING.getFixVersions(false));
         JComboBox lludevsup_comboBox = new JComboBox(JiraProject.LLU_DEV_SUPPORT.getFixVersions(false));
         JComboBox tst_comboBox = new JComboBox(JiraProject.ATLASSIN_TST.getFixVersions(false));

         TableCellEditor llu = new ComboTableCellEditor(llu_comboBox);
         TableCellEditor lludevsup = new ComboTableCellEditor(lludevsup_comboBox);
         TableCellEditor tst = new ComboTableCellEditor(tst_comboBox);

         // map.put("llu", new EditorProjectPair(llu, llu_comboBox, JiraProject.LLU_SYSTEMS_PROVISIONING));
         // map.put("lludevsup", new EditorProjectPair(lludevsup, lludevsup_comboBox, JiraProject.LLU_SYSTEMS_PROVISIONING));
         map.put("tst", new EditorProjectPair(tst, tst_comboBox, JiraProject.ATLASSIN_TST));
      }

      @Override
      public TableCellEditor getCellEditor(int row, int column) {
         int modelRow = convertRowIndexToModel(row);
         int modelCol = convertColumnIndexToModel(column);
         if (modelCol == 1) {
            String jira = (String) ((MyTableModel) this.getModel()).getValueAt(modelRow, 0);
            log.debug("########");
            log.debug(jira);
            try {
               return map.get(helper.getProjectKey(jira).toLowerCase()).getEditor();
            } catch (Throwable t) {
               log.error(t);
               return super.getCellEditor(row, column);
            }
         }
         return super.getCellEditor(row, column);
      }

      @Override
      public TableCellRenderer getCellRenderer(int row, int column) {
         return super.getCellRenderer(row, column);
      }

      void refreshFixVersions() {
         for (EditorProjectPair editorProjectPair : map.values()) {
            JComboBox combo = editorProjectPair.getCombo();
            combo.removeAllItems();
            JiraProject jiraProject = editorProjectPair.getJiraProject();
            try {
               JiraVersion[] fixVersionsFromProject = jiraProject.getJiraClient().getFixVersionsFromProject(jiraProject, false);
               for (JiraVersion jiraVersion : fixVersionsFromProject) {
                  combo.addItem(jiraVersion);
               }
            } catch (RemotePermissionException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (RemoteAuthenticationException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (RemoteException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (java.rmi.RemoteException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

         }
      }
   }

   private final class EditorProjectPair {
      private final JComboBox combo;
      private final TableCellEditor editor;
      private final JiraProject jiraProject;

      public EditorProjectPair(TableCellEditor editor, JComboBox combo, JiraProject jiraProject) {
         super();
         this.combo = combo;
         this.editor = editor;
         this.jiraProject = jiraProject;
      }

      public JComboBox getCombo() {
         return combo;
      }

      public TableCellEditor getEditor() {
         return editor;
      }

      public JiraProject getJiraProject() {
         return jiraProject;
      }
   }

   private final class RefreshFixVersionsListener implements ActionListener {

      private final ComboBoxTable table;

      public RefreshFixVersionsListener(ComboBoxTable table) {
         this.table = table;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         table.refreshFixVersions();
      }
   }

   private final class SyncWithJiraActionListener implements ActionListener {
      private Logger log = MyLogger.getLogger(this.getClass());

      public SyncWithJiraActionListener() {
      }

      public void actionPerformed(ActionEvent e) {
         log.debug(e);
         final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Syncing with Jira...", "Starting...", 0);
         dialog.setIndeterminate(false);
         SwingWorker worker = new SwingWorker() {
            public Object construct() {
               final int[] rows = table.getSelectedRows();
               dialog.increaseMax("Syncing...", rows.length);
               try {
                  for (int rowSelected : rows) {
                     int convertedTableRowToModel = table.convertRowIndexToModel(rowSelected);
                     final String jiraToGet = (String) (table.getModel()).getValueAt(convertedTableRowToModel, 0);
                     dialog.increseProgress("Syncing " + jiraToGet);
                     log.debug("Syncing Jira" + jiraToGet);
                     JiraIssue jira;
                     try {
                        jira = helper.getJiraIssueFromName(jiraToGet, new JiraListener() {
                           public void notifyOfAccess(JiraAccessUpdate accessUpdate) {
                              switch (accessUpdate) {
                              case LOGGING_IN:
                                 String string = "Syncing " + jiraToGet + " - Logging in!";
                                 log.debug(string);
                                 dialog.setNote(string);
                                 break;
                              case GETTING_FIXVERSION:
                                 String string2 = "Syncing " + jiraToGet + " - Getting FixVersion!";
                                 log.debug(string2);
                                 dialog.setNote(string2);
                                 break;
                              case GETTING_JIRA:
                                 String string3 = "Syncing " + jiraToGet + " - Accessing Jira info!";
                                 log.debug(string3);
                                 dialog.setNote(string3);
                                 break;
                              default:
                                 break;
                              }
                           }
                        });
                     } catch (JiraIssueNotFoundException e) {
                        AlertDialog.alertException(helper, e);
                        e.printStackTrace();
                        continue;
                     }
                     log.debug("jira: " + jira + " rowSelected: " + rowSelected);
                     ((PlanTableModel) table.getModel()).setRow(jira, convertedTableRowToModel);
                     // table.setRow(jira, rowSelected);
                  }
               } catch (Exception e) {
                  AlertDialog.alertException(helper, e);
                  e.printStackTrace();
               }
               return null;
            }

            public void finished() {
               log.debug("Syncing Finished!");
               dialog.setCompleteSoonish();
            }
         };
         worker.start();
      }
   }
}
