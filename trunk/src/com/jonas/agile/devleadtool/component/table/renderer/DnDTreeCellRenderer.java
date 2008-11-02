package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.common.logging.MyLogger;

public class DnDTreeCellRenderer extends DefaultTreeCellRenderer {

   private static final ImageIcon IMAGE_ICON_WHITE = new ImageIcon("icons/white_tick.png");
   private static final ImageIcon IMAGE_ICON_GREEN = new ImageIcon("icons/green_tick.png");
   private static final ImageIcon IMAGE_ICON_BLUE = new ImageIcon("icons/blue_tick.png");
   private static final ImageIcon IMAGE_ICON_RED = new ImageIcon("icons/red_tick.png");

   private final static Logger log = MyLogger.getLogger(DnDTreeCellRenderer.class);

   @Override
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      // log.debug("getTreeCellRendererComponent");
      if (value instanceof JiraNode) {
         JiraNode jira = (JiraNode) value;
         if ("Closed".equalsIgnoreCase(jira.getStatus())) {
            setIcon(IMAGE_ICON_GREEN);
         } else if ("Resolved".equalsIgnoreCase(jira.getStatus())) {
            setIcon(IMAGE_ICON_BLUE);
         } else if ("Reopened".equalsIgnoreCase(jira.getStatus())) {
            setIcon(IMAGE_ICON_RED);
         } else {
            setIcon(IMAGE_ICON_WHITE);
         }
         log.debug("jira rendering: " + jira.isToSync());

         if (jira.isToSync()) {
            Font oldFont = getFont();
            setFont(new Font(oldFont.getName(), Font.ITALIC, oldFont.getSize()));
         } else {
            Font oldFont = getFont();
            setFont(new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize()));
         }
      } else {
         Font oldFont = getFont();
         setFont(new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize()));
      }
      return this;
   }

}
