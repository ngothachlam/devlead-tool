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

   private static Logger log = MyLogger.getLogger(InternalFrame.class);

   private static List<InternalFrame> internalFrames = new ArrayList<InternalFrame>();

   public static void closeAll() {
      for (Iterator<InternalFrame> iterator = internalFrames.iterator(); iterator.hasNext();) {
         iterator.next().close();
         iterator.remove();
      }
   }

   private InternalFrameTabPanel content;

   private String originalTitle;
   private String originalTitleWithDuplicateNumber;

   private String excelFile;

   public InternalFrame(String title) {
      super("", true, true, true, true);
      internalFrames.add(this);
      this.originalTitle = title;
      originalTitleWithDuplicateNumber = createTitle(title);
      log.debug("created and setting Title: " + originalTitleWithDuplicateNumber);
      this.setTitle(originalTitleWithDuplicateNumber);
   }

   public InternalFrame(PlannerHelper client, String title, BoardTableModel boardModel, PlanTableModel planModel) {
      this(title);

      content = new InternalFrameTabPanel(client, boardModel, planModel);

      this.addInternalFrameListener(new MyInternalFrameListener(client, this));
      setContentPane(content);
      client.setActiveInternalFrame(this);
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

   public MyTableModel getBoardModel() {
      return getBoardPanel().getModel();
   }

   private BoardPanel getBoardPanel() {
      return content.getBoardPanel();
   }

   public MyTable getBoardTable() {
      return getBoardPanel().getTable();

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

   public String getExcelFile() {
      return excelFile;
   }

   public int getInternalFramesCount() {
      return internalFrames.size();
   }

   private JiraPanel getJiraPanel() {
      return content.getJiraPanel();
   }

   public MyTable getJiraTable() {
      return getJiraPanel().getTable();
   }

   public PlanTableModel getPlanModel() {
      return getPlanPanel().getPlanModel();
   }

   private PlanPanel getPlanPanel() {
      return content.getPlanPanel();
   }

   public MyTable getPlanTable() {
      return getPlanPanel().getTable();
   }

   public String getRightMostFromString(String string, int i) {
      return string.length() > i ? string.substring(string.length() - i, string.length()) : string;
   }

   public void setExcelFile(String fileName) {
      excelFile = fileName;
      setFileName(fileName, 35);
   }

   void setFileName(String fileName, int cutoverLength) {
      this.setTitle(this.originalTitleWithDuplicateNumber + " - ..." + getRightMostFromString(fileName, cutoverLength));
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
