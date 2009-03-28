package com.jonas.agile.devleadtool.component.listener;

public interface AddNewRowActionListenerListener {

	public void addedNewRow(String jiraString, int itsRow, int itsColumn);
	public void addedNewRowsCompleted();

}
