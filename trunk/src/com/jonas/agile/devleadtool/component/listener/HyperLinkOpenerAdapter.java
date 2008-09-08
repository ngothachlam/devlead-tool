package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.HyperLinker;

/**
 * Is meant to be a mouseclick adapter to the JTable that listens on clicks to a cell with a header name as specified in the
 * constructor. When that happens it should open up a browser window in relation to that url.
 * 
 * @author jonasjolofsson
 * 
 */
public class HyperLinkOpenerAdapter extends MouseAdapter {
	private final PlannerHelper helper;
	private final Column hyperLinkColumn;
	private final Column jiraColumn;

	public HyperLinkOpenerAdapter(PlannerHelper helper, Column hyperLinkColumn, Column jiraColumn) {
		this.helper = helper;
		this.hyperLinkColumn = hyperLinkColumn;
		this.jiraColumn = jiraColumn;
	}

	public void myMouseClicked(MouseEvent e) throws NotJiraException {

	}

	public void mouseClicked(MouseEvent e) {
		MyTable table = (MyTable) e.getSource();
		int itsRow = table.rowAtPoint(e.getPoint());
		int itsColumn = table.columnAtPoint(e.getPoint());
		if (hyperLinkColumn.equals(table.getColumnEnum(itsColumn))) {
			String jira = (String) table.getValueAt(jiraColumn, itsRow);
			try {
				HyperLinker.displayURL(helper.getJiraUrl(jira) + "/browse/" + jira);
			} catch (NotJiraException e1) {
				AlertDialog.alertException(helper, e1);
			}
		}
	}
}