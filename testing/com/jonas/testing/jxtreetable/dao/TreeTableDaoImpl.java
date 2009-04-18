package com.jonas.testing.jxtreetable.dao;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class TreeTableDaoImpl {

   public DefaultMutableTreeTableNode read(File file, DaoTreeBuilder treeBuilder) throws IOException {
      FileInputStream fs = null;
      BufferedInputStream bs = null;
      XMLDecoder xdec = null;
      try {
         
         fs = new FileInputStream(file);
         bs = new BufferedInputStream(fs);
         xdec = new XMLDecoder(bs);
         
         DaoTreeNode tree = (DaoTreeNode) xdec.readObject(); 
         
         return treeBuilder.readDaoTreeAndGetRoot(tree);
         
      } finally {
         if (xdec != null) {
            xdec.close();
         }
         if (bs != null) {
            bs.close();
         }
         if (fs != null) {
            fs.close();
         }
      }
   }
   
   public void persist(File file, DefaultTreeTableModel treeTableModel, DaoTreeBuilder treeBuilder) throws IOException {
      FileOutputStream fos = null;
      BufferedOutputStream bos = null;
      XMLEncoder xenc = null;
      try {

         fos = new FileOutputStream(file);
         bos = new BufferedOutputStream(fos);
         xenc = new XMLEncoder(bos);

         DaoTreeNode tree = treeBuilder.buildDaoTree(treeTableModel);
         
         xenc.writeObject(tree);

      } finally {
         if (xenc != null) {
            xenc.close();
         }
         if (bos != null) {
            bos.close();
         }
         if (fos != null) {
            fos.close();
         }
      }
   }
}
