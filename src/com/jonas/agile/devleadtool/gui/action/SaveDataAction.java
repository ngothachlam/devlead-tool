package com.jonas.agile.devleadtool.gui.action;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.burndown.HistoricalBoardDao;
import com.jonas.agile.devleadtool.gui.component.frame.AbstractBasicFrame;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.sprint.Sprint;

public class SaveDataAction extends BasicAbstractGUIAction {

   private SaveDataFrame frame;

   public SaveDataAction(String name, String description, Frame parentFrame, MyTableModel boardModel, HistoricalBoardDao historicalBoardDao, PlannerHelper helper) {
      super(name, description, parentFrame);
      frame = new SaveDataFrame(parentFrame, boardModel, historicalBoardDao, helper);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }
}

class SaveDataFrame extends AbstractBasicFrame {

   private final MyTableModel boardModel;
   private final HistoricalBoardDao historicalBoardDao;
   private final PlannerHelper helper;
   private final Frame parentFrame;

   public SaveDataFrame(Frame parentFrame, MyTableModel boardModel, HistoricalBoardDao historicalBoardDao, PlannerHelper helper) {
      super(parentFrame, 200, 100);
      this.parentFrame = parentFrame;
      this.boardModel = boardModel;
      this.historicalBoardDao = historicalBoardDao;
      this.helper = helper;
   }

   private JTextField dayInSprintTextField;

   @Override
   public Container getMyPanel() {
      JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
      panel.add(new JLabel("Sprint Name:"));
      JTextField sprintName = new JTextField(9);
      sprintName.setEditable(false);
      sprintName.setToolTipText("To edit - 'Manage Sprints'!");
      panel.add(sprintName);

      panel.add(new JLabel("Day in Sprint:"));
      dayInSprintTextField = new JTextField(9);
      panel.add(dayInSprintTextField);

      JButton comp = new JButton(new SaveDataPerformAction("Save", "Save to file", parentFrame, boardModel, historicalBoardDao, helper, dayInSprintTextField));
      panel.add(new JLabel(""));
      panel.add(comp);

      Sprint currentSprint = helper.getSprintCache().getCurrentSprint();
      sprintName.setText(currentSprint == null ? "<NONE>" : currentSprint.getName());
      dayInSprintTextField.setText(currentSprint != null ? currentSprint.calculateDayInSprint().toString() : "");

      return panel;
   }

   public File getFileForHistoricalSave(File file, File file2) {
      return SaveDataPerformAction.getFileForHistoricalSave(file, file2);
   }
}

class SaveDataPerformAction extends BasicAbstractGUIAction {
   private final MyTableModel boardModel;
   private final HistoricalBoardDao historicalBoardDao;
   private final PlannerHelper helper;
   private final JTextField dayInSprintTextField;

   @Override
   public void doActionPerformed(ActionEvent e) {
      File file = getFileForHistoricalSave(helper.getSaveDirectory(), helper.getExcelFile());
      try {
         // FIXME - add a method that can check and warn if the data already exists. 
         historicalBoardDao.save(file, boardModel, Integer.parseInt(dayInSprintTextField.getText()), helper.getSprintCache().getCurrentSprint());
      } catch (IOException e1) {
         e1.printStackTrace();
         throw new RuntimeException(e1);
      }
   }

   protected static File getFileForHistoricalSave(File saveDirectory, File originalFile) {
      String orignalFileName = originalFile.getName();
      orignalFileName = orignalFileName.substring(0, orignalFileName.indexOf("."));
      return new File(saveDirectory, "HISTORICAL - " + orignalFileName + ".csv");
   }

   public SaveDataPerformAction(String name, String description, Frame parentFrame, MyTableModel boardModel, HistoricalBoardDao historicalBoardDao, PlannerHelper helper, JTextField dayInSprintTextField) {
      super(name, description, parentFrame);
      this.boardModel = boardModel;
      this.historicalBoardDao = historicalBoardDao;
      this.helper = helper;
      this.dayInSprintTextField = dayInSprintTextField;
   }
}
