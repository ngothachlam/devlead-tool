package com.jonas.agile.devleadtool.component;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.component.panel.PlanPanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.common.logging.MyLogger;

public class MyInternalFrame extends JInternalFrame {

   private static List<MyInternalFrame> internalFrames = new ArrayList<MyInternalFrame>();

   private static Logger log = MyLogger.getLogger(MyInternalFrame.class);

   private PlannerHelper client;

   private PlannerDAO dao;

   private String excelFile;
   private InternalTabPanel internalFrameTabPanel;

   private String originalTitle;

   private String originalTitleWithDuplicateNumber;

   public MyInternalFrame(PlannerHelper client, String title, InternalTabPanel internalFrameTabPanel, PlannerDAO dao) {
      this(title, client);
      this.dao = dao;

      this.internalFrameTabPanel = internalFrameTabPanel;

      this.addInternalFrameListener(new MyInternalFrameListener(client, this));

      setContentPane(internalFrameTabPanel);
      client.setActiveInternalFrame(this);
   }

   MyInternalFrame(String title, PlannerHelper client) {
      super("", true, true, true, true);
      this.client = client;
      internalFrames.add(this);
      this.originalTitle = title;
      originalTitleWithDuplicateNumber = createTitle(title);
      log.debug("created and setting Title: " + originalTitleWithDuplicateNumber);
      this.setTitle(originalTitleWithDuplicateNumber);
   }

   public static void closeAll() {
      while (internalFrames.size() > 0) {
         MyInternalFrame internalFrame = internalFrames.get(internalFrames.size() - 1);
         internalFrame.close();
      }
   }

   public MyTableModel getBoardModel() {
      return getBoardPanel().getModel();
   }

   public MyTable getBoardTable() {
      return getBoardPanel().getTable();

   }

   public String getExcelFile() {
      return excelFile;
   }

   public int getInternalFramesCount() {
      return internalFrames.size();
   }

   public MyTableModel getJiraModel() {
      return getJiraPanel().getJiraModel();
   }

   public MyTable getJiraTable() {
      return getJiraPanel().getTable();
   }

   public PlanTableModel getPlanModel() {
      return getPlanPanel().getPlanModel();
   }

   public MyTable getPlanTable() {
      return getPlanPanel().getTable();
   }

   public String getRightMostFromString(String string, int i) {
      return string.length() > i ? string.substring(string.length() - i, string.length()) : string;
   }

   public void setExcelFile(String fileName, CutoverLength cutoverLength) {
      excelFile = fileName;
      setFileName(fileName, cutoverLength);
   }

   private BoardPanel getBoardPanel() {
      return internalFrameTabPanel.getBoardPanel();
   }

   private JiraPanel getJiraPanel() {
      return internalFrameTabPanel.getJiraPanel();
   }

   private PlanPanel getPlanPanel() {
      return internalFrameTabPanel.getPlanPanel();
   }

   private void openSaveDialogForThisInternalFrame() {
      log.debug("Should Open Save Dialog!!");
      if (dao != null && client != null) {
         new SavePlannerDialog(dao, this, client);
      }
   }

   void close() {
      if (internalFrameTabPanel != null) {
         int result = JOptionPane.showConfirmDialog(internalFrameTabPanel, "Want to Save " + getTitle() + "?", "Save?", JOptionPane.YES_NO_OPTION);
         log.debug(result + " Yes: " + JOptionPane.YES_OPTION + " No: " + JOptionPane.NO_OPTION + " Cancel: " + JOptionPane.CANCEL_OPTION);
         switch (result) {
         case JOptionPane.YES_OPTION:
            openSaveDialogForThisInternalFrame();
            closeHard();
            break;
         case JOptionPane.NO_OPTION:
            closeHard();
            break;
         case JOptionPane.CANCEL_OPTION:
            //FIXME doesn't work - the cancel cannot be rewoked - needs fixing.
            break;
         default:
            break;
         }
      }
   }

   private void closeHard() {
      internalFrameTabPanel.close();
      internalFrames.remove(this);
      dispose();
   }

   String createTitle(String title) {
      int countOfSameTitles = getCountWithSameTitle(title);
      return title + (countOfSameTitles > 1 ? " (" + (countOfSameTitles - 1) + ")" : "");
   }

   int getCountWithSameTitle(String title) {
      int count = 0;
      for (MyInternalFrame internalFrame : internalFrames) {
         String checkedTitle = internalFrame.originalTitle;
         log.debug("The checked internalFrames title is: " + checkedTitle + " and is it the same as the checked title: " + title);
         if (checkedTitle.equalsIgnoreCase(title)) {
            count++;
         }
      }
      return count;
   }

   void setFileName(String fileName, CutoverLength cutoverLength) {
      String rightMostFromString = getRightMostFromString(fileName, cutoverLength.value());
      StringBuffer sb = new StringBuffer(this.originalTitleWithDuplicateNumber);
      sb.append(" - ");
      if (rightMostFromString.length() < fileName.length()) {
         sb.append("...");
      }
      sb.append(rightMostFromString);
      this.setTitle(sb.toString());
   }

   private final class MyInternalFrameListener extends InternalFrameAdapter {
      private final PlannerHelper client;

      private final MyInternalFrame frame;

      private MyInternalFrameListener(PlannerHelper client, MyInternalFrame frame) {
         this.client = client;
         this.frame = frame;
      }

      public void internalFrameActivated(InternalFrameEvent e) {
         client.setActiveInternalFrame(frame);
      }

      public void internalFrameClosing(InternalFrameEvent ife) {
         close();
      }
   }

}
