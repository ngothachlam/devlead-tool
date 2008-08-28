package com.jonas.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.jonas.agile.devleadtool.component.listener.AddNewRowActionListener;
import com.jonas.agile.devleadtool.component.listener.AddNewRowActionListenerListener;
import com.jonas.agile.devleadtool.component.listener.RemoveJTableSelectedRowsListener;
import com.jonas.agile.devleadtool.component.table.MyTable;

public class MyPanel extends JPanel {

	public MyPanel(LayoutManager layoutManager) {
		super(layoutManager);
	}

	public final void addNorth(Component component) {
		super.add(component, BorderLayout.NORTH);
	}

	public final void addEast(Component component) {
		super.add(component, BorderLayout.EAST);
	}

	public final void addSouth(Component component) {
		super.add(component, BorderLayout.SOUTH);
	}

	public final void addWest(Component component) {
		super.add(component, BorderLayout.WEST);
	}

	public final void addCenter(Component component) {
		super.add(component, BorderLayout.CENTER);
	}

	public final MyPanel bordered() {
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return this;
	}

	public final MyPanel bordered(int top, int left, int bottom, int right) {
		setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		return this;
	}
	
	public final MyPanel borderedHighlightingThisPanels(){
		setBorder(BorderFactory.createLineBorder(Color.GREEN));
		return this;
	}

	public final MyPanel bordered(Border border) {
		setBorder(border);
		return this;
	}
	
	public JButton addButton(JPanel buttonPanel, String string, ActionListener listener) {
		JButton button = new JButton(string);
		button.addActionListener(listener);
		buttonPanel.add(button);
		return button;
	}

	protected JLabel addLabel(JPanel buttons, String labelText) {
		JLabel label = new JLabel(labelText);
		buttons.add(label);
		return label;
	}

	protected JTextField addTextField(JPanel buttons, int textFieldLength) {
		JTextField jiraCommas = new JTextField(textFieldLength);
		buttons.add(jiraCommas);
		return jiraCommas;
	}

	protected void addPanelWithAddAndRemoveOptions(MyTable table, JPanel buttons, AddNewRowActionListenerListener listener) {
		addLabel(buttons, "Prefix:");
		JTextField jiraPrefix = addTextField(buttons, 5);
		JTextField jiraCommas = addTextField(buttons, 20);
		addButton(buttons, "Add", new AddNewRowActionListener(table, jiraPrefix, jiraCommas, listener));
		addButton(buttons, "Remove", new RemoveJTableSelectedRowsListener(table));
	}
}
