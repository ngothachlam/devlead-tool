package com.jonas.agile.devleadtool.gui.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.HyperLinker;

/**
 * Is meant to be a mouseclick adapter to the JTable that listens on clicks to a cell with a header name as specified in the constructor. When that happens it
 * should open up a browser window in relation to that url.
 * 
 * @author jonasjolofsson
 * 
 */
public class HyperLinkOpenerAdapter extends MouseAdapter {
   private final PlannerHelper helper;
   private final Column hyperLinkColumn;
   private final Column jiraColumn;

   public HyperLinkOpenerAdapter(PlannerHelper helper, Column hyperLinkColumn, Column jiraColumn) {
      this.helper = helper;
      this.hyperLinkColumn = hyperLinkColumn;
      this.jiraColumn = jiraColumn;
   }

   public void mouseClicked(MouseEvent e) {
      MyTable table = (MyTable) e.getSource();
      int itsRow = table.rowAtPoint(e.getPoint());
      int itsColumn = table.columnAtPoint(e.getPoint());
      if (hyperLinkColumn.equals(table.getColumnEnum(itsColumn))) {
         String jira = (String) table.getValueAt(jiraColumn, itsRow);
         try {
            HyperLinker.displayURL(PlannerHelper.getJiraUrl(jira) + "/browse/" + jira);
         } catch (NotJiraException ex) {
            AlertDialog.alertException(helper.getParentFrame(), ex);
         } catch (URISyntaxException ex) {
            AlertDialog.alertException(helper.getParentFrame(), ex);
         } catch (IOException ex) {
            AlertDialog.alertException(helper.getParentFrame(), ex);
         }
      }
   }
}