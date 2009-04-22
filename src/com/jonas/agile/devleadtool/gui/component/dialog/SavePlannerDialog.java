package com.jonas.agile.devleadtool.gui.component.dialog;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.agile.devleadtool.gui.component.MyInternalFrame;
import com.jonas.common.logging.MyLogger;

public class SavePlannerDialog extends JFileChooser {

   public static final File[] RESETFILES = {new File("")};
   
   private final PlannerDAO dao;
   private Logger log = MyLogger.getLogger(SavePlannerDialog.class);
   private final JFrame parent;
   private final PlannerHelper helper;

   public SavePlannerDialog(PlannerDAO dao, JFrame parent, PlannerHelper helper) {
      super();
      this.dao = dao;
      this.parent = parent;
      this.helper = helper;
      addChoosableFileFilter(new XLSFileFilter());
   }

   public void save(MyInternalFrame internalFrame, boolean isFileChoosable) {
      File file = internalFrame.getExcelFile();
      boolean goAheadAndSave = true;
      if (isFileChoosable || file == null) {
         if (file != null) {
            setSelectedFile(file);
            System.out.println("*file*" + file.getAbsolutePath());
         } else {
            setCurrentDirectory(helper.getSaveDirectory());
            setSelectedFiles(RESETFILES);
         }
         System.out.println("current dir: " + getCurrentDirectory());
         File[] selectedFiles = getSelectedFiles();
         for (File file2 : selectedFiles) {
            System.out.println("selected file: " + file2.getAbsolutePath());
         }

         int result = showSaveDialog(parent);
         if (result == JFileChooser.APPROVE_OPTION) {
            file = getSelectedFile();
         } else {
            goAheadAndSave = false;
         }
      }

      if (goAheadAndSave) {
         log.debug("saving!");
         if (file == null) {
            AlertDialog.alertMessage(parent, "Can't save file choose another one!");
         }

         internalFrame.setSaveFile(file);
         dao.setXlsFile(file);
         internalFrame.saveModels(dao);
      }
   }

   private final class XLSFileFilter extends FileFilter {
      @Override
      public boolean accept(File f) {
         if (getTypeDescription(f).equalsIgnoreCase("Microsoft Excel Worksheet") || f.isDirectory())
            return true;
         return false;
      }

      @Override
      public String getDescription() {
         return "XLS files";
      }
   }

}
