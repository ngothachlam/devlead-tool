package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.dnd.TableAndTitleDTO;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.guibuilding.basicdnd.MyPopupListener;

public class InternalTabPanel extends MyComponentPanel {

	private BoardPanel boardPanel;
	private PlanPanel planPanel;
	private JiraPanel jiraPanel;

	private JTabbedPane tabbedPane;

	private JCheckBox checkBox;

	public InternalTabPanel(PlannerHelper client) {
		this(client, null, null, null);
	}

	public InternalTabPanel(PlannerHelper client, BoardTableModel boardModel, PlanTableModel planModel, JiraTableModel jiraModel) {
		super(new BorderLayout());
		boardModel = (boardModel == null) ? new BoardTableModel() : boardModel;
		planModel = (planModel == null) ? new PlanTableModel() : planModel;
		jiraModel = (jiraModel == null) ? new JiraTableModel() : jiraModel;
		
		boardPanel = new BoardPanel(client, boardModel);
		planPanel = new PlanPanel(client, planModel);
		jiraPanel = new JiraPanel(client, jiraModel);
		
      TableAndTitleDTO tableAndTitleDTO1 = new TableAndTitleDTO("Board", boardPanel.getTable());
      TableAndTitleDTO tableAndTitleDTO2 = new TableAndTitleDTO("Plan", planPanel.getTable());
      TableAndTitleDTO tableAndTitleDTO3 = new TableAndTitleDTO("Jira", jiraPanel.getTable());
      
      MyTablePopupMenu myTablePopupMenu = new MyTablePopupMenu(tableAndTitleDTO1, tableAndTitleDTO2, tableAndTitleDTO3);
      MyPopupListener myPopupListener = new MyPopupListener(myTablePopupMenu);
      
      boardPanel.getTable().addMouseListener(myPopupListener);
      planPanel.getTable().addMouseListener(myPopupListener);
      jiraPanel.getTable().addMouseListener(myPopupListener);

		JPanel panel = new JPanel();
		checkBox = new JCheckBox("Editable?", true);
		panel.add(checkBox);
		addNorth(panel);

		makeContent(boardModel);
		wireUpListeners();
		this.setBorder(BorderFactory.createEmptyBorder(0, 2, 1, 0));
	}

	public void makeContent(MyTableModel boardTableModel) {
		tabbedPane = new JTabbedPane(JTabbedPane.VERTICAL);
		tabbedPane.add(boardPanel, "Board");
		tabbedPane.add(planPanel, "Plan");
		tabbedPane.add(jiraPanel, "Jira");
		addCenter(tabbedPane);
	}

	public void wireUpListeners() {
		// senderPanel.addComponentListener(this);
		// receiverPanel.addComponentListener(this);
		checkBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				boardPanel.setEditable(checkBox.isSelected());
				planPanel.setEditable(checkBox.isSelected());
				jiraPanel.setEditable(checkBox.isSelected());
			}
		});
	}

	protected void closing() {
		// client.disconnect();
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public JiraPanel getJiraPanel() {
		return jiraPanel;
	}

	public PlanPanel getPlanPanel() {
		return planPanel;
	}
}
