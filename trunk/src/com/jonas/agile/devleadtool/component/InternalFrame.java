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
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;

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

	private String realTitle;

	private final PlannerHelper client;

	public InternalFrame(final PlannerHelper client) {
		this(client, null);
	}

	public InternalFrame(PlannerHelper client, BoardTableModel model) {
		super("", true, true, true, true);
		this.client = client;
		this.setTitle(createTitle(client));

		internalFrames.add(this);

		content = new InternalFrameTabPanel(this, client, model);

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
			if (title.equalsIgnoreCase(iterator.next().realTitle)) {
				count++;
			}
		}
		return count;
	}

	private String createTitle(final PlannerHelper panel) {
		realTitle = panel.getTitle();
		int countOfSameTitles = getCountWithSameTitle(realTitle);
		return realTitle + (countOfSameTitles > 0 ? " (" + countOfSameTitles + ")" : "");
	}

	public BoardTableModel getBoardModel() {
		return content.getBoardPanel().getBoardModel();
	}

	public JiraTableModel getJiraModel() {
		return null;
	}

	public PlanTableModel getPlanModel() {
		return null;
	}
}
