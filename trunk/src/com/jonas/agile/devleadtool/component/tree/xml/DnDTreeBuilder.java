package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.IOException;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.JiraException;

public class DnDTreeBuilder {

   private Logger log = MyLogger.getLogger(DnDTreeBuilder.class);

   private final XmlParser parser;

   public DnDTreeBuilder(XmlParser parser) {
      super();
      this.parser = parser;
   }

   public void buildTree(DnDTree tree, final String sprint) {
      SwingWorker worker = new SwingWorker() {
         @Override
         protected Object doInBackground() {
            try {
               parser.parse(sprint);
            } catch (IOException e) {
               e.printStackTrace();
            } catch (SAXException e) {
               e.printStackTrace();
            } catch (JiraException e) {
               e.printStackTrace();
            }
            return null;
         }
      };
      worker.execute();
   }

}
