package com.jonas.agile.devleadtool.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.component.panel.InternalFrameTabPanel;
import com.jonas.agile.devleadtool.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.component.panel.PlanPanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.component.table.model.TableModelBuilder;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.common.logging.MyLogger;

public class InternalFrame extends JInternalFrame {

   private static List<InternalFrame> internalFrames = new ArrayList<InternalFrame>();

   private static Logger log = MyLogger.getLogger(InternalFrame.class);

   private InternalFrameTabPanel internalFrameTabPanel;

   private String excelFile;

   private String originalTitle;
   private String originalTitleWithDuplicateNumber;

   private PlannerHelper client;

   public InternalFrame(PlannerHelper client, String title, InternalFrameTabPanel internalFrameTabPanel) {
      this(title, client);

      this.internalFrameTabPanel = internalFrameTabPanel;

      this.addInternalFrameListener(new MyInternalFrameListener(client, this));

      setContentPane(internalFrameTabPanel);
      client.setActiveInternalFrame(this);
   }

   InternalFrame(String title, PlannerHelper client) {
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
         InternalFrame internalFrame = internalFrames.get(internalFrames.size()-1);
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

   void close() {
      log.debug("Closing internal frame: " + getTitle());
      openSaveDialogForThisInternalFrame();
      if (internalFrameTabPanel != null) {
         internalFrameTabPanel.close();
      }
      internalFrames.remove(this);
      dispose();
   }

   private void openSaveDialogForThisInternalFrame() {
      log.debug("Should Open Save Dialog!!");
      final PlannerDAOExcelImpl plannerDAO = new PlannerDAOExcelImpl(new TableModelBuilder());
      new SavePlannerDialog(plannerDAO, this, client);
   }

   String createTitle(String title) {
      int countOfSameTitles = getCountWithSameTitle(title);
      return title + (countOfSameTitles > 1 ? " (" + (countOfSameTitles - 1) + ")" : "");
   }

   int getCountWithSameTitle(String title) {
      int count = 0;
      for (InternalFrame internalFrame : internalFrames) {
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

      private final InternalFrame frame;

      private MyInternalFrameListener(PlannerHelper client, InternalFrame frame) {
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