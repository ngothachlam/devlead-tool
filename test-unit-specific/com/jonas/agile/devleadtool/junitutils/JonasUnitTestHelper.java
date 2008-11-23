package com.jonas.agile.devleadtool.junitutils;

import javax.swing.tree.DefaultMutableTreeNode;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;

public class JonasUnitTestHelper {

   public static DefaultMutableTreeNode getRoot(DnDTreeModel model) {
      return (DefaultMutableTreeNode) model.getRoot();
   }

   public static SprintNode getSprintFromModel(DnDTreeModel model, int index) {
      Object child = model.getChild(getRoot(model), index);
      SprintNode sprintNode = (SprintNode) child;
      return sprintNode;
   }

   public static JiraDTO getTestJiraDto(String sprint, String jira, Integer originalEstimate, String status, String... fixVersions) {
      JiraDTO jiraDto = new JiraDTO();
      jiraDto.setSprint(sprint);
      for (String fixVersion : fixVersions) {
         jiraDto.addFixVersion(fixVersion);
      }
      jiraDto.setKey(jira);

      if (originalEstimate != null)
         jiraDto.setOriginalEstimate(originalEstimate);
      if (status != null)
         jiraDto.setStatus(status);

      return jiraDto;
   }

}
