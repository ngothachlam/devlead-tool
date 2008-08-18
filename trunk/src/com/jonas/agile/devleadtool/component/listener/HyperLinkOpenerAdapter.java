package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.HyperLinker;

/**
 * Is meant to be a mouseclick adapter to the JTable that listens on clicks to a cell with a header name as specified in the constructor.
 * When that happens it should open up a browser window in relation to that url. 
 * 
 * @author jonasjolofsson
 * 
 */
public class HyperLinkOpenerAdapter extends MouseAdapter {
	private final JTable table;
	private final PlannerHelper helper;
	private final String hyperLinkColumnName;
	private final int columnContainingUrl;

	public HyperLinkOpenerAdapter(JTable table, PlannerHelper helper, String hyperLinkColumnName, int columnContainingUrl) {
		this.table = table;
		this.helper = helper;
		this.hyperLinkColumnName = hyperLinkColumnName;
		this.columnContainingUrl = columnContainingUrl;
	}

	public void mouseClicked(MouseEvent e) {
		JTable aTable = (JTable) e.getSource();
		int itsRow = aTable.rowAtPoint(e.getPoint());
		int itsColumn = aTable.columnAtPoint(e.getPoint());
		if (hyperLinkColumnName.equals(aTable.getColumnName(itsColumn))) {
			String jira = (String) table.getModel().getValueAt(table.convertRowIndexToModel(itsRow), columnContainingUrl);
			HyperLinker.displayURL(helper.getJiraUrl(jira) + "/browse/" + jira);
		}
	}
}