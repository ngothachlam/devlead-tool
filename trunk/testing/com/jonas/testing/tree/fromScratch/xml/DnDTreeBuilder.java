package com.jonas.testing.tree.fromScratch.xml;

import java.io.IOException;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.JiraException;
import com.jonas.testing.tree.fromScratch.tree.DnDTree;

public class DnDTreeBuilder {

   private Logger log = MyLogger.getLogger(DnDTreeBuilder.class);

   private final XmlParser parser;

   public DnDTreeBuilder(XmlParser parser) {
      super();
      this.parser = parser;
   }

   public void buildTree(DnDTree tree) {
      SwingWorker worker = new SwingWorker() {
         @Override
         protected Object doInBackground() {
            try {
               parser.parse();
            } catch (IOException e) {
               e.printStackTrace();
            } catch (SAXException e) {
               e.printStackTrace();
            } catch (JiraException e) {
               e.printStackTrace();
            }
            return null;
         }

         @Override
         protected void done() {
            MyStatusBar.getInstance().setMessage("Finished Building Table!", true);
         }
      };
      worker.execute();
   }

}
