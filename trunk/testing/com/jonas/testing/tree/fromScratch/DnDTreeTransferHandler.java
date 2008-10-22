package com.jonas.testing.tree.fromScratch;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class DnDTreeTransferHandler extends TransferHandler {

   private Logger log = MyLogger.getLogger(DnDTreeTransferHandler.class);

   private DataFlavor[] dataFlavor = new DataFlavor[1];

   public DnDTreeTransferHandler() {
      try {
         dataFlavor[0] = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.ArrayList");
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
   }

   // EXPORT:

   @Override
   final protected Transferable createTransferable(JComponent c) {
      log.debug("createTransferable: JComponent:" + c);
      Transferable tr = null;

      if (c instanceof JTree) {
         final JTree tree = (JTree) c;
         TreePath path = tree.getSelectionPath();
         if ((path == null) || (path.getPathCount() <= 1)) {
            return null;
         }
         tr = new Transferable() {
            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
               return tree.getSelectionModel();
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
               return dataFlavor;
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
               return dataFlavor[0].equals(flavor);
            }
         };
      }
      return tr;

   }

   @Override
   final protected void exportDone(JComponent source, Transferable data, int action) {
      log.debug("exportDone: Source:" + source + " Data: " + data + " Action: " + action);
      if (action == MOVE) {
         if (c instanceof JTree) {
            final JTree tree = (JTree) c;
            TreePath path = tree.getSelectionPath();
            tree.removeSelectionPath();
         }
      }
   }

   @Override
   final public int getSourceActions(JComponent c) {
      log.debug("getSourceActions: JComponent:" + c);
      return MOVE;
   }

   // IMPORT:

   @Override
   final public boolean canImport(TransferSupport supp) {
      log.debug("canImport");

      if (!supp.isDataFlavorSupported(DataFlavor.stringFlavor)) {
         return false;
      }
      log.debug("canImport returning true");
      return true;
   }

   @Override
   final public boolean importData(TransferSupport supp) {
      log.debug("importData");
      if (!canImport(supp)) {
         return false;
      }

      // Fetch the Transferable and its data
      Transferable t = supp.getTransferable();
      String data = (String) t.getTransferData(DataFlavor.stringFlavor);

      // Fetch the drop location
      DropLocation loc = supp.getDropLocation();

      // Insert the data at this location
      insertAt(loc, data);

      return true;
   }
}
