package com.jonas.jira.access;

import junit.framework.TestCase;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;

public class JiraSoapClientFunctionalTest extends TestCase {

   JiraSoapClient clientAtlassain = JiraSoapClient.ATLASSIN;
   JiraSoapClient clientAolBB = JiraSoapClient.AOLBB;

   protected void setUp() throws Exception {
      super.setUp();
   }

   // public void testShouldGetFixVersionsOk() throws Exception {
   // RemoteVersion[] fixVersions = clientAtlassain.getFixVersions(JiraProject.ATLASSIN_TST);
   // assertTrue(fixVersions.length > 0);
   // }

   // public void testShouldGetSingleFixVersionOk() throws Exception {
   // // TODO optimise this call = re-do in httpClient if XML can be found?
   // RemoteVersion fixVersion = clientAtlassain.getFixVersion("Dev Version", JiraProject.ATLASSIN_TST);
   // assertTrue(fixVersion != null);
   // }

   // public void testAllCustomFields() throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
   // java.rmi.RemoteException {
   // clientAolBB.printAllCustomFieldInfo();
   // }

   // public void testGettingFilters() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException
   // {
   // RemoteFilter filter = clientAolBB.getFilter("LLU Dev Support - Priority List (unresolved)");
   // System.out.println(filter.getId());
   // System.out.println(filter.getXml());
   // }

   // public void testProgressWorkflow() throws RemoteAuthenticationException, RemoteException, java.rmi.RemoteException{
   // clientAolBB.login();
   // clientAolBB.testProgressWorkflow("LLU-4365");
   // }

   public void testReOpenJira() throws RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      clientAolBB.login();
      // clientAolBB.testGetAvailableActions("LLU-4382");
      // try {
      // clientAolBB.testCloseJira("LLU-4382");
      // } catch (Exception e) {
      //
      // }
//      System.out.println("--Get Actions--");
//      clientAolBB.testGetActions("LLU-4382");
      System.out.println("--ReOpen Jira--");
      String resolution = clientAolBB.reopenJira("LLU-4382");
//      System.out.println("--Close Jira--");
//      clientAolBB.closeJira("LLU-4382", "1");
//      System.out.println("--Resolution:--");
//      System.out.println(resolution);
   }

   // public void testCreateMerge() throws RemoteAuthenticationException, RemoteException, java.rmi.RemoteException{
   // clientAolBB.login();
   // RemoteIssue createMergeJira = clientAolBB.createMergeJira("LLU-4305", "LLU 13");
   // System.out.println(createMergeJira.getKey()+" - "+createMergeJira.getId());
   // }
}
