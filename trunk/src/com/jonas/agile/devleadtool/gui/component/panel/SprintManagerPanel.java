package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.AbstractBorder;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.action.AddSprintAction;
import com.jonas.agile.devleadtool.gui.action.CalculateSprintLengthAction;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.gui.component.frame.main.MainFrame;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.SprintCacheListener;
import com.jonas.agile.devleadtool.sprint.SprintCacheNotification;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import com.jonas.agile.devleadtool.sprint.SprintCreationTarget;
import com.jonas.agile.devleadtool.sprint.SprintLengthCalculationTarget;
import com.jonas.agile.devleadtool.sprint.table.JXSprintTableModel;
import com.jonas.agile.devleadtool.sprint.table.ListSelectionListenerImpl;
import com.jonas.agile.devleadtool.sprint.table.SprintsTableHighlighter;
import com.jonas.agile.devleadtool.sprint.table.SprintsTablePopupMenu;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyPanel;
import com.jonas.common.swing.SwingUtil;

public class SprintManagerPanel extends MyPanel {

	private MainFrame frame;
	private final PlannerHelper helper;
	private SprintCreationSourceImpl sprintCreationSourcesource;
	private final ExcelSprintDao sprintDao;
	private JXSprintTableModel sprintsTableModel;
	private ListSelectionListenerImpl tableSelectionListener;
	private JXTable sprintsTable;
	private SprintsTableHighlighter sprintsTableHighlighter;

	public SprintManagerPanel(PlannerHelper helper, ExcelSprintDao sprintDao, Frame parentFrame) {
		super(new GridBagLayout());
		this.helper = helper;
		this.sprintDao = sprintDao;
		JPanel viewSprint = getSprintsPanel(BorderFactory.createTitledBorder("View Sprints"));
		SprintCreationTargetImpl sprintCreationTarget = new SprintCreationTargetImpl(helper, sprintDao, sprintsTableModel, sprintsTable);
		
		sprintCreationSourcesource = new SprintCreationSourceImpl();
		
		AddSprintAction addSprintAction = new AddSprintAction(parentFrame, sprintCreationSourcesource, sprintCreationTarget);
		SprintCacheListenerImpl sprintCacheListenerImpl = new SprintCacheListenerImpl(frame);
		addSprintAction.addListener(sprintCacheListenerImpl);
		
		tableSelectionListener.setSourceTable(sprintsTable);
		
		CalculateSprintLengthAction calculateSprintLengthAction = new CalculateSprintLengthAction(frame);
		calculateSprintLengthAction.setLengthSource(sprintCreationSourcesource);
		
		SprintLengthCalculationTargetImpl sprintLengthCalculationTargetImpl = new SprintLengthCalculationTargetImpl();
		calculateSprintLengthAction.setLengthTarget(sprintLengthCalculationTargetImpl);

		JPanel addSprint = new SprintManagerAddPanel(tableSelectionListener, BorderFactory.createTitledBorder("Add Sprint"), sprintCreationSourcesource,
				calculateSprintLengthAction, addSprintAction, sprintLengthCalculationTargetImpl);

		GridBagConstraints gbc = new GridBagConstraints();
		SwingUtil.defaultGridBagConstraints(gbc);

		gbc.fill = gbc.BOTH;
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		viewSprint.setPreferredSize(new Dimension(300, 250));
		add(viewSprint, gbc);

//		gbc.fill = gbc.NONE;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridy++;
		add(addSprint, gbc);
	}

	private JPanel getSprintsPanel(AbstractBorder titledBorder) {
		JPanel panel = new JPanel(new BorderLayout());

		sprintsTableModel = new JXSprintTableModel();
		sprintsTable = new JXTable(sprintsTableModel);
		sprintsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sprintsTable.setColumnControlVisible(true);
		sprintsTable.packAll();

		sprintsTableHighlighter = new SprintsTableHighlighter(sprintsTable);
		sprintsTable.addHighlighter(sprintsTableHighlighter);
		new SprintsTablePopupMenu(sprintsTable, frame, sprintDao, helper);

		tableSelectionListener = new ListSelectionListenerImpl();
		sprintsTable.getSelectionModel().addListSelectionListener(tableSelectionListener);
		sprintsTable.setColumnSelectionAllowed(false);
		sprintsTable.setRowSelectionAllowed(true);

		JScrollPane scrollpane = new JScrollPane(sprintsTable);
		panel.add(scrollpane, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createTitledBorder("Sprints"));

		return panel;
	}

	public void initialiseForViewing() {
		tableSelectionListener.setSprintCache(helper.getSprintCache());
		sprintsTableHighlighter.setSprintCache(helper.getSprintCache());
		sprintsTableModel.setSprintCache(helper.getSprintCache());
		sprintsTableModel.fireTableStructureChanged();
		sprintCreationSourcesource.clear();
	}

}

class SprintCacheListenerImpl implements SprintCacheListener {

	private ProgressDialog dialog;
	private Frame parent;

	public SprintCacheListenerImpl(Frame parent) {
		super();
		this.parent = parent;
	}

	@Override
	public void notify(SprintCacheNotification notification) {
		switch (notification) {
		case REQUIREDFIELDMISSING:
			dialog.setCompleteWithDelay(100);
			AlertDialog.alertMessage(parent, "Required fields not filled out!");
			break;
		case ADDINGTOCACHE:
			System.out.println("ADDINGTOCACHE creating dialog");
			dialog = new ProgressDialog(parent, "running...", "note", 0, true);
			System.out.println("done creating dialog");
			break;
		case ADDEDTOCACHE:
			System.out.println("ADDEDTOCACHE is null?");
			if (dialog != null) {
				System.out.println("not null");
				dialog.setCompleteWithDelay(100);
			}
			break;
		}
	}
}

class SprintCreationSourceImpl implements SprintCreationSource {

	private JXDatePicker endDatePicker;
	private JTextField lengthTextField;
	private JTextField nameTextField;
	private JXDatePicker startDatePicker;
	private JTextArea noteTextArea;

	public void setSources(JTextField nameTextField, JXDatePicker startDatePicker, JXDatePicker endDatePicker, JTextField lengthTextField,
			JTextArea noteTextArea) {
		this.nameTextField = nameTextField;
		this.startDatePicker = startDatePicker;
		this.endDatePicker = endDatePicker;
		this.lengthTextField = lengthTextField;
		this.noteTextArea = noteTextArea;
	}

	public SprintCreationSourceImpl() {
	}

	@Override
	public void clear() {
		nameTextField.setText("");
		startDatePicker.setDate(null);
		endDatePicker.setDate(null);
		lengthTextField.setText("");
		noteTextArea.setText("");
	}

	@Override
	public Date getEnd() {
		return endDatePicker.getDate();
	}

	@Override
	public Integer getLength() {
		String text = lengthTextField.getText();
		if (text.trim().length() == 0)
			return null;
		return new Integer(text);
	}

	@Override
	public String getName() {
		return nameTextField.getText();
	}

	@Override
	public String getNote() {
		return noteTextArea.getText();
	}

	@Override
	public Date getStart() {
		return startDatePicker.getDate();
	}
}

class SprintCreationTargetImpl implements SprintCreationTarget {

	private final ExcelSprintDao dao;
	private final PlannerHelper helper;
	private final JXSprintTableModel sprintTableModel;
	private static final Logger log = MyLogger.getLogger(SprintCreationTargetImpl.class);
	private final JXTable sprintTable;

	public SprintCreationTargetImpl(PlannerHelper helper, ExcelSprintDao dao, JXSprintTableModel sprintTableModel, JXTable sprintTable) {
		this.helper = helper;
		this.dao = dao;
		this.sprintTableModel = sprintTableModel;
		this.sprintTable = sprintTable;
	}

	@Override
	public void addOrSetSprint(Sprint sprint) throws IOException {
		SprintCache sprintCache = helper.getSprintCache();

		Sprint oldSprint = sprintCache.getSprintWithName(sprint.getName());
		log.debug("new sprint has name: " + sprint.getName() + " and we found oldSprint " + oldSprint);
		boolean saveToDao;
		if (oldSprint != null) {
			oldSprint.setStartDate(sprint.getStartDate());
			oldSprint.setEndDate(sprint.getEndDate());
			oldSprint.setLength(sprint.getLength());
			oldSprint.setNote(sprint.getNote());
			sprintCache.sort();
			saveToDao = true;
		} else {
			saveToDao = sprintCache.cache(sprint, true);
		}

		File excelFile = helper.getExcelFile();
		if (excelFile == null) {
			saveToDao = false;
		}

		log.debug("dao: " + dao + " excelFile: " + excelFile + " sprintCache: " + sprintCache);
		if (saveToDao) {
			dao.save(excelFile, sprintCache);
		}

		int[] selectedRows = sprintTable.getSelectedRows();
		sprintTableModel.fireTableDataChanged();

		for (int counter = 0; counter < selectedRows.length; counter++) {
			int selectedRow = selectedRows[counter];
			sprintTable.addRowSelectionInterval(selectedRow, selectedRow);

		}
	}
}

class SprintLengthCalculationTargetImpl implements SprintLengthCalculationTarget {

	private JTextField lengthTextField;

	public SprintLengthCalculationTargetImpl() {
	}

	@Override
	public void setWorkingDays(Integer days) {
		lengthTextField.setText(days.toString());
	}

	public void setTarget(JTextField lengthTextField) {
		this.lengthTextField = lengthTextField;
	}

}