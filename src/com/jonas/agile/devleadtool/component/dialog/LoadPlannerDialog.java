package com.jonas.agile.devleadtool.component.dialog;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyDesktopPane;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.component.SaveKeyListener;
import com.jonas.agile.devleadtool.component.panel.MyInternalFrameInnerPanel;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.common.logging.MyLogger;

public class LoadPlannerDialog extends JFileChooser {

   private final PlannerDAO dao;
   private final JFrame frame;
   private final PlannerHelper helper;
   private final MyDesktopPane desktop;
   private SavePlannerDialog savePlannerDialog;
   private SaveKeyListener saveKeyListener;
   private Logger log = MyLogger.getLogger(LoadPlannerDialog.class);

   public LoadPlannerDialog(MyDesktopPane desktop, PlannerDAO plannerDAO, JFrame frame, PlannerHelper helper, SavePlannerDialog savePlannerDialog, SaveKeyListener saveKeyListener) {
      super(new File("."));
      this.desktop = desktop;
      this.dao = plannerDAO;
      this.frame = frame;
      this.helper = helper;
      this.savePlannerDialog = savePlannerDialog;
      this.saveKeyListener = saveKeyListener;


      addChoosableFileFilter(new FileFilter() {
         public boolean accept(File f) {
            if (getTypeDescription(f).equalsIgnoreCase("Microsoft Excel Worksheet") || f.isDirectory())
               return true;
            return false;
         }

         public String getDescription() {
            return "XLS files";
         }
      });
   }

   public void load() {
      log.trace("load");
      int result = showOpenDialog(frame);

      if (result == JFileChooser.APPROVE_OPTION) {
         final File xlsFile = getSelectedFile();

         dao.setXlsFile(xlsFile);

         SwingWorker<CombinedModelDTO, Object> swingWorker = new SwingWorker<CombinedModelDTO, Object>() {
            @Override
            protected CombinedModelDTO doInBackground() throws Exception {
               return dao.loadModels();
            }

            @Override
            protected void done() {
               log.trace("done");
               try {
                  CombinedModelDTO dto = get();
                  if (dto != null) {
                     MyInternalFrameInnerPanel internalFrameTabPanel = new MyInternalFrameInnerPanel(helper, dto.getBoardModel(), dto.getJiraModel());
                     MyInternalFrame internalFrame = new MyInternalFrame(helper, helper.getTitle(), internalFrameTabPanel, dao, savePlannerDialog, saveKeyListener, desktop);
                     internalFrame.setSaveFile(xlsFile);
                  }
                  super.done();
               } catch (Throwable e) {
                  AlertDialog.alertException(helper.getParentFrame(), e);
                  e.printStackTrace();
               }
            }
         };

         swingWorker.execute();
      }
   }
}
