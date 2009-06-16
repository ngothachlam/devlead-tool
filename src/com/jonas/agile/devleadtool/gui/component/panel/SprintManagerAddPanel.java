package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXLabel;

import com.jonas.agile.devleadtool.gui.action.AddSprintAction;
import com.jonas.agile.devleadtool.gui.action.CalculateSprintLengthAction;
import com.jonas.agile.devleadtool.sprint.table.ListSelectionListenerImpl;
import com.jonas.common.swing.MyPanel;
import com.jonas.common.swing.SwingUtil;

public class SprintManagerAddPanel extends MyPanel {

	public SprintManagerAddPanel(ListSelectionListenerImpl tableSelectionListener, TitledBorder titledBorder,
			SprintCreationSourceImpl sprintCreationSourceImpl, CalculateSprintLengthAction calculateSprintLengthAction, AddSprintAction addSprintAction,
			SprintLengthCalculationTargetImpl sprintLengthCalculationTargetImpl) {
		super(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		SwingUtil.defaultGridBagConstraints(gbc);
		
		JPanel panel = this;
		JTextField nameTextField = addTextField(panel, "Name:", gbc);
		JXDatePicker startDatePicker = addDateField(panel, "Start:", gbc);
		JXDatePicker endDatePicker = addDateField(panel, "End:", gbc);
		JTextField lengthTextField = addPanel(panel, "Length:", gbc, calculateSprintLengthAction);
		JTextArea noteTextArea = addTextArea(panel, "Note:", gbc);

		tableSelectionListener.setTargets(nameTextField, startDatePicker, endDatePicker, lengthTextField, noteTextArea);
		sprintCreationSourceImpl.setSources(nameTextField, startDatePicker, endDatePicker, lengthTextField, noteTextArea);

		sprintLengthCalculationTargetImpl.setTarget(lengthTextField);

		gbc.gridx = 0;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(new JXButton(addSprintAction), gbc);

		panel.setBorder(titledBorder);
	}

	private JTextArea addTextArea(JPanel panel, String string, GridBagConstraints gbc) {
		JTextArea textArea = getTextArea(3, 13);
		int savedGridY = gbc.gridy;
		gbc.gridy = 0;
		gbc.gridheight = savedGridY;
		add_Component(panel, string, gbc, new JScrollPane(textArea), 2);
		gbc.gridy = savedGridY;
		return textArea;
	}

	private JXDatePicker addDateField(JPanel panel, String labelText, GridBagConstraints gbc) {
		return (JXDatePicker) add_Component(panel, labelText, gbc, new JXDatePicker(), 0);
	}

	private JTextField addPanel(JPanel panel, String labelText, GridBagConstraints gbc, Action components) {
		JPanel subPanel = new JPanel(new BorderLayout());
		JTextField fieldield = new JTextField(6);
		subPanel.add(fieldield, BorderLayout.CENTER);
		JXButton comp = new JXButton(components);
		Insets margin = comp.getMargin();
		margin.set(margin.top, margin.left - 10, margin.bottom, margin.right - 10);
		comp.setMargin(margin);
		subPanel.add(comp, BorderLayout.EAST);
		add_Component(panel, labelText, gbc, subPanel, 0);
		return fieldield;
	}

	private JTextField addTextField(JPanel panel, String labelText, GridBagConstraints gbc) {
		return (JTextField) add_Component(panel, labelText, gbc, new JTextField(13), 0);
	}

	private JComponent add_Component(JPanel panel, String labelText, GridBagConstraints gbc, JComponent jComponent, int startX) {
		gbc.gridx = startX;
		gbc.weightx = 0d;
		JXLabel label = new JXLabel(labelText);
		panel.add(label, gbc);
		
		gbc.gridx++;
		int prefill = gbc.fill;
		gbc.fill = gbc.BOTH;
		gbc.weightx = 0.5d;
		panel.add(jComponent, gbc);
		
		gbc.fill = prefill;
		gbc.weightx = 0d;
		gbc.gridy++;
		return jComponent;
	}

}
