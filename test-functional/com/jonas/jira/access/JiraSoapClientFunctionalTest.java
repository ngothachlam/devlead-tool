package com.jonas.jira.access;

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

      // http://10.155.38.105/jira/secure/IssueNavigator.jspa?
      // view=rss&
      // &customfield_10241%3AlessThan=00001000000000.000
      // &customfield_10241%3AgreaterThan=00000000000000.000
      // &pid=10192&status=1&status=3&status=4&status=5&sorter/field=created&sorter/order=ASC&sorter
      // /field=customfield_10188
      // &sorter/order=ASC&sorter/
      // field=customfield_10241
      // &sorter/order=DESC&tempMax=25&reset=true&decorator=none

   }
   
   public void testShouldCreateMergeOk() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException{
      RemoteIssue newJira = clientAtlassain.createMergeJira("TST-16639", "Test Version 1");
      System.out.println("Created merge jira " + newJira.getKey());
   }
}
