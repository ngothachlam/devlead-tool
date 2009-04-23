package com.jonas.agile.devleadtool.gui.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTable;
import org.jfree.util.Log;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.gui.component.frame.main.MainFrame;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import com.jonas.agile.devleadtool.sprint.SprintCreationTarget;
import com.jonas.agile.devleadtool.sprint.SprintLengthCalculationTarget;
import com.jonas.agile.devleadtool.sprint.table.JXSprintTableModel;
import com.jonas.agile.devleadtool.sprint.table.ListSelectionListenerImpl;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;

public class SprintManagerGuiAction extends BasicAbstractGUIAction {

   @Override
   protected void finalize() throws Throwable {
      throw new RuntimeException("Method not implemented yet!");
   }

   private MainFrame frame;
   private final PlannerHelper helper;
   private SprintCreationSource source;
   private final ExcelSprintDao sprintDao;
   private JXSprintTableModel sprintsTableModel;
   private ListSelectionListenerImpl tableSelectionListener;
   private JXTable sprintsTable;
   
   public SprintManagerGuiAction(Frame parentFrame, PlannerHelper helper, ExcelSprintDao sprintDao) {
      super("Add Sprint", "Add a Sprint to be Cached!", parentFrame);
      this.helper = helper;
      this.sprintDao = sprintDao;
      frame = new MainFrame("Sprint Manager");
      JPanel contentPanel = getContentPane();
      frame.setContentPane(contentPanel);
   }

   private JPanel getContentPane() {
      JPanel viewSprint = getSprintsPanel(BorderFactory.createTitledBorder("View Sprints"));
      JPanel addSprint = getAddPanel(BorderFactory.createTitledBorder("Add Sprint"));

      JPanel contentPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      SwingUtil.defaultGridBagConstraints(gbc);

      gbc.fill = gbc.BOTH;
      gbc.weightx = 1.0;
      gbc.weighty = 1.0;
      viewSprint.setPreferredSize(new Dimension(300, 100));
      contentPanel.add(viewSprint, gbc);
      gbc.fill = gbc.NONE;
      gbc.weightx = 0.0;
      gbc.weighty = 0.0;
      gbc.gridy++;
      contentPanel.add(addSprint, gbc);
      return contentPanel;
   }

   private JPanel getSprintsPanel(AbstractBorder titledBorder) {
      JPanel panel = new JPanel(new BorderLayout());

      sprintsTableModel = new JXSprintTableModel();
      sprintsTable = new JXTable(sprintsTableModel);
      sprintsTable.setColumnControlVisible(true);

      tableSelectionListener = new ListSelectionListenerImpl();
      sprintsTable.getSelectionModel().addListSelectionListener(tableSelectionListener);
      sprintsTable.setColumnSelectionAllowed(false);
      sprintsTable.setRowSelectionAllowed(true);

      JScrollPane scrollpane = new JScrollPane(sprintsTable);
      panel.add(scrollpane, BorderLayout.CENTER);

      return panel;
   }

   private JPanel getAddPanel(AbstractBorder titledBorder) {
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      SwingUtil.defaultGridBagConstraints(gbc);

      JTextField nameTextField = addTextField(panel, "Name:", gbc);
      JXDatePicker startDatePicker = addDateField(panel, "Start:", gbc);
      JXDatePicker endDatePicker = addDateField(panel, "End:", gbc);
      CalculateSprintLengthAction calculateLengthAction = new CalculateSprintLengthAction(frame);
      addButton(panel, "", gbc, calculateLengthAction);
      JTextField lengthTextField = addTextField(panel, "Length:", gbc);

      tableSelectionListener.setSourceTable(sprintsTable);
      tableSelectionListener.setTargets(nameTextField, startDatePicker, endDatePicker, lengthTextField);
      
      SprintCreationTarget target = new SprintCreationTargetImpl(helper, sprintDao, sprintsTableModel);
      source = new SprintCreationSourceImpl(nameTextField, startDatePicker, endDatePicker, lengthTextField);
      AddSprintAction addSprintAction = new AddSprintAction(getParentFrame(), source, target);
      calculateLengthAction.setLengthSource(source);
      calculateLengthAction.setLengthTarget(new SprintLengthCalculationTargetImpl(lengthTextField));

      SprintCacheListener listener = new SprintCacheListenerImpl(frame);
      addSprintAction.addListener(listener);

      gbc.gridx = 0;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      panel.add(new JXButton(addSprintAction), gbc);

      panel.setBorder(titledBorder);
      return panel;
   }

   private JComponent add_Component(JPanel panel, String labelText, GridBagConstraints gbc, JComponent fieldield) {
      gbc.gridx = 0;
      panel.add(new JXLabel(labelText), gbc);
      gbc.gridx++;
      double preweight = gbc.weightx;
      int prefill = gbc.fill;
      gbc.fill = gbc.VERTICAL;
      gbc.weightx = 1;
      panel.add(fieldield, gbc);
      gbc.fill = prefill;
      gbc.weightx = preweight;
      gbc.gridy++;
      return fieldield;
   }

   private JXButton addButton(JPanel panel, String labelText, GridBagConstraints gbc, Action action) {
      return (JXButton) add_Component(panel, labelText, gbc, new JXButton(action));
   }

   private JXDatePicker addDateField(JPanel panel, String labelText, GridBagConstraints gbc) {
      return (JXDatePicker) add_Component(panel, labelText, gbc, new JXDatePicker());
   }

   private JTextField addTextField(JPanel panel, String labelText, GridBagConstraints gbc) {
      return (JTextField) add_Component(panel, labelText, gbc, new JTextField(13));
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      source.clear();
      frame.pack();
      SwingUtil.centreWindowWithinWindow(frame, getParentFrame());
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
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

   private final JXDatePicker endDatePicker;
   private final JTextField lengthTextField;
   private final JTextField nameTextField;
   private final JXDatePicker startDatePicker;

   public SprintCreationSourceImpl(JTextField nameTextField, JXDatePicker startDatePicker, JXDatePicker endDatePicker, JTextField lengthTextField) {
      this.nameTextField = nameTextField;
      this.startDatePicker = startDatePicker;
      this.endDatePicker = endDatePicker;
      this.lengthTextField = lengthTextField;
   }

   @Override
   public void clear() {
      nameTextField.setText("");
      startDatePicker.setDate(null);
      endDatePicker.setDate(null);
      lengthTextField.setText("");
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
   public Date getStart() {
      return startDatePicker.getDate();
   }
}


class SprintCreationTargetImpl implements SprintCreationTarget {

   private final ExcelSprintDao dao;
   private final PlannerHelper helper;
   private final JXSprintTableModel sprintTableModel;
   private static final Logger log = MyLogger.getLogger(SprintCreationTargetImpl.class);

   public SprintCreationTargetImpl(PlannerHelper helper, ExcelSprintDao dao, JXSprintTableModel sprintTableModel) {
      this.helper = helper;
      this.dao = dao;
      this.sprintTableModel = sprintTableModel;
   }

   @Override
   public void addOrSetSprint(Sprint sprint) throws IOException {
      SprintCache sprintCache = SprintCache.getInstance();

      Sprint oldSprint = sprintCache.getSprintWithName(sprint.getName());
      log.debug("new sprint has name: " + sprint.getName() + " and we found oldSprint " + oldSprint);
      boolean saveToDao;
      if (oldSprint != null) {
         oldSprint.setStartDate(sprint.getStartDate());
         oldSprint.setEndDate(sprint.getEndDate());
         oldSprint.setLength(sprint.getLength());
         saveToDao = true;
      } else {
         saveToDao = sprintCache.cache(sprint, true);
      }
      
      if (saveToDao) {
         dao.save(sprintCache, helper.getSprintFile());
      } else {
         AlertDialog.alertMessage(helper.getParentFrame(), "Did not add Sprint. Are you sure this sprint (name: "+sprint.getName()+") has not been used already?");
      }

      sprintTableModel.fireTableDataChanged();
   }
}


class SprintLengthCalculationTargetImpl implements SprintLengthCalculationTarget {

   private final JTextField lengthTextField;

   public SprintLengthCalculationTargetImpl(JTextField lengthTextField) {
      this.lengthTextField = lengthTextField;
   }

   @Override
   public void setWorkingDays(Integer days) {
      lengthTextField.setText(days.toString());
   }

}