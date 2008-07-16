package com.jonas.agile.devleadtool.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.panel.InternalFrameTabPanel;

public class InternalFrame extends JInternalFrame {
	private static List<InternalFrame> internalFrames = new ArrayList<InternalFrame>();

	private InternalFrameTabPanel content;
	private String realTitle;

	public InternalFrame(final PlannerHelper client) {
		super("", true, true, true, true);
		this.setTitle(createTitle(client));
		internalFrames.add(this);
		content = new InternalFrameTabPanel(this, client);
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
}
