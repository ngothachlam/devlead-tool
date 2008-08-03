package com.jonas.common;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;

public class MyComponentPanel extends MyPanel {

	private List<MyComponentPanel> changeListeners = new ArrayList<MyComponentPanel>();
	private List<MyComponentPanel> closeListeners = new ArrayList<MyComponentPanel>();
	
	public MyComponentPanel(LayoutManager layoutManager) {
		super(layoutManager);
	}
	
	protected void savePreferences() {
		// override as needed
	}

	public final void close() {
		for(Iterator<MyComponentPanel> iterator = closeListeners.iterator(); iterator.hasNext(); ) {
			iterator.next().close();
		}
		savePreferences();
		closing();
	}
	
	protected void closing() {
		// override as needed
	}
	
	protected void fireComponentChanged() {
		componentChanged();
		for(Iterator<MyComponentPanel> iterator = changeListeners.iterator(); iterator.hasNext(); ) {
			iterator.next().fireComponentChanged();
		}
	}
	
	protected void componentChanged() {
		// override as needed
	}

	public final void addComponentListener(MyComponentPanel panel) {
		changeListeners.add(panel);
		panel.closeListeners.add(this);
	}

	public final void removeComponentListener(MyComponentPanel panel) {
		changeListeners.remove(panel);
		panel.closeListeners.remove(this);
	}
}
