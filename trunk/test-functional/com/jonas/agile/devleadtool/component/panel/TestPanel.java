package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import com.jonas.agile.devleadtool.data.PlanFixVersion;
import com.jonas.common.MyComponentPanel;

public class TestPanel extends MyComponentPanel {

   private JList list;

   public TestPanel(){
      super(new BorderLayout());
      
      list = new JList(new DefaultListModel());
      addNorth(list);
      addSouth(getButtonPanel());
      
   }

   private Component getButtonPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      addButton(panel, "refresh", new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
            List<PlanFixVersion> planVersions = PlanFixVersion.getList();
            DefaultListModel defaultListModel = new DefaultListModel();
            defaultListModel.removeAllElements();
            for (PlanFixVersion planFixVersion : planVersions) {
               defaultListModel.addElement(planFixVersion);
            }
            list.setModel(defaultListModel);
         }
      });
      return panel;
   }
}
