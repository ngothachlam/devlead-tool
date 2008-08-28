/**
 * 
 */
package com.jonas.agile.devleadtool.component.panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTable;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;

final class TableKeyListenerOnJira extends KeyAdapter {
	/**
	 * 
	 */
	private final BoardPanel TableKeyListenerOnJiraUpdate;
	private Logger log = MyLogger.getLogger(TableKeyListenerOnJira.class);

	/**
	 * @param boardPanel
	 */
	TableKeyListenerOnJira(BoardPanel boardPanel) {
		TableKeyListenerOnJiraUpdate = boardPanel;
	}

	public void keyTyped(KeyEvent e) {
		// FIXME is there a better way of doing this - not very nice!
		JTable aTable = (JTable) e.getSource();
		int itsRow = aTable.getEditingRow();
		int itsColumn = aTable.getEditingColumn();
		log.debug("itsRow: " + itsRow + "itsColumn: " + itsColumn);
		if (TableKeyListenerOnJiraUpdate.table.getColumnName(itsColumn).equals(BoardTableModel.COLUMNNAME_JIRA)) {
			char keyChar = e.getKeyChar();
			String valueAt = (String) ((MyTableModel) TableKeyListenerOnJiraUpdate.table.getModel()).getValueAt(itsRow, itsColumn);
			if (keyChar >= 48 && keyChar <= 57)
				TableKeyListenerOnJiraUpdate.table.setValueAt(valueAt + keyChar, itsRow, itsColumn);
			if (keyChar == 8) {
				if (valueAt != null && valueAt.length() > 0)
					TableKeyListenerOnJiraUpdate.table.setValueAt(valueAt.substring(0, valueAt.length() - 1), itsRow, itsColumn);
			}
			log.debug("value  " + TableKeyListenerOnJiraUpdate.table.getValueAt(itsRow, itsColumn));
		}
	}
}