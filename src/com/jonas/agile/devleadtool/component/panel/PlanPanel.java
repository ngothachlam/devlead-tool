package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import com.ProgressDialog;
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
import com.jonas.jira.access.JiraIssueNotFoundException;
import com.jonas.jira.access.JiraListener;
import com.jonas.testHelpers.TryoutTester;

public class PlanPanel extends MyComponentPanel {

	private final PlannerHelper helper;
	private MyTable table;
	private Logger log = MyLogger.getLogger(PlanPanel.class);
	private JComboBox comboBox;
   private JTextField jiraPrefix;
   private JTextField jiraCommas;

	public PlanPanel(PlannerHelper client) {
		this(client, new PlanTableModel());
	}

	public PlanPanel(PlannerHelper helper, PlanTableModel planModel) {
		super(new BorderLayout());
		this.helper = helper;
		PlanTableModel model = planModel;

		table = new MyTable();
		table.setModel(model);

		table.setDefaultRenderer(String.class, new StringTableCellRenderer());
		table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());


		comboBox = new JComboBox(JiraProject.LLU_SYSTEMS_PROVISIONING.getFixVersions(false));
		table.setColumnEditor(1, new ComboTableCellEditor(model, comboBox));

		table.addMouseListener(new HyperLinkOpenerAdapter(table, helper, PlanTableModel.COLUMNNAME_HYPERLINK, 0));
		
		JScrollPane scrollpane = new JScrollPane(table);

		table.setAutoCreateRowSorter(true);
		this.addCenter(scrollpane);
		this.addSouth(getBottomPanel());
	}

	private Component getBottomPanel() {
		JPanel buttons = new JPanel();
		JLabel jiraPrefixLabel = new JLabel("Prefix:");
		jiraPrefix = new JTextField(5);
      jiraCommas = new JTextField(20);
      JButton addJira = new JButton("Add");
		JButton syncSelectedWithJiraButton = new JButton("sync With Jira");

		addJira.addActionListener(new AddNewRowActionListener(table));
		syncSelectedWithJiraButton.addActionListener(new SyncWithJiraActionListener());

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

	public static void main(String[] args) {
		JFrame frame = TryoutTester.getFrame();
		JPanel panel = new PlanPanel(new PlannerHelper(frame, "test"));
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	public void setEditable(boolean selected) {
		((PlanTableModel) table.getModel()).setEditable(selected);
	}

	public boolean doesJiraExist(String jira) {
		return ((PlanTableModel) table.getModel()).doesJiraExist(jira);
	}

	private final class AddNewRowActionListener implements ActionListener {
		private final MyTable table;

		public AddNewRowActionListener(MyTable table) {
			this.table = table;
		}

		public void actionPerformed(ActionEvent e) {
		   MyStringParser parser = new MyStringParser();
		   parser.separateString(jiraCommas);
			((MyTableModel) table.getModel()).addEmptyRow();
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
							final String jiraToGet = (String) table.getValueAt(rowSelected, 0);
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
							table.setRow(jira, rowSelected);
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
