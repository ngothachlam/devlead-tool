package com.jonas.jira.access;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import junit.framework.TestCase;
import org.apache.axis.client.Stub;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteCustomFieldValue;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.jira.JiraProject;

public class JiraSoapClientFunctionalTest extends TestCase {

   JiraSoapClient client = null;
   JiraSoapClient clientAolBB = null;

   protected void setUp() throws Exception {
      super.setUp();
      client = new JiraSoapClient(ClientConstants.ATLASSIN_WS);
      clientAolBB = new JiraSoapClient(ClientConstants.AOLBB_WS);
   }

   public void testShouldGetFixVersionsOk() throws Exception {
      RemoteVersion[] fixVersions = client.getFixVersions(JiraProject.ATLASSIN_TST);
      assertTrue(fixVersions.length > 0);
   }

   public void testShouldGetSingleFixVersionOk() throws Exception {
      // TODO optimise this call = re-do in httpClient if XML can be found?
      RemoteVersion fixVersion = client.getFixVersion("Dev", JiraProject.ATLASSIN_TST);
      assertTrue(fixVersion != null);
   }

   public void testGetJiraOk() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException,
         JiraIssueNotFoundException, InterruptedException {
      RemoteIssue jira = clientAolBB.getJira("LLU-4088");
      assertEquals("LLU-4088", jira.getKey());
      RemoteCustomFieldValue[] customFieldValues = jira.getCustomFieldValues();

      printAllMethodsAndResults(jira);

      System.out.println("Custom Fields:");
      for (RemoteCustomFieldValue remoCustomFieldValue : customFieldValues) {
         System.out.println("\tgetCustomfieldId: " + remoCustomFieldValue.getCustomfieldId() + " - getKey: " + remoCustomFieldValue.getKey());
         String[] values = remoCustomFieldValue.getValues();
         for (String string : values) {
            System.out.println("\t\tgetValues(...): " + string);
         }
         if ("customfield_10160".equals(remoCustomFieldValue.getCustomfieldId())) {
            System.out.println("\t**");
         }
      }
      assertTrue(false);
   }

   private void printAllMethodsAndResults(RemoteIssue jira) {
      Class<?> c = jira.getClass();
      Method[] allMethods = c.getDeclaredMethods();
      System.out.println("Methods:");
      for (Method method : allMethods) {

         if (!method.getName().startsWith("get") && method.getGenericParameterTypes().length != 0)
            continue;
         System.out.println(method.getName());

         try {
            method.setAccessible(true);
            Object o = method.invoke(jira);
            System.out.println("\t" + method.getName() + "() returned: " + o);

            // Handle any exceptions thrown by method to be invoked.
         } catch (Throwable x) {
            Throwable cause = x.getCause();
            System.out.println("\t" + method.getName() + "() returned: " + cause);
            x.printStackTrace();
         }

      }

   }

   public void testGetJiraThatDoesNotExist() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException,
         JiraIssueNotFoundException, InterruptedException {
      assertEquals("TST-1", client.getJira("TST-1").getKey());
      try {
         assertEquals(null, client.getJira("TST-2"));
         assertTrue(false);
      } catch (JiraIssueNotFoundException e) {
      }
   }

   public void testGetJiraThatTimesOut() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException,
         JiraIssueNotFoundException, InterruptedException {
      assertEquals("TST-1", client.getJira("TST-1").getKey());
      try {
         assertEquals(null, client.getJira("TST-2"));
         assertTrue(false);
      } catch (JiraIssueNotFoundException e) {
      }
      Thread.currentThread().sleep(700);
      assertTrue("Get Timeout to work!", false);
      assertEquals("TST-3", client.getJira("TST-3").getKey());
   }

   public void testAllCustomFields() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      clientAolBB.printAllCustomFieldInfo();
   }
}
