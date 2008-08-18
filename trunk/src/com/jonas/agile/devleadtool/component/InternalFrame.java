package com.jonas.agile.devleadtool.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.panel.InternalFrameTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.jira.JiraIssue;

public class InternalFrame extends JInternalFrame {
	private final class MyInternalFrameListener extends InternalFrameAdapter {
		private final PlannerHelper client;

		private final InternalFrame frame;

		private MyInternalFrameListener(PlannerHelper client, InternalFrame frame) {
			this.client = client;
			this.frame = frame;
		}

		public void internalFrameActivated(InternalFrameEvent e) {
			client.setActiveInternalFrame(frame);
		}

		public void internalFrameClosing(InternalFrameEvent ife) {
			close();
		}
	}

	private static List<InternalFrame> internalFrames = new ArrayList<InternalFrame>();

	private InternalFrameTabPanel content;

	private String title;

	private final PlannerHelper client;

	public InternalFrame(final PlannerHelper client, String title) {
		this(client, title, null, null, null);
	}

	public InternalFrame(PlannerHelper client, String title, BoardTableModel boardModel, PlanTableModel planModel,
			JiraTableModel jiraModel) {
		super("", true, true, true, true);
		this.client = client;
		this.title = title;

		this.setTitle(createTitle());

		internalFrames.add(this);

		content = new InternalFrameTabPanel(this, client, boardModel, planModel, jiraModel);

		this.addInternalFrameListener(new MyInternalFrameListener(client, this));
		setContentPane(content);
		client.setActiveInternalFrame(this);
	}

	private void close() {
		content.close();
		internalFrames.remove(this);
	}

	public static void closeAll() {
		for (Iterator<InternalFrame> iterator = internalFrames.iterator(); iterator.hasNext();) {
			iterator.next().close();
			iterator.remove();
		}
	}

	public int getInternalFramesCount() {
		return internalFrames.size();
	}

	private int getCountWithSameTitle(String title) {
		int count = 0;
		for (Iterator<InternalFrame> iterator = internalFrames.iterator(); iterator.hasNext();) {
			if (title.equalsIgnoreCase(iterator.next().title)) {
				count++;
			}
		}
		return count;
	}

	protected String createTitle() {
		int countOfSameTitles = getCountWithSameTitle(title);
		return title + (countOfSameTitles > 0 ? " (" + countOfSameTitles + ")" : "");
	}

	public MyTableModel getBoardModel() {
		return content.getBoardPanel().getModel();
	}

	public void setExcelFile(String name) {
		content.setExcelFile(name);
	}

	public String getExcelFile() {
		return content.getExcelFile();
	}

	public JiraTableModel getJiraModel() {
		return content.getJiraPanel().getJiraModel();
	}

	public PlanTableModel getPlanModel() {
		return content.getPlanPanel().getPlanModel();
	}

	public void addToPlan(JiraIssue jiraIssue) {
		getPlanModel().addRow(jiraIssue);
	}

	public void setFileName(String fileName) {
		this.setTitle(this.getTitle() + " - ..." + getRightMostFromString(fileName, 35));
	}

	public String getRightMostFromString(String string, int i) {
		return string.length() > i ? string.substring(string.length() - i, string.length()) : string;
	}
}
