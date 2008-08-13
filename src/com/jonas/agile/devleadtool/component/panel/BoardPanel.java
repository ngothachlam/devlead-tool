package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import org.apache.log4j.Logger;
import com.ProgressDialog;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.HyperlinkTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.HyperLinker;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingWorker;
import com.jonas.common.logging.MyLogger;

public class BoardPanel extends MyComponentPanel {

	private BoardTableModel model;

	public String jira_url = "http://10.155.38.105/jira/browse/";

	private MyTable table;

	private final PlannerHelper helper;

	public BoardPanel(PlannerHelper client) {
		this(client, new BoardTableModel());
	}

	public BoardPanel(PlannerHelper helper, BoardTableModel boardModel) {
		super(new BorderLayout());
		this.helper = helper;
		makeContent(boardModel);
		setButtons();
		initialiseTableHeader();
	}

	protected void makeContent(BoardTableModel boardTableModel) {
		model = boardTableModel;

		table = new MyTable();
		table.setModel(model);

		table.setDefaultRenderer(String.class, new StringTableCellRenderer(model));
		table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer(model));
		table.setColumnRenderer(6, new HyperlinkTableCellRenderer(model));

		table.setDefaultEditor(Boolean.class, new CheckBoxTableCellEditor(model));
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable aTable = (JTable) e.getSource();
				int itsRow = aTable.rowAtPoint(e.getPoint());
				int itsColumn = aTable.columnAtPoint(e.getPoint());
				if (itsColumn == 6) {
					HyperLinker.displayURL(jira_url + (String) model.getValueAt(itsRow, itsColumn));
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				JTable aTable = (JTable) e.getSource();
				int itsRow = aTable.getEditingRow();
				int itsColumn = aTable.getEditingColumn();
				if (itsColumn == 0) {
					// TODO this is not very nice - doesn't work with keypressed for a long while (as it repeats) as well as
					// restricts input!
					model.fireTableRowsUpdated(itsRow, itsRow);
					char keyChar = e.getKeyChar();
					String valueAt = (String) model.getValueAt(itsRow, itsColumn);
					if (keyChar >= 48 && keyChar <= 57)
						model.setValueAt(valueAt + keyChar, itsRow, itsColumn);
					if (keyChar == 8) {
						if (valueAt != null && valueAt.length() > 0)
							model.setValueAt(valueAt.substring(0, valueAt.length() - 1), itsRow, itsColumn);
					}
					System.out.println("value  " + model.getValueAt(itsRow, itsColumn));
				}
			}
		});

		table.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(table);
		addCenter(scrollPane);
	}

	private void initialiseTableHeader() {
		JTableHeader header = table.getTableHeader();
		header.setReorderingAllowed(true);
	}

	protected void setButtons() {
//		MyPanel buttonPanel = SwingUtil.getGridPanel(0, 2, 5, 5).bordered();
		JPanel buttonPanel = new JPanel();

		addButton(buttonPanel, "Add", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addEmptyRow();
			}
		});

		addButton(buttonPanel, "Remove", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.removeSelectedRows(table);
			}
		});

		addButton(buttonPanel, "Copy to Plan", new ActionListener() {
			private Logger log = MyLogger.getLogger(this.getClass());

			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {
					public void run() {
						// AlertDialog messageDialog = AlertDialog.message(helper, "Copying... ");

						final int[] selectedRows = table.getSelectedRows();
						final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Copying...",
								"Copying selected messages from Board to Plan...", selectedRows.length);
						SwingWorker worker = new SwingWorker() {
							public Object construct() {
								for (int i = 0; i < selectedRows.length; i++) {
									Object valueAt = table.getValueAt(selectedRows[i], 6);
									helper.addToPlan((String) valueAt, false);
									dialog.increseProgress();
								}
								return null;
							}

							@Override
							public void finished() {
								dialog.setCompleteSoonish();
							}

						};
						worker.start();
						// messageDialog.addText("Done!");
					}
				});
				thread.start();
			}
		});

		addSouth(buttonPanel);
	}

	// public void setModel(BoardTableModel model) {
	// table.setModel(model);
	// }

	public MyTableModel getBoardModel() {
		return model;
	}

	public void setEditable(boolean selected) {
		model.setEditable(selected);
	}
}
