package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.jira.access.JiraHttpClient;
import com.jonas.jira.access.JiraSoapClient;
import com.jonas.jira.jirastat.criteria.JiraCriteriaBuilder;

public class JiraCloseHelper {

   Pattern pattern = Pattern.compile(getRE("jira:") + ".*" + getRE("id:") + ".*" + getRE("resolution:"));

   JiraClient jiraClient = JiraClient.JiraClientAolBB;
   JiraSoapClient jiraSoapClient = JiraSoapClient.AOLBB;
   JiraHttpClient jiraHttpClient = JiraHttpClient.AOLBB;

   public void close() throws HttpException, IOException, JiraException, JDOMException {
      closeAllResolvedJirasInArchivedFixVersions(JiraProject.TALK);
   }

   public static void main(String[] args) throws Throwable {
      JiraCloseHelper jiraCloser = new JiraCloseHelper();
      jiraCloser.closeJirasManually();
      // try {
      // System.out.println("#################################");
      // System.out.println("######### Re Opening!!! #########");
      // System.out.println("#################################");
      // jiraCloser.reOpen(new File("test.txt"));
      // } catch (Throwable e) {
      // e.printStackTrace();
      // }

      // try {
      // System.out.println("#################################");
      // System.out.println("######### Now Closing!! #########");
      // System.out.println("#################################");
      // jiraCloser.closeJiras(new File("test.txt"));
      // } catch (Throwable e) {
      // e.printStackTrace();
      // }
   }

   private void closeJirasManually() throws HttpException, IOException, JDOMException, JiraException {
      jiraHttpClient.loginToJira();
      JiraCriteriaBuilder criteriaBuilder = new JiraCriteriaBuilder().project(JiraProject.LLU).statusOfNonClosed().issueType(IssueType.AUTOMATIC_BUILD_BREAK);

      JiraIssue[] jirasToClose = jiraClient.getJiras(criteriaBuilder);
      for (JiraIssue jiraToClose : jirasToClose) {
         boolean httpClose = false;
         try {
            httpClose = calculateResolutionAndCloseJira(jiraToClose);
         } catch (JiraException e) {
         }
         System.out.println(httpClose ? "Closed " : "Did not manage to close" + jiraToClose.getKey());
         // System.out.println(jiraToClose.getKey());
      }
   }

   private void reOpen(File output) throws HttpException, IOException, JiraException, JDOMException {
      String storedInfoOfClosedJiras = performPreanalysisPreReopening(JiraFilter.LLU_10_CLOSED);
      readFileAndReopen(output, storedInfoOfClosedJiras);

   }

   private void readFileAndReopen(File output, String storedInfoOfClosedJiras) throws IOException {
      FileWriter fw = null;
      BufferedWriter bw = null;
      if (output.exists()) {
         throw new RuntimeException("file " + output.getAbsolutePath() + " already exists!");
      }
      try {
         fw = new FileWriter(output);
         bw = new BufferedWriter(fw);
         try {
            System.out.println("Writing to " + output.getAbsolutePath());
            output.createNewFile();

            List<CloserDTO> preReOpenedStatuses = readInOldResolutions(storedInfoOfClosedJiras);
            for (CloserDTO dto : preReOpenedStatuses) {
               String jira = dto.getJira();
               String id = dto.getId();
               String resoId = dto.getResolutionId();

               JiraProject project = JiraProject.getProjectByJira(jira);
               String preJiraReopenState = "jira:\"" + jira + "\" id:\"" + id + "\" resolution:\"" + resoId + "\" project:\"" + project + "\"";
               bw.append(preJiraReopenState).append("\n");
               System.out.println("reopened " + preJiraReopenState);
            }

         } catch (Exception e) {
            e.printStackTrace();
         }
      } finally {
         if (bw != null)
            bw.close();
         if (fw != null)
            fw.close();
      }
   }

   private void closeJiras(File input) throws HttpException, IOException, JiraException {
      FileReader fr = null;
      BufferedReader br = null;
      try {
         fr = new FileReader(input);
         br = new BufferedReader(fr);

         String string;
         List<CloserDTO> closerDTOs = new ArrayList<CloserDTO>();

         while ((string = br.readLine()) != null) {
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
               closerDTOs.add(getCloserDTO(matcher));
            }
         }

         br.close();
         br = null;
         fr.close();
         fr = null;

         for (CloserDTO dto : closerDTOs) {
            closeJira(dto);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (br != null)
            br.close();
         if (fr != null)
            fr.close();
      }

      // jiraHttpClient.closeJira(remoteJira.getId(), remoteJira.getResolution(), jiraProject);
   }

   private void closeJira(CloserDTO dto) throws HttpException, IOException, JiraException {
      String jira = dto.getJira();
      String id = dto.getId();
      String resoId = dto.getResolutionId();

      JiraProject project = JiraProject.getProjectByJira(jira);
      jiraHttpClient.closeJira(id, resoId, project);
      System.out.println("closed jira:\"" + jira + "\" id:\"" + id + "\" resolution:\"" + resoId + "\" project:\"" + project + "\"");
   }

   private void closeAllResolvedJirasInArchivedFixVersions(JiraProject jiraProject) throws HttpException, IOException, JiraException, JDOMException {
      jiraHttpClient.loginToJira();
      List<JiraVersion> nonArchivedFixVersions = getNonArchivedFixVersions(jiraProject);
      closeAllResolvedJirasInFixVersions(nonArchivedFixVersions);

   }

   private List<JiraVersion> getNonArchivedFixVersions(JiraProject project) throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
         java.rmi.RemoteException {
      List<JiraVersion> archivedFixVersions = new ArrayList<JiraVersion>();
      JiraVersion[] fixVersions = jiraClient.getFixVersionsFromProject(project, true, false);
      for (JiraVersion jiraVersion : fixVersions) {
         if (jiraVersion.isArchived()) {
            archivedFixVersions.add(jiraVersion);
         }
      }
      return archivedFixVersions;
   }

   private void closeAllResolvedJirasInFixVersions(List<JiraVersion> relevantFixVersions) throws HttpException, IOException, JDOMException, JiraException {
      JiraCriteriaBuilder criteriaBuilder = new JiraCriteriaBuilder().statusOfNonClosed();
      criteriaBuilder.save();
      for (JiraVersion version : relevantFixVersions) {
         criteriaBuilder.reset(false);
         criteriaBuilder.fixVersion(version.getProject(), version);

         JiraIssue[] jirasToClose = jiraClient.getJiras(criteriaBuilder);
         for (JiraIssue jiraToClose : jirasToClose) {
            boolean httpClose = false;
            try {
               httpClose = calculateResolutionAndCloseJira(jiraToClose);
            } catch (JiraException e) {
            }
            System.out.println(httpClose ? "Closed " : "Did not manage to close" + jiraToClose.getKey());
         }
      }
   }

   private boolean calculateResolutionAndCloseJira(JiraIssue jira) throws HttpException, IOException, JiraException {
      RemoteIssue remoteJira = jiraSoapClient.getJira(jira.getKey());
      JiraProject jiraProject = JiraProject.getProjectByJira(jira.getKey());
      String resolution = remoteJira.getResolution() == null ? "6" : remoteJira.getResolution() ;
      jiraHttpClient.closeJira(remoteJira.getId(), resolution, jiraProject);
      return true;
   }

   private String performPreanalysisPreReopening(JiraFilter filter) throws HttpException, IOException, JiraException, JDOMException {
      jiraClient.login();
      JiraCriteriaBuilder criteria = new JiraCriteriaBuilder().filter(filter);
      JiraIssue[] jiras = jiraClient.getJiras(criteria);
      StringBuffer sb = new StringBuffer();
      for (JiraIssue jira : jiras) {
         sb.append(getAnalysisString(jira)).append("\n");
      }
      return sb.toString();
   }

   private List<CloserDTO> readInOldResolutions(String string) {
      Matcher matcher = pattern.matcher(string);
      List<CloserDTO> closerDTOs = new ArrayList<CloserDTO>();
      while (matcher.find()) {
         closerDTOs.add(getCloserDTO(matcher));
      }
      return closerDTOs;
   }

   private CloserDTO getCloserDTO(Matcher matcher) {
      CloserDTO closerDTO = null;
      for (int group = 0; group <= matcher.groupCount(); group++) {
         String match = matcher.group(group);
         switch (group) {
         case 1:
            closerDTO = new CloserDTO(match);
            break;
         case 2:
            closerDTO.setId(match);
            break;
         case 3:
            closerDTO.setResolutionId(match);
            return closerDTO;
         }
      }
      return closerDTO;
   }

   private String getRE(String prefix) {
      return prefix + "\"([^\"]+)\"";
   }

   private String getAnalysisString(JiraIssue jira) throws HttpException, IOException, JiraException {
      RemoteIssue remoteJira = jiraSoapClient.getJira(jira.getKey());
      return getReopenDebug(remoteJira);
   }

   private String getReopenDebug(RemoteIssue remoteJira) {
      StringBuffer sb = new StringBuffer();
      sb.append("jira:\"").append(remoteJira.getKey()).append("\"");
      sb.append(" with id:\"").append(remoteJira.getId()).append("\"");
      sb.append(" has resolution:\"").append(remoteJira.getResolution()).append("\"");
      return sb.toString();
   }
}


class CloserDTO {

   public String getJira() {
      return jira;
   }

   public String getResolutionId() {
      return resolutionId;
   }

   public String getId() {
      return id;
   }

   private final String jira;
   private String resolutionId;
   private String id;

   public CloserDTO(String jira) {
      this.jira = jira;
   }

   public void setResolutionId(String resolutionId) {
      this.resolutionId = resolutionId;
   }

   public void setId(String id) {
      this.id = id;
   }

}