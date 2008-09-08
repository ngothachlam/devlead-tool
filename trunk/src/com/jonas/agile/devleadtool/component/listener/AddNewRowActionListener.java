/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.string.MyStringParser;

public class AddNewRowActionListener implements ActionListener {
	private final MyTable table;
	private final JTextField jiraPrefix;
	private JTextComponent jiraCommas;

	private List<AddNewRowActionListenerListener> listeners = new ArrayList<AddNewRowActionListenerListener>();

	public AddNewRowActionListener(MyTable table, JTextField jiraPrefix, JTextField jiraCommas, AddNewRowActionListenerListener listener) {
		this.table = table;
		this.jiraPrefix = jiraPrefix;
		this.jiraCommas = jiraCommas;
		addListener(listener);
	}

	public void actionPerformed(@SuppressWarnings("unused")
	ActionEvent e) {
		MyStringParser parser = new MyStringParser();
		List<String> jiras = parser.separateString(jiraCommas.getText());
		for (String jiraNumber : jiras) {
			Vector<String> vector = new Vector<String>();
			MyTableModel model = (MyTableModel) table.getModel();

			String prefix = jiraPrefix.getText();
			String jiraString = getJiraString(prefix, jiraNumber);
			vector.add(jiraString);
			model.addRow(vector);
			int itsRow = table.getModel().getRowCount() - 1;
			int itsColumn = table.getModel().getColumnCount() - 1;
			for (AddNewRowActionListenerListener listener : listeners) {
				listener.addedNewRow(jiraString, itsRow, itsColumn);
			}
		}
		for (AddNewRowActionListenerListener listener : listeners) {
		   listener.addedNewRowsCompleted();
		}
	}

	public void addListener(AddNewRowActionListenerListener listener) {
		if (listener != null)
			listeners.add(listener);
	}

	protected String getJiraString(String jiraPrefix, String jiraNumber) {
		jiraPrefix = (jiraPrefix != null && jiraPrefix.trim().length() > 0 ? jiraPrefix : "");
		jiraNumber = (jiraNumber != null ? jiraNumber : "");
		return jiraPrefix + (isHyphenRequired(jiraPrefix, jiraNumber) ? "-" : "") + jiraNumber;
	}

	protected boolean isHyphenRequired(String jiraPrefix, String jiraNumber) {
		return !isEmpty(jiraPrefix) && !isEmpty(jiraNumber);
	}

	private boolean isEmpty(String jiraPrefix) {
		return (jiraPrefix == null || jiraPrefix.trim().length() == 0);
	}
}