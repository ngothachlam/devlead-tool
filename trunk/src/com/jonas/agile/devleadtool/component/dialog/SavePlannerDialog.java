package com.jonas.agile.devleadtool.component.dialog;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.common.logging.MyLogger;

public class SavePlannerDialog extends JFileChooser {

   private Logger log = MyLogger.getLogger(SavePlannerDialog.class);

   public SavePlannerDialog(PlannerDAO dao, JFrame parent, MyInternalFrame internalFrame, boolean isFileChoosable, DaoListener daoListener) {
      super(new File("."));
      if (dao == null || internalFrame == null) {
         return;
      }
      File file = internalFrame.getFile();
      boolean goAheadAndSave = true;
      if (isFileChoosable || file == null) {
         if (file != null) {
            setSelectedFile(file);
         }
         // addChoosableFileFilter(new FileFilter() {
         // public boolean accept(File f) {
         // if (getTypeDescription(f).equalsIgnoreCase("Microsoft Excel Worksheet") || f.isDirectory())
         // return true;
         // return false;
         // }
         //
         // public String getDescription() {
         // return "XLS files";
         // }
         // });

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
         internalFrame.saveModels(dao, daoListener);
      }
   }
}
