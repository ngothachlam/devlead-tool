package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXLabel;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.frame.main.MainFrame;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import com.jonas.agile.devleadtool.sprint.SprintCreationTarget;
import com.jonas.common.swing.SwingUtil;

public class SprintManagerGuiAction extends BasicAbstractGUIAction {

   private MainFrame frame;
   private final PlannerHelper helper;
   private SprintCreationSource source;
   private final ExcelSprintDao sprintDao;

   public SprintManagerGuiAction(Frame parentFrame, PlannerHelper helper, ExcelSprintDao sprintDao) {
      super("Add Sprint", "Add a Sprint to be Cached!", parentFrame);
      this.helper = helper;
      this.sprintDao = sprintDao;
      frame = new MainFrame("Sprint Manager");
      JPanel contentPanel = getContentPane();
      frame.setContentPane(contentPanel);
   }

   private JPanel getContentPane() {
      JPanel addSprint = getAddPanel(BorderFactory.createTitledBorder("Add Sprint"));
      JPanel viewSprint = getSprintsPanel(BorderFactory.createTitledBorder("View Sprints"));
      
      JPanel contentPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      SwingUtil.defaultGridBagConstraints(gbc);

      contentPanel.add(viewSprint, gbc);
      gbc.gridy++;
      contentPanel.add(addSprint, gbc);
      return contentPanel;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      source.clear();
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   private JXDatePicker addDateField(JPanel panel, String labelText, GridBagConstraints gbc) {
      gbc.gridx = 0;
      panel.add(new JXLabel(labelText), gbc);
      gbc.gridx++;
      JXDatePicker startDatePicker = new JXDatePicker();
      panel.add(startDatePicker, gbc);
      gbc.gridy++;
      return startDatePicker;
   }

   private JTextField addTextField(JPanel panel, String labelText, GridBagConstraints gbc) {
      gbc.gridx = 0;
      panel.add(new JXLabel(labelText), gbc);
      gbc.gridx++;
      JTextField nameTextField = new JTextField(20);
      panel.add(nameTextField, gbc);
      gbc.gridy++;
      return nameTextField;
   }

   private JPanel getAddPanel(AbstractBorder titledBorder) {
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      SwingUtil.defaultGridBagConstraints(gbc);

      JTextField nameTextField = addTextField(panel, "Name:", gbc);
      JXDatePicker startDatePicker = addDateField(panel, "Start:", gbc);
      JXDatePicker endDatePicker = addDateField(panel, "End:", gbc);
      JTextField lengthTextField = addTextField(panel, "Length:", gbc);

      source = new SprintCreationSourceImpl(nameTextField, startDatePicker, endDatePicker, lengthTextField);
      SprintCreationTarget target = new SprintCreationTargetImpl(helper, sprintDao);
      AddSprintAction addSprintAction = new AddSprintAction(getParentFrame(), source, target);

      gbc.gridx = 0;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      panel.add(new JXButton(addSprintAction), gbc);

      panel.setBorder(titledBorder);
      return panel;
   }

   private JPanel getSprintsPanel(AbstractBorder titledBorder) {
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      SwingUtil.defaultGridBagConstraints(gbc);

      JTextField nameTextField = addTextField(panel, "Sprint Count:", gbc);

      gbc.gridx = 0;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      Action refreshAction = new RefreshAction(getParentFrame(), nameTextField);
      panel.add(new JXButton(refreshAction), gbc);

      panel.setBorder(titledBorder);
      return panel;
   }
}


class RefreshAction extends BasicAbstractGUIAction {
   private JTextComponent nameTextField;

   public RefreshAction(Frame parentFrame, JTextComponent nameTextField) {
      super("Refresh Sprint count", "", parentFrame);
      this.nameTextField = nameTextField;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      nameTextField.setText(""+SprintCache.getInstance().getSprints().size());
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
   public int getLength() {
      return new Integer(lengthTextField.getText());
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