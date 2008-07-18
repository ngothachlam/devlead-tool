package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.BoardTableCellEditor;
import com.jonas.agile.devleadtool.component.table.renderer.BoardTableCheckBoxCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.BoardTableDefaultCellRenderer;
import com.jonas.common.HyperLinker;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;

public class BoardPanel extends MyComponentPanel {

	private final class BoardTableMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			JTable aTable = (JTable) e.getSource();
			int itsRow = aTable.rowAtPoint(e.getPoint());
			int itsColumn = aTable.columnAtPoint(e.getPoint());
			if (itsColumn == 6) {
				HyperLinker.displayURL(jira_url + (String) model.getValueAt(itsRow, itsColumn));
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

	private BoardTableModel model;
	public String jira_url = "http://10.155.38.105/jira/browse/";
	private MyTable table;

	public BoardPanel(PlannerHelper client) {
		super(new BorderLayout());
		initialise();
	}

	@Override
	protected void makeContent() {
		model = new BoardTableModel();
		table = new MyTable(model);

//		table.setDefaultRenderer(Object.class, new BoardTableCheckBoxCellRenderer(model));
		table.setColumnRenderer(0, new BoardTableDefaultCellRenderer(model));
		table.setDefaultRenderer(Boolean.class, new BoardTableCheckBoxCellRenderer(model));

		table.setDefaultEditor(Boolean.class, new BoardTableCellEditor(model));

		table.addMouseListener(new BoardTableMouseListener());

		initialiseTableHeader();

		JScrollPane scrollPane = new JScrollPane(table);
		addCenter(scrollPane);
	}



	private void initialiseTableHeader() {
		JTableHeader header = table.getTableHeader();
		header.setUpdateTableInRealTime(true);

		header.setReorderingAllowed(true);
	}

	@Override
	protected void setButtons() {
		MyPanel buttonPanel = SwingUtil.getGridPanel(0, 2, 5, 5).bordered();

		JButton addRowButton = new JButton("Add");
		JButton removeRowButton = new JButton("Remove");

		addRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addEmptyRow();
			}
		});

		removeRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.removeSelectedRows(table);
			}
		});

		buttonPanel.add(addRowButton);
		buttonPanel.add(removeRowButton);
		addSouth(buttonPanel);
	}

	@Override
	protected void wireUpListeners() {
		super.wireUpListeners();
	}
}
