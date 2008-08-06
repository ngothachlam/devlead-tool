package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import com.ProgressDialog;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.BoardTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.HyperlinkTableCellRenderer;
import com.jonas.common.HyperLinker;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;
import com.jonas.common.SwingWorker;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

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

		table.setColumnRenderer(0, new StringTableCellRenderer(model));
		table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer(model));
		table.setColumnRenderer(6, new HyperlinkTableCellRenderer(model));

		table.setDefaultEditor(Boolean.class, new BoardTableCellEditor(model));
		table.addMouseListener(new BoardTableMouseListener());
		
		table.setAutoCreateRowSorter(true);


		JTableHeader header = table.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.setReorderingAllowed(true);

		JScrollPane scrollPane = new JScrollPane(table);
		addCenter(scrollPane);
	}

	private void initialiseTableHeader() {
		JTableHeader header = table.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.setReorderingAllowed(true);
	}

	protected void setButtons() {
		MyPanel buttonPanel = SwingUtil.getGridPanel(0, 2, 5, 5).bordered();

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
									helper.addToPlan((String) valueAt);
									dialog.increseProgress();
								}
								return null;
							}
							@Override
							public void finished() {
								dialog.setComplete();
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

	public BoardTableModel getBoardModel() {
		return model;
	}
}
