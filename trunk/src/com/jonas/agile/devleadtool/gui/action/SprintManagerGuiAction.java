package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXLabel;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.gui.component.frame.main.MainFrame;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import com.jonas.agile.devleadtool.sprint.SprintCreationTarget;
import com.jonas.agile.devleadtool.sprint.SprintLengthTarget;
import com.jonas.common.swing.SwingUtil;

public class SprintManagerGuiAction extends BasicAbstractGUIAction {

   private MainFrame frame;
   private final PlannerHelper helper;
   private SprintCreationSource source;
   private final ExcelSprintDao sprintDao;
   private JComboBox sprintsCombo;

   public SprintManagerGuiAction(Frame parentFrame, PlannerHelper helper, ExcelSprintDao sprintDao) {
      super("Add Sprint", "Add a Sprint to be Cached!", parentFrame);
      this.helper = helper;
      this.sprintDao = sprintDao;
      frame = new MainFrame("Sprint Manager");
      JPanel contentPanel = getContentPane();
      frame.setContentPane(contentPanel);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      source.clear();
      frame.pack();
      SwingUtil.centreWindowWithinWindow(frame, getParentFrame());
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   private JComponent add_Component(JPanel panel, String labelText, GridBagConstraints gbc, JComponent fieldield) {
      gbc.gridx = 0;
      panel.add(new JXLabel(labelText), gbc);
      gbc.gridx++;
      int prefill = gbc.fill;
      gbc.fill = gbc.VERTICAL;
      panel.add(fieldield, gbc);
      gbc.fill = prefill;
      gbc.gridy++;
      return fieldield;
   }
   
   private JXButton addButton(JPanel panel, String labelText, GridBagConstraints gbc, Action action) {
      return (JXButton) add_Component(panel, labelText, gbc, new JXButton(action));
   }

   private JComboBox addCheckBoxField(JPanel panel, String labelText, Vector<Sprint> contents, GridBagConstraints gbc) {
      return (JComboBox) add_Component(panel, labelText, gbc, new JComboBox(contents));
   }


   private JXDatePicker addDateField(JPanel panel, String labelText, GridBagConstraints gbc) {
      return (JXDatePicker) add_Component(panel, labelText, gbc, new JXDatePicker());
   }

   private JTextField addTextField(JPanel panel, String labelText, GridBagConstraints gbc) {
      return (JTextField) add_Component(panel, labelText, gbc, new JTextField(13));
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

      SprintCreationTarget target = new SprintCreationTargetImpl(helper, sprintDao);
      source = new SprintCreationSourceImpl(nameTextField, startDatePicker, endDatePicker, lengthTextField);
      AddSprintAction addSprintAction = new AddSprintAction(getParentFrame(), source, target);
      calculateLengthAction.setLengthSource(source);
      calculateLengthAction.setLengthTarget(new SprintLengthTargetImpl(lengthTextField));

      SprintCacheListener listener = new SprintCacheListenerImpl(frame, sprintsCombo);
      addSprintAction.addListener(listener);

      gbc.gridx = 0;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      panel.add(new JXButton(addSprintAction), gbc);

      panel.setBorder(titledBorder);
      return panel;
   }

   private JPanel getContentPane() {
      JPanel viewSprint = getSprintsPanel(BorderFactory.createTitledBorder("View Sprints"));
      JPanel addSprint = getAddPanel(BorderFactory.createTitledBorder("Add Sprint"));

      JPanel contentPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      SwingUtil.defaultGridBagConstraints(gbc);

      contentPanel.add(viewSprint, gbc);
      gbc.gridy++;
      contentPanel.add(addSprint, gbc);
      return contentPanel;
   }

   private JPanel getSprintsPanel(AbstractBorder titledBorder) {
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      SwingUtil.defaultGridBagConstraints(gbc);

      sprintsCombo = addCheckBoxField(panel, "Sprint Count:", SprintCache.getInstance().getSprints(), gbc);

      // gbc.gridx = 0;
      // gbc.gridwidth = 2;
      // gbc.anchor = GridBagConstraints.CENTER;
      // Action refreshAction = new RefreshAction(getParentFrame(), sprintsCombo);
      // panel.add(new JXButton(refreshAction), gbc);

      panel.setBorder(titledBorder);
      return panel;
   }
}


// class RefreshAction extends BasicAbstractGUIAction {
// private JComboBox sprintsCombo;
//
// public RefreshAction(Frame parentFrame, JComboBox sprintsCombo) {
// super("Refresh Sprint count", "", parentFrame);
// this.sprintsCombo = sprintsCombo;
// }
//
// @Override
// public void doActionPerformed(ActionEvent e) {
// // sprintsCombo.set
//
// // sprintsCombo.setText(""+SprintCache.getInstance().getSprints().size());
// }
// }

class SprintCacheListenerImpl implements SprintCacheListener {

   private JComboBox combo;
   private ProgressDialog dialog;
   private Frame parent;

   public SprintCacheListenerImpl(Frame parent, JComboBox combo) {
      super();
      this.parent = parent;
      this.combo = combo;
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
         ComboBoxModel model = combo.getModel();
         Object anObject = new Object();
         DefaultComboBoxModel dModel = (DefaultComboBoxModel) model;
         dModel.addElement(anObject);
         dModel.removeElement(anObject);
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

   public SprintCreationTargetImpl(PlannerHelper helper, ExcelSprintDao dao) {
      this.helper = helper;
      this.dao = dao;
   }

   @Override
   public void addSprint(Sprint sprint) throws IOException {
      SprintCache sprintCache = SprintCache.getInstance();
      sprintCache.cache(sprint);
      dao.save(sprintCache, helper.getSprintFile());
   }
}

class SprintLengthTargetImpl implements SprintLengthTarget{

   private final JTextField lengthTextField;

   public SprintLengthTargetImpl(JTextField lengthTextField) {
      this.lengthTextField = lengthTextField;
   }

   @Override
   public void setWorkingDays(Integer days) {
      lengthTextField.setText(days.toString());
   }
   
}