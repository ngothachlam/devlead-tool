package com.jonas.agile.devleadtool.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.component.panel.InternalFrameTabPanel;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class InternalFrame extends JInternalFrame {
	private final class MyInternalFrameListener implements InternalFrameListener {
		private final PlannerHelper client;
		private final InternalFrame frame;

		private MyInternalFrameListener(PlannerHelper client, InternalFrame frame) {
			this.client = client;
			this.frame = frame;
		}

		public void internalFrameActivated(InternalFrameEvent e) {
			client.setPlanner(frame);
		}

		public void internalFrameClosed(InternalFrameEvent e) {
		}

		public void internalFrameClosing(InternalFrameEvent e) {
		}

		public void internalFrameDeactivated(InternalFrameEvent e) {
		}

		public void internalFrameDeiconified(InternalFrameEvent e) {
		}

		public void internalFrameIconified(InternalFrameEvent e) {
		}

		public void internalFrameOpened(InternalFrameEvent e) {
		}
	}

	private static List<InternalFrame> internalFrames = new ArrayList<InternalFrame>();

	private InternalFrameTabPanel content;

	private String realTitle;

	public InternalFrame(final PlannerHelper client) {
		super("", true, true, true, true);
		client.setPlanner(this);
		this.setTitle(createTitle(client));
		internalFrames.add(this);
		content = new InternalFrameTabPanel(this, client);
		wireUpListeners();
		setContentPane(content);
		
		this.addInternalFrameListener(new MyInternalFrameListener(client, this));
	}

	public InternalFrame(PlannerHelper client, BoardTableModel model) {
		super("", true, true, true, true);
		this.setTitle(createTitle(client));
		internalFrames.add(this);
		System.out.println("Creating Internal Frame with new Model!");
		content = new InternalFrameTabPanel(this, client, model);
		wireUpListeners();
		setContentPane(content);
	}

	protected void wireUpListeners() {
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent ife) {
				closing();
				remove();
			}
		});
	}

	private void closing() {
		content.close();
	}

	private void remove() {
		internalFrames.remove(this);
	}

	public static void closeAll() {
		for (Iterator<InternalFrame> iterator = internalFrames.iterator(); iterator.hasNext();) {
			iterator.next().closing();
			iterator.remove();
		}
	}

	public int getInternalFramesCount() {
		return internalFrames.size();
	}

	private int getCountWithSameTitle(String title) {
		int count = 0;
		for (Iterator<InternalFrame> iterator = internalFrames.iterator(); iterator.hasNext();) {
			if (title.equalsIgnoreCase(iterator.next().getRealTitle())) {
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

	public String getRealTitle() {
		return realTitle;
	}

	public BoardPanel getBoardPanel() {
		return content.getBoardPanel();
	}
}
