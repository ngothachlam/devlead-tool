package com.jonas.agile.devleadtool.controller.listener;

public interface AddNewRowActionListenerListener {

	public void addedNewRow(String jiraString, int itsRow, int itsColumn);
	public void addedNewRowsCompleted();

}
