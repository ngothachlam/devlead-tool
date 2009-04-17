package com.jonas.agile.devleadtool.gui.component.dialog;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.agile.devleadtool.gui.component.MyInternalFrame;
import com.jonas.common.logging.MyLogger;

public class SavePlannerDialog extends JFileChooser {

   private final PlannerDAO dao;
   private Logger log = MyLogger.getLogger(SavePlannerDialog.class);
   private final JFrame parent;
   public SavePlannerDialog(PlannerDAO dao, JFrame parent) {
      super(new File("."));
      this.dao = dao;
      this.parent = parent;
      addChoosableFileFilter(new XLSFileFilter());
   }

   public void save(MyInternalFrame internalFrame, boolean isFileChoosable) {
      File file = internalFrame.getExcelFile();
      boolean goAheadAndSave = true;
      if (isFileChoosable || file == null) {
         if (file != null) {
            setSelectedFile(file);
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
