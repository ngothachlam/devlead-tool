package com.jonas.agile.devleadtool.component;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.component.panel.InternalTabPanel;
import com.jonas.agile.devleadtool.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.component.panel.PlanPanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.data.DaoListenerEvent;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.common.logging.MyLogger;

public class MyInternalFrame extends JInternalFrame {

   private static List<MyInternalFrame> internalFrames = new ArrayList<MyInternalFrame>();

   static Logger log = MyLogger.getLogger(MyInternalFrame.class);

   public static void closeAll() {
      while (internalFrames.size() > 0) {
         MyInternalFrame internalFrame = internalFrames.get(internalFrames.size() - 1);
         internalFrame.close();
      }
   }

   private PlannerHelper client;

   private PlannerDAO dao;
   private String excelFile;
   private InternalTabPanel internalFrameTabPanel;

   private String originalTitle;

   private String originalTitleWithDuplicateNumber;

   private DaoListener daoListener;

   public MyInternalFrame(final PlannerHelper client, String title, InternalTabPanel internalFrameTabPanel, PlannerDAO dao) {
      this(title, client);
      this.dao = dao;
      this.internalFrameTabPanel = internalFrameTabPanel;
      this.addInternalFrameListener(new MyInternalFrameListener(client, this));

      setContentPane(internalFrameTabPanel);
      setFocusable(true);
      client.setActiveInternalFrame(this);

      daoListener = new DaoListener() {
         private ProgressDialog dialog;

            @Override
            public void notify(DaoListenerEvent event, String message) {
               switch (event) {
               case SavingStarted:
                  dialog = new ProgressDialog(client.getParentFrame(), "Saving Planner", "Saving Planner", 0);
                  break;
               case SavingModelStarted:
                  dialog.setNote(message);
                  break;
               case SavingFinished:
                  dialog.setNote(message);
                  dialog.setCompleteWithDelay(300);
                  break;
               default:
                  break;
               }
            }
      };
      addKeyListener(new SaveKeyListener(dao, client.getParentFrame(), client, daoListener));
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

   void close() {
      if (internalFrameTabPanel != null) {
         int result = showConfirmDialogAndGetResults();
         log.debug(result + " Yes: " + JOptionPane.YES_OPTION + " No: " + JOptionPane.NO_OPTION + " Cancel: " + JOptionPane.CANCEL_OPTION);
         switch (result) {
         case JOptionPane.YES_OPTION:
            saveBoardsWithoutConfirmation();
            closeHard();
            break;
         case JOptionPane.NO_OPTION:
            closeHard();
            break;
         case JOptionPane.CANCEL_OPTION:
            // FIXME doesn't work - the cancel cannot be rewoked - needs fixing.
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

   public MyTableModel getBoardModel() {
      return getBoardPanel().getModel();
   }

   private BoardPanel getBoardPanel() {
      return internalFrameTabPanel.getBoardPanel();
   }

   public MyTable getBoardTable() {
      return getBoardPanel().getTable();

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

   public String getExcelFile() {
      return excelFile;
   }

   public int getInternalFramesCount() {
      return internalFrames.size();
   }

   public MyTableModel getJiraModel() {
      return getJiraPanel().getJiraModel();
   }

   private JiraPanel getJiraPanel() {
      return internalFrameTabPanel.getJiraPanel();
   }

   public MyTable getJiraTable() {
      return getJiraPanel().getTable();
   }

   public PlanTableModel getPlanModel() {
      return getPlanPanel().getPlanModel();
   }

   private PlanPanel getPlanPanel() {
      return internalFrameTabPanel.getPlanPanel();
   }

   public MyTable getPlanTable() {
      return getPlanPanel().getTable();
   }

   public String getRightMostFromString(String string, int i) {
      return string.length() > i ? string.substring(string.length() - i, string.length()) : string;
   }

   private void saveBoardsWithoutConfirmation() {
      if (dao != null && client != null) {
         new SavePlannerDialog(dao, this, client, false, daoListener);
      }
   }

   public void setExcelFile(String fileName, CutoverLength cutoverLength) {
      excelFile = fileName;
      setFileName(fileName, cutoverLength);
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

   private int showConfirmDialogAndGetResults() {
      return JOptionPane.showConfirmDialog(internalFrameTabPanel, "Want to Save " + getTitle() + "?", "Save?", JOptionPane.YES_NO_OPTION);
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
