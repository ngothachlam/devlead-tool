package com.jonas.agile.devleadtool.component.table;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.table.editor.ComboTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class MyTable extends JTable {

	private Logger log = MyLogger.getLogger(MyTable.class);

	public MyTable() {
		super();
	}

	public void setColumnRenderer(int i, TableCellRenderer renderer) {
		TableColumnModel tcm = getColumnModel();
		TableColumn tc = tcm.getColumn(i);
		tc.setCellRenderer(renderer);
	}

	public void setRow(JiraIssue jira, int i) {
		log.debug("setRow for jira: " + jira + "i: " + i);
		setValueAt(jira.getKey(), i, 0);
		// TODO only works with one fix-version below!!
		List<JiraVersion> fixVersions = jira.getFixVersions();
		JiraVersion jiraVersion = fixVersions != null && fixVersions.size() > 0 ? fixVersions.get(0) : null;
		setValueAt(jiraVersion, i, 1);
		setValueAt(jira.getStatus(), i, 2);
		setValueAt(jira.getResolution(), i, 3);
	}

	public void setColumnEditor(int i, ComboTableCellEditor editor) {
		TableColumnModel tcm = getColumnModel();
		TableColumn tc = tcm.getColumn(i);
		tc.setCellEditor(editor);

	}

   public boolean isRed(Object value, int row, int column) {
      return ((MyTableModel)getModel()).isRed(value, convertRowIndexToModel(row), convertColumnIndexToModel(column));
   }

}
