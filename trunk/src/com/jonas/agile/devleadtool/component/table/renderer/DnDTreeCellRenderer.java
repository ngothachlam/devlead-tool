package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.tree.nodes.FixVersionNode;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.common.logging.MyLogger;

public class DnDTreeCellRenderer extends DefaultTreeCellRenderer {

   private static final ImageIcon IMAGE_ICON_DEFAULT = new ImageIcon("icons/jira_status_open.gif");
   private static final ImageIcon IMAGE_ICON_CLOSED = new ImageIcon("icons/jira_status_closed.gif");
   private static final ImageIcon IMAGE_ICON_RESOLVED = new ImageIcon("icons/jira_status_resolved.gif");
   private static final ImageIcon IMAGE_ICON_REOPENED = new ImageIcon("icons/jira_status_reopened.gif");
   private static final ImageIcon IMAGE_ICON_INPROGRESS = new ImageIcon("icons/jira_status_inprogress.gif");

   private static final ImageIcon IMAGE_ICON_VERSION_OPEN = new ImageIcon("icons/jira_fixVersion_open.gif");
   private static final ImageIcon IMAGE_ICON_VERSION_RELEASED = new ImageIcon("icons/jira_fixVersion_released.gif");

   private final static Logger log = MyLogger.getLogger(DnDTreeCellRenderer.class);
   private Font font_plain;
   private Font font_italic;

   @Override
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      // log.debug("getTreeCellRendererComponent");
      if (value instanceof JiraNode) {
         setLFforJira(value);
      } else if (value instanceof FixVersionNode) {
         setLFforFixVersion(value);
      } else if (value instanceof SprintNode) {
         setLFforSprint(value);
      } else {
         setFont(getPlainFont());
      }
      return this;
   }

   private void setLFforSprint(Object value) {
//      SprintNode sprintNode = (SprintNode) value;
      setFont(getPlainFont());
   }

   private void setLFforFixVersion(Object value) {
      FixVersionNode fixversion = (FixVersionNode) value;
      if (fixversion.isReleased()) {
         setIcon(IMAGE_ICON_VERSION_RELEASED);
      } else {
         setIcon(IMAGE_ICON_VERSION_OPEN);
      }
      setFont(getPlainFont());
   }

   private void setLFforJira(Object value) {
      JiraNode jira = (JiraNode) value;
      if ("Closed".equalsIgnoreCase(jira.getStatus())) {
         setIcon(IMAGE_ICON_CLOSED);
      } else if ("Resolved".equalsIgnoreCase(jira.getStatus())) {
         setIcon(IMAGE_ICON_RESOLVED);
      } else if ("Reopened".equalsIgnoreCase(jira.getStatus())) {
         setIcon(IMAGE_ICON_REOPENED);
      } else if ("In Progress".equalsIgnoreCase(jira.getStatus())) {
         setIcon(IMAGE_ICON_INPROGRESS);
      } else {
         setIcon(IMAGE_ICON_DEFAULT);
      }
      log.debug("jira rendering: " + jira.isToSync());

      if (jira.isToSync()) {
         setFont(getItalicFont());
      } else {
         setFont(getPlainFont());
      }
   }

   private Font getItalicFont() {
      Font oldFont = getFont();
      if (font_italic == null) {
         font_italic = new Font(oldFont.getName(), Font.ITALIC, oldFont.getSize());
      }
      return font_italic;
   }

   private Font getPlainFont() {
      Font oldFont = getFont();
      if (font_plain == null) {
         font_plain = new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize());
      }
      return font_plain;
   }

}
