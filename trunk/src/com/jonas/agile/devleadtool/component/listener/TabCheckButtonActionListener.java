/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.jonas.agile.devleadtool.PlannerCommunicator;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;

public class TabCheckButtonActionListener implements ActionListener {

   private final MyTable table;
   private final PlannerCommunicator plannerCommunicator;
   private final PlannerHelper helper;
   
   /**
    * @param helper 
    * @param boardPanel
    */
   public TabCheckButtonActionListener(PlannerHelper helper, MyTable table, PlannerCommunicator plannerCommunicator){
      this.helper = helper;
      this.table = table;
      this.plannerCommunicator = plannerCommunicator;
   }
   
   
   public void actionPerformed(ActionEvent e) {
      JFrame newFrame = new JFrame();
      MyPanel contentPane = new MyPanel(new BorderLayout());
      DefaultTableModel model = new DefaultTableModel(new ColumnDataType[]{ColumnDataType.Jira, ColumnDataType.BoardStatus, ColumnDataType.Planned_Sprint, ColumnDataType.Resolved_Sprint, ColumnDataType.Closed_Sprint}, 0);
      
      for (int i = 0; i < table.getModel().getRowCount(); i++) {
         Vector<String> rowData = new Vector<String>();
         //col 1: 
         String jira = (String) table.getValueAt(ColumnDataType.Jira, i);
         rowData.add(jira);

         //col 2: 
         List<BoardStatus> boardStatus = plannerCommunicator.getJiraStatusFromBoard(jira);
         StringBuffer sb = new StringBuffer();
         for (BoardStatus status : boardStatus) {
            sb.append("[").append(status).append("]");
         }
         rowData.add(sb.toString());

         //col 3: 
         rowData.add(plannerCommunicator.getPlannedSprint(jira));
         //col 4: 
         rowData.add(plannerCommunicator.getResolvedSprint(jira));
         //col 5: 
         rowData.add(plannerCommunicator.getClosedSprint(jira));
         
         
         model.addRow(rowData);
      }
      
      JTable tabCheckTable = new JTable(model);
      MyScrollPane scrollpane = new MyScrollPane(tabCheckTable);
      
      contentPane.addNorth(new JLabel("North!"));
      contentPane.addCenter(scrollpane);
      contentPane.addSouth(new JLabel("South!"));
      
      newFrame.setContentPane(contentPane);
      newFrame.setSize(new Dimension(300, 700));
      SwingUtil.locateWindowRelativeToWindow(newFrame, helper.getActiveInternalFrame(), 840, 58);
      newFrame.setVisible(true);
      }
}