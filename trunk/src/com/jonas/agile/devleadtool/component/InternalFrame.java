package com.jonas.agile.devleadtool.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.component.panel.InternalFrameTabPanel;
import com.jonas.agile.devleadtool.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.component.panel.PlanPanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.jira.JiraIssue;

public class InternalFrame extends JInternalFrame {
   private static List<InternalFrame> internalFrames = new ArrayList<InternalFrame>();

   public static void closeAll() {
      for (Iterator<InternalFrame> iterator = internalFrames.iterator(); iterator.hasNext();) {
         iterator.next().close();
         iterator.remove();
      }
   }

   private final PlannerHelper client;

   private InternalFrameTabPanel content;

   private String title;

   public InternalFrame(final PlannerHelper client, String title) {
      this(client, title, null, null);
   }

   public InternalFrame(PlannerHelper client, String title, BoardTableModel boardModel, PlanTableModel planModel) {
      super("", true, true, true, true);
      this.client = client;
      this.title = title;

      this.setTitle(createTitle());

      internalFrames.add(this);

      content = new InternalFrameTabPanel(this, client, boardModel, planModel);

      this.addInternalFrameListener(new MyInternalFrameListener(client, this));
      setContentPane(content);
      client.setActiveInternalFrame(this);
   }

   private void close() {
      content.close();
      internalFrames.remove(this);
   }

   protected String createTitle() {
      int countOfSameTitles = getCountWithSameTitle(title);
      return title + (countOfSameTitles > 0 ? " (" + countOfSameTitles + ")" : "");
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

   private int getCountWithSameTitle(String title) {
      int count = 0;
      for (Iterator<InternalFrame> iterator = internalFrames.iterator(); iterator.hasNext();) {
         if (title.equalsIgnoreCase(iterator.next().title)) {
            count++;
         }
      }
      return count;
   }

   public String getExcelFile() {
      return content.getExcelFile();
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

   public void setExcelFile(String name) {
      content.setExcelFile(name);
   }

   public void setFileName(String fileName) {
      this.setTitle(this.getTitle() + " - ..." + getRightMostFromString(fileName, 35));
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
