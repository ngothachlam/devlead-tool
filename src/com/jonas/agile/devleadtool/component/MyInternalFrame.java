package com.jonas.agile.devleadtool.component;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.PlannerListeners;
import com.jonas.agile.devleadtool.component.dialog.SavePlannerDialog;
import com.jonas.agile.devleadtool.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.component.panel.MyInternalFrameInnerComponent;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.common.logging.MyLogger;

public class MyInternalFrame extends JInternalFrame {

   private static List<MyInternalFrame> internalFrames = new ArrayList<MyInternalFrame>();

   static Logger log = MyLogger.getLogger(MyInternalFrame.class);

   private PlannerDAO dao;

   private File excelFile;
   private PlannerHelper helper;
   private MyInternalFrameInnerComponent internalFrameTabPanel;
   private String originalTitle;
   private String originalTitleWithDuplicateNumber;
   public MyInternalFrame(final PlannerHelper client, String title, MyInternalFrameInnerComponent internalFrameTabPanel, PlannerDAO dao, SavePlannerDialog savePlannerDialog, SaveKeyListener saveKeyListener, MyDesktopPane desktop) {
      this(title, client);
      this.dao = dao;
      this.internalFrameTabPanel = internalFrameTabPanel;
      desktop.addInternalFrame(this);
      
      setContentPane(internalFrameTabPanel);
      setFocusable(true);
      client.setActiveInternalFrame(this);

      wireListeners(client, savePlannerDialog, saveKeyListener);
      
      PlannerListeners.notifyListenersThatFrameWasCreated(this);
   }
   
   MyInternalFrame(String title, PlannerHelper helper) {
      super("", true, true, true, true);
      this.helper = helper;
      internalFrames.add(this);
      this.originalTitle = title;
      originalTitleWithDuplicateNumber = createTitle(title);
      log.debug("created and setting Title: " + originalTitleWithDuplicateNumber);
      this.setTitle(originalTitleWithDuplicateNumber);
   }
   
   public static void closeAll() throws PropertyVetoException {
      if (!SwingUtilities.isEventDispatchThread()) {
         closeInEventThread();
      } else {
         closeAllAgain();
      }
   }

   private static void closeAllAgain() throws PropertyVetoException {
      for (int i = internalFrames.size() - 1; i >= 0; i--) {
         MyInternalFrame internalFrame = internalFrames.get(i);
         internalFrame.setClosed(true);
         internalFrames.remove(internalFrame);
      }
   }

   private static void closeInEventThread() throws PropertyVetoException {
      try {
         SwingRunnable doRun = new SwingRunnable();
         SwingUtilities.invokeAndWait(doRun);
         if (!doRun.isAllClosed()) {
            throw new PropertyVetoException("Cancelled!", null);
         }
      } catch (InterruptedException e) {
         e.printStackTrace();
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      }
   }

   public MyTableModel getBoardModel() {
      return getBoardPanel().getModel();
   }

   public MyTable getBoardTable() {
      return getBoardPanel().getTable();

   }

   public File getExcelFile() {
      return excelFile;
   }

   public File getFile() {
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

   public String getRightMostFromString(String string, int i) {
      return string.length() > i ? string.substring(string.length() - i, string.length()) : string;
   }

   public void saveModels(final PlannerDAO dao) {
      SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
         @Override
         protected Object doInBackground() throws Exception {
            try {
               dao.saveBoardModel(getBoardModel());
               dao.saveJiraModel(getJiraModel());
            } catch (IOException e) {
               AlertDialog.alertException(helper.getParentFrame(), e);
            } 
            return null;
         }
      };
      worker.execute();
      try {
         worker.get();
      } catch (InterruptedException e) {
         e.printStackTrace();
      } catch (ExecutionException e) {
         e.printStackTrace();
      }
   }

   public void setSaveFile(File file) {
      setExcelFile(file, CutoverLength.DEFAULT);
   }

   @Override
   public void setTitle(String title) {
      super.setTitle(title);
      PlannerListeners.notifyListenersThatFrameChangedTitle(this);
   }

   private BoardPanel getBoardPanel() {
      return internalFrameTabPanel.getBoardPanel();
   }

   private JiraPanel getJiraPanel() {
      return internalFrameTabPanel.getJiraPanel();
   }

   private void wireListeners(final PlannerHelper client, SavePlannerDialog savePlannerDialog, SaveKeyListener saveKeyListener) {
      MyInternalFrameListener myInternalFrameListener = new MyInternalFrameListener(this, helper);
      VetoListener vetoListener = new VetoListener(this, client.getParentFrame(), savePlannerDialog);
      
      addInternalFrameListener( myInternalFrameListener);
      addVetoableChangeListener(vetoListener);
      addKeyListener(saveKeyListener);
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

   void setExcelFile(File file, CutoverLength cutoverLength) {
      excelFile = file;
      setTitleFileName(file.getAbsolutePath(), cutoverLength);
   }

   void setTitleFileName(String fileName, CutoverLength cutoverLength) {
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
      private PlannerHelper helper;
      private MyInternalFrame internalFrame;
      public MyInternalFrameListener(MyInternalFrame internalFrame, PlannerHelper helper) {
         super();
         this.internalFrame = internalFrame;
         this.helper = helper;
      }
      @Override
      public void internalFrameActivated(InternalFrameEvent e) {
         helper.setActiveInternalFrame(internalFrame);
      }
   }

   private static final class SwingRunnable implements Runnable {
      private boolean allClosed;

      public boolean isAllClosed() {
         return allClosed;
      }

      @Override
      public void run() {
         try {
            closeAllAgain();
            allClosed = true;
         } catch (PropertyVetoException e) {
            e.printStackTrace();
            allClosed = false;
         }
      }
   }

   private final class VetoListener implements VetoableChangeListener {

      private MyInternalFrame internalFrame;
      private Component parent;
      private SavePlannerDialog savePlannerDialog;

      public VetoListener(MyInternalFrame internalFrame, Component parent, SavePlannerDialog savePlannerDialog) {
         super();
         this.internalFrame = internalFrame;
         this.parent = parent;
         this.savePlannerDialog = savePlannerDialog;
      }

      @Override
      public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
         if (evt.getPropertyName().equals(IS_CLOSED_PROPERTY)) {
            boolean changed = ((Boolean) evt.getNewValue()).booleanValue();
            if (changed) {
               int resultFromDialog = JOptionPane.showOptionDialog(parent, "Save " + internalFrame.getTitle() + "?", "Save Confirmation",
                     JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

               switch (resultFromDialog) {
               case JOptionPane.YES_OPTION:
                  saveWithoutConfirmation(internalFrame);
               case JOptionPane.NO_OPTION:
                  PlannerListeners.notifyListenersThatFrameWasClosed(internalFrame);
                  internalFrames.remove(this);
                  break;
               case JOptionPane.CANCEL_OPTION:
                  throw new PropertyVetoException("Cancelled!", null);
               default:
                  break;
               }
            }
         }
      }

      private void saveWithoutConfirmation(MyInternalFrame frameClosing) {
         if (dao != null && helper != null) {
            savePlannerDialog.save(frameClosing, false);
         }
      }
   }
}
