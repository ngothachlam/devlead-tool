package com.jonas.agile.devleadtool.gui.component.dialog;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.agile.devleadtool.gui.component.DesktopPane;
import com.jonas.agile.devleadtool.gui.component.SaveKeyListener;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.common.logging.MyLogger;

public class LoadPlannerDialog extends JFileChooser {

   private final PlannerDAO dao;
   private final JFrame frame;
   private final PlannerHelper helper;
   private final DesktopPane desktop;
   private SavePlannerDialog savePlannerDialog;
   private SaveKeyListener saveKeyListener;
   private Logger log = MyLogger.getLogger(LoadPlannerDialog.class);
   private ExcelSprintDao sprintDao;

   public LoadPlannerDialog(DesktopPane desktop, PlannerDAO plannerDAO, JFrame frame, PlannerHelper helper, SavePlannerDialog savePlannerDialog,
         SaveKeyListener saveKeyListener, ExcelSprintDao sprintDao) {
      super();
      this.desktop = desktop;
      this.dao = plannerDAO;
      this.frame = frame;
      this.helper = helper;
      this.savePlannerDialog = savePlannerDialog;
      this.saveKeyListener = saveKeyListener;
      this.sprintDao = sprintDao;

      addChoosableFileFilter(new FileFilter() {
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
      });
   }

   public void load() {
      log.trace("load");

      setCurrentDirectory(helper.getSaveDirectory());
      setSelectedFiles(savePlannerDialog.RESETFILES);

      int result = showOpenDialog(frame);

      if (result == JFileChooser.APPROVE_OPTION) {
         final File xlsFile = getSelectedFile();

         SwingWorker<CombinedModelDTO, Object> swingWorker = new AbstractInternalFrameCreatorSwingthread(helper, dao, savePlannerDialog,
               saveKeyListener, desktop, xlsFile, sprintDao) {
            @Override
            protected CombinedModelDTO doInBackground() throws Exception {
               CombinedModelDTO loadModels = null;
               loadModels = dao.loadAllData(xlsFile);
               return loadModels;
            }
         };

         ExecutorService executor = Executors.newSingleThreadExecutor();
         executor.execute(swingWorker);
      }
   }
}
