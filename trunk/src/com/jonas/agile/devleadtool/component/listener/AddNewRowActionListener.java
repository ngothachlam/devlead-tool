/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	public AddNewRowActionListener(MyTable table, JTextField jiraPrefix, JTextField jiraCommas) {
		this.table = table;
		this.jiraPrefix = jiraPrefix;
		this.jiraCommas = jiraCommas;
	}

	public void actionPerformed(@SuppressWarnings("unused")
	ActionEvent e) {
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