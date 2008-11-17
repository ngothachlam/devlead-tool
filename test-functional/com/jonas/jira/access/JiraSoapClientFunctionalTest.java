package com.jonas.jira.access;

import javax.xml.rpc.ServiceException;
import junit.framework.TestCase;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteFilter;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.jira.JiraProject;

public class JiraSoapClientFunctionalTest extends TestCase {

   JiraSoapClient clientAtlassain = new JiraSoapClient(ClientConstants.ATLASSIN_WS);
   JiraSoapClient clientAolBB = new JiraSoapClient(ClientConstants.AOLBB_WS);

   protected void setUp() throws Exception {
      super.setUp();
   }

   public void atestShouldGetFixVersionsOk() throws Exception {
      RemoteVersion[] fixVersions = clientAtlassain.getFixVersions(JiraProject.ATLASSIN_TST);
      assertTrue(fixVersions.length > 0);
   }

   public void atestShouldGetSingleFixVersionOk() throws Exception {
      // TODO optimise this call = re-do in httpClient if XML can be found?
      RemoteVersion fixVersion = clientAtlassain.getFixVersion("Dev Version", JiraProject.ATLASSIN_TST);
      assertTrue(fixVersion != null);
   }

   public void atestAllCustomFields() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      clientAolBB.printAllCustomFieldInfo();
   }

   public void atestGettingFilters() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      RemoteFilter filter = clientAolBB.getFilter("LLU Dev Support - Priority List (unresolved)");
      System.out.println(filter.getId());
      System.out.println(filter.getXml());
   }

   public void trestShouldCreateMergeOk() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException,
         ServiceException {
//      RemoteIssue newJira = clientAtlassain.createMergeJira("TST-11368", "Dev Version", "1");
       RemoteIssue newJira = clientAolBB.createMergeJira("LLU-4275", "LLU 12");
      System.out.println("Created merge jira " + newJira.getKey());

   }

   public void tsestShsdouldCreateSprintOk() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException,
         ServiceException {
      clientAolBB.updateSprint("LLU-3809", "test");
   }
}
