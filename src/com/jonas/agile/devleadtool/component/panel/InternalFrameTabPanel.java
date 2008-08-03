package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.common.MyComponentPanel;

public class InternalFrameTabPanel extends MyComponentPanel {

	private BoardPanel boardPanel;

	private PlanPanel planPanel;

	private JiraPanel jiraPanel;

	private JTabbedPane tabbedPane;

   private JTextField excelFile = new JTextField(35);

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

		JPanel panel = new JPanel();
		panel.add(new JLabel("File:"));
		excelFile.setEditable(false);
      panel.add(excelFile);
      addNorth(panel);
      
		makeContent(model);
		wireUpListeners();
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

   public void setExcelFile(String fileName) {
      excelFile.setText(fileName);
   }

   public String getExcelFile() {
      return excelFile.getText();
   }

   public JiraPanel getJiraPanel() {
      return jiraPanel;
   }
   
   public PlanPanel getPlanPanel() {
      return planPanel;
   }
}
