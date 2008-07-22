package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.MyComponentPanel;

public class InternalFrameTabPanel extends MyComponentPanel {

	private BoardPanel boardPanel;

	private JPanel planPanel;

	private JPanel jiraPanel;

	private JTabbedPane tabbedPane;

	public InternalFrameTabPanel(InternalFrame parent, PlannerHelper client) {
		this(parent, client, null);
	}

	public InternalFrameTabPanel(InternalFrame frame, PlannerHelper client, BoardTableModel model) {
		super(new BorderLayout());
		if (model == null)
			model = new BoardTableModel();
		boardPanel = new BoardPanel(client, model);
		planPanel = new PlanPanel(client);
		jiraPanel = new JiraPanel(client);

		makeContent(model);
		wireUpListeners();
		loadPreferences();
		setButtons();
	}

	public void makeContent(BoardTableModel boardTableModel) {
		tabbedPane = new JTabbedPane(JTabbedPane.VERTICAL);
		tabbedPane.add(boardPanel, "Board");
		tabbedPane.add(planPanel, "Plan");
		tabbedPane.add(jiraPanel, "Jira");
		addCenter(tabbedPane);
	}

	public void wireUpListeners() {
		// senderPanel.addComponentListener(this);
		// receiverPanel.addComponentListener(this);
	}

	protected void closing() {
		// client.disconnect();
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}
}
