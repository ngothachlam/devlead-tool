package com.jonas.agile.devleadtool.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.component.panel.InternalFrameTabPanel;
import com.jonas.agile.devleadtool.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.component.panel.PlanPanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.logging.MyLogger;

public class InternalFrame extends JInternalFrame {

   private static List<InternalFrame> internalFrames = new ArrayList<InternalFrame>();

   private static Logger log = MyLogger.getLogger(InternalFrame.class);

   private InternalFrameTabPanel content;

   private String excelFile;

   private String originalTitle;
   private String originalTitleWithDuplicateNumber;

   public InternalFrame(PlannerHelper client, String title, BoardTableModel boardModel, PlanTableModel planModel) {
      this(title);

      content = new InternalFrameTabPanel(client, boardModel, planModel);

      this.addInternalFrameListener(new MyInternalFrameListener(client, this));
      setContentPane(content);
      client.setActiveInternalFrame(this);
   }

   public InternalFrame(String title) {
      super("", true, true, true, true);
      internalFrames.add(this);
      this.originalTitle = title;
      originalTitleWithDuplicateNumber = createTitle(title);
      log.debug("created and setting Title: " + originalTitleWithDuplicateNumber);
      this.setTitle(originalTitleWithDuplicateNumber);
   }

   public static void closeAll() {
      for (Iterator<InternalFrame> iterator = internalFrames.iterator(); iterator.hasNext();) {
         iterator.next().close();
         iterator.remove();
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
      return content.getBoardPanel();
   }

   private JiraPanel getJiraPanel() {
      return content.getJiraPanel();
   }

   private PlanPanel getPlanPanel() {
      return content.getPlanPanel();
   }

   void close() {
      if (content != null)
         content.close();
      internalFrames.remove(this);
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
