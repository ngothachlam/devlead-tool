package com.jonas.common;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyComponentPanel extends MyPanel {

	private List<MyComponentPanel> changeListeners = new ArrayList<MyComponentPanel>();
	private List<MyComponentPanel> closeListeners = new ArrayList<MyComponentPanel>();
	
	public MyComponentPanel(LayoutManager layoutManager) {
		super(layoutManager);
	}
	
	protected final void initialise() {
		makeContent();
		wireUpListeners();
		loadPreferences();
		setButtons();		
	}
	
	protected void makeContent() {
		// override as needed
	}

	protected void wireUpListeners() {
		// override as needed
	}
	
	protected void setButtons() {
		// override as needed
	}
	
	protected void loadPreferences() {
		// override as needed
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
