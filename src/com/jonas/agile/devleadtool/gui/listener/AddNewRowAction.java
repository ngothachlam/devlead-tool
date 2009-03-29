/**
 * 
 */
package com.jonas.agile.devleadtool.gui.listener;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.dto.JiraStringDTO;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.JiraRegexParser;
import com.jonas.jira.JiraIssue;

public abstract class AddNewRowAction extends BasicAbstractGUIAction {
   private final JTextComponent jiraCommas;
   private final JTextComponent jiraPrefix;
   private List<JiraToBeReconciledListener> listeners = new ArrayList<JiraToBeReconciledListener>();
   private Logger log = MyLogger.getLogger(AddNewRowAction.class);
   private JiraRegexParser parser;
   private final JTextComponent release;
   private final JComboBox statusCombo;

   public AddNewRowAction(String title, String desc, JTextComponent jiraPrefix, JTextComponent jiraCommas, JTextComponent release, JComboBox statusCombo, Frame parentFrame) {
      super(title, desc, parentFrame);
      this.jiraPrefix = jiraPrefix;
      this.jiraCommas = jiraCommas;
      this.release = release;
      this.statusCombo = statusCombo;
      parser = new JiraRegexParser();
   }

   private void addJiras(MyTable table) {
      List<String> jiras = parser.separateIntoJiras(jiraCommas.getText());

      for (String jira : jiras) {
         String prefix = parser.getPrefixWithHyphen(jiraPrefix.getText());

         JiraStringDTO dto = parser.separateJira(jira);

         String jiraString = prefix + dto.getJira();
         String devEst = dto.getDevEstimate();
         String actual = dto.getDevActual();
         String qaEst = dto.getQAEstimate();
         String remainder = dto.getDevRemainder();
         log.debug("added jira " + jiraString);
         
         notifyJirasAdded(table, jiraString, devEst, actual, release.getText(), remainder, qaEst, (BoardStatusValue) statusCombo.getSelectedItem());
      }
   }

   public void addJiraToBeReconciledListener(JiraToBeReconciledListener jiraToBeReconciledListener){
      listeners.add(jiraToBeReconciledListener);
   }

   private void addJiraToTable(MyTable table) {
      TableModelListenerAlerter tableModelListenerAlerter = table.getTableModelListenerAlerter();
      if (tableModelListenerAlerter != null) {
         synchronized (tableModelListenerAlerter) {
            tableModelListenerAlerter.activate();
            addJiras(table);
            tableModelListenerAlerter.setParent(getParentFrame());
            tableModelListenerAlerter.deActivateAndAlert();
         }
      } else {
         addJiras(table);
      }
   }

   @Override
   public final void doActionPerformed(ActionEvent e) {
      MyTable table = getTargetTable();

      if (table == null)
         return;
      addJiraToTable(table);
   }
   
   private JiraIssue getJiraIssue(String jira){
      return new JiraIssue(jira, release.getText());
   }
   
   public abstract MyTable getTargetTable();

   private void notifyJirasAdded(MyTable table, String jira, String devEst, String actual, String release, String remainder, String qaEst, BoardStatusValue status) {
      for (JiraToBeReconciledListener jiraToBeReconciledListener : listeners) {
         jiraToBeReconciledListener.jiraAdded(table, jira, devEst, actual, release, remainder, qaEst, status);
      }
   }

}
