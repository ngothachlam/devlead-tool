package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
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
import org.apache.log4j.Logger;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyComboBox;
import com.jonas.agile.devleadtool.component.listener.AddNewRowActionListenerListener;
import com.jonas.agile.devleadtool.component.listener.HyperLinkOpenerAdapter;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.ComboTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.Column;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.testHelpers.TryoutTester;

public class PlanPanel extends MyComponentPanel {

	final PlannerHelper helper;
	private Logger log = MyLogger.getLogger(PlanPanel.class);
	ComboBoxTable table;

	public PlanPanel(PlannerHelper client) {
		this(client, new PlanTableModel());
	}

	public PlanPanel(PlannerHelper helper, PlanTableModel planModel) {
		super(new BorderLayout());
		this.helper = helper;
		table = new ComboBoxTable();
		JScrollPane scrollpane = new JScrollPane(table);

		table.setModel(planModel);

		table.setDefaultRenderer(String.class, new StringTableCellRenderer());
		table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());

		table.addMouseListener(new HyperLinkOpenerAdapter(helper, Column.URL, Column.Jira));
		table.setAutoCreateRowSorter(true);

		this.addCenter(scrollpane);
		this.addSouth(getBottomPanel());
		this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
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

		addButton(buttons, "Refresh FixVersions", new RefreshFixVersionsListener(table));
		addPanelWithAddAndRemoveOptions(table, buttons, new AddNewRowActionListenerListener(){
			public void addedNewRow(String jiraString, int itsRow, int itsColumn) {
				((MyTableModel)table.getModel()).setValueAt(jiraString, itsRow, itsColumn);
			}
		});

		addButton(buttons, "Sync", new SyncWithJiraActionListener(table, helper));
		return buttons;
	}

	public PlanTableModel getPlanModel() {
		return ((PlanTableModel) table.getModel());
	}

	public void setEditable(boolean selected) {
		((PlanTableModel) table.getModel()).setEditable(selected);
	}

	private final class ComboBoxTable extends MyTable {

		Map<String, ComboTableCellEditor> map = new HashMap<String, ComboTableCellEditor>();

		ComboBoxTable() {
			MyComboBox llu_comboBox = new MyComboBox(JiraProject.LLU_SYSTEMS_PROVISIONING);
			MyComboBox lludevsup_comboBox = new MyComboBox(JiraProject.LLU_DEV_SUPPORT);
			MyComboBox tst_comboBox = new MyComboBox(JiraProject.ATLASSIN_TST);

			map.put("llu", new ComboTableCellEditor(llu_comboBox));
			map.put("lludevsup", new ComboTableCellEditor(lludevsup_comboBox));
			map.put("tst", new ComboTableCellEditor(tst_comboBox));
		}

		@Override
		public TableCellEditor getCellEditor(int row, int column) {
			int modelRow = convertRowIndexToModel(row);
			int modelCol = convertColumnIndexToModel(column);
			if (modelCol == 1) {
				String jira = (String) ((MyTableModel) this.getModel()).getValueAt(modelRow, 0);
				try {
					String projectKey = helper.getProjectKey(jira).toLowerCase();
					ComboTableCellEditor editor = map.get(projectKey);
					return editor;
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
			for (ComboTableCellEditor editor : map.values()) {
				MyComboBox combo = editor.getComboBox();
				combo.removeAllItems();
				JiraProject jiraProject = combo.getProject();
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
}
