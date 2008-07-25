package _105._38._155._10.jira.rpc.soap.jirasoapservice_v2;

public class JiraSoapServiceProxy implements _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService {
  private String _endpoint = null;
  private _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService jiraSoapService = null;
  
  public JiraSoapServiceProxy() {
    _initJiraSoapServiceProxy();
  }
  
  public JiraSoapServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initJiraSoapServiceProxy();
  }
  
  private void _initJiraSoapServiceProxy() {
    try {
      jiraSoapService = (new _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator()).getJirasoapserviceV2();
      if (jiraSoapService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)jiraSoapService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)jiraSoapService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (jiraSoapService != null)
      ((javax.xml.rpc.Stub)jiraSoapService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService getJiraSoapService() {
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService;
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteGroup createGroup(java.lang.String in0, java.lang.String in1, com.atlassian.jira.rpc.soap.beans.RemoteUser in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.createGroup(in0, in1, in2);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteServerInfo getServerInfo(java.lang.String in0) throws java.rmi.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getServerInfo(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteGroup getGroup(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getGroup(in0, in1);
  }
  
  public java.lang.String login(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.login(in0, in1);
  }
  
  public boolean logout(java.lang.String in0) throws java.rmi.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.logout(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteUser getUser(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getUser(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteUser createUser(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.createUser(in0, in1, in2, in3, in4);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssue getIssue(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getIssue(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssue createIssue(java.lang.String in0, com.atlassian.jira.rpc.soap.beans.RemoteIssue in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.createIssue(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteNamedObject[] getAvailableActions(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getAvailableActions(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteComponent[] getComponents(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getComponents(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteProject[] getProjects(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getProjects(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssue updateIssue(java.lang.String in0, java.lang.String in1, com.atlassian.jira.rpc.soap.beans.RemoteFieldValue[] in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.updateIssue(in0, in1, in2);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteProject updateProject(java.lang.String in0, com.atlassian.jira.rpc.soap.beans.RemoteProject in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.updateProject(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssueType[] getIssueTypes(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getIssueTypes(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemotePriority[] getPriorities(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getPriorities(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteResolution[] getResolutions(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getResolutions(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteStatus[] getStatuses(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getStatuses(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssueType[] getSubTaskIssueTypes(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getSubTaskIssueTypes(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteField[] getCustomFields(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getCustomFields(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteFilter[] getSavedFilters(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getSavedFilters(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteComment[] getComments(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getComments(in0, in1);
  }
  
  public void addComment(java.lang.String in0, java.lang.String in1, com.atlassian.jira.rpc.soap.beans.RemoteComment in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.addComment(in0, in1, in2);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteVersion[] getVersions(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getVersions(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteProject createProject(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5, com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme in6, com.atlassian.jira.rpc.soap.beans.RemoteScheme in7, com.atlassian.jira.rpc.soap.beans.RemoteScheme in8) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.createProject(in0, in1, in2, in3, in4, in5, in6, in7, in8);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteField[] getFieldsForEdit(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getFieldsForEdit(in0, in1);
  }
  
  public void addUserToGroup(java.lang.String in0, com.atlassian.jira.rpc.soap.beans.RemoteGroup in1, com.atlassian.jira.rpc.soap.beans.RemoteUser in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.addUserToGroup(in0, in1, in2);
  }
  
  public void removeUserFromGroup(java.lang.String in0, com.atlassian.jira.rpc.soap.beans.RemoteGroup in1, com.atlassian.jira.rpc.soap.beans.RemoteUser in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.removeUserFromGroup(in0, in1, in2);
  }
  
  public void deleteProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.deleteProject(in0, in1);
  }
  
  public void deleteIssue(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.deleteIssue(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteScheme[] getNotificationSchemes(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getNotificationSchemes(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme[] getPermissionSchemes(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getPermissionSchemes(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme createPermissionScheme(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.createPermissionScheme(in0, in1, in2);
  }
  
  public void deletePermissionScheme(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.deletePermissionScheme(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme addPermissionTo(java.lang.String in0, com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme in1, com.atlassian.jira.rpc.soap.beans.RemotePermission in2, com.atlassian.jira.rpc.soap.beans.RemoteEntity in3) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.addPermissionTo(in0, in1, in2, in3);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme deletePermissionFrom(java.lang.String in0, com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme in1, com.atlassian.jira.rpc.soap.beans.RemotePermission in2, com.atlassian.jira.rpc.soap.beans.RemoteEntity in3) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.deletePermissionFrom(in0, in1, in2, in3);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemotePermission[] getAllPermissions(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getAllPermissions(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteField[] getFieldsForAction(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getFieldsForAction(in0, in1, in2);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssue progressWorkflowAction(java.lang.String in0, java.lang.String in1, java.lang.String in2, com.atlassian.jira.rpc.soap.beans.RemoteFieldValue[] in3) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.progressWorkflowAction(in0, in1, in2, in3);
  }
  
  public boolean addAttachmentToIssue(java.lang.String in0, java.lang.String[] in1, com.atlassian.jira.rpc.soap.beans.RemoteIssue in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.addAttachmentToIssue(in0, in1, in2);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssue[] getIssuesFromTextSearch(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getIssuesFromTextSearch(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssue[] getIssuesFromTextSearchWithProject(java.lang.String in0, java.lang.String[] in1, java.lang.String in2, int in3) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getIssuesFromTextSearchWithProject(in0, in1, in2, in3);
  }
  
  public void deleteUser(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.deleteUser(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteGroup updateGroup(java.lang.String in0, com.atlassian.jira.rpc.soap.beans.RemoteGroup in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.updateGroup(in0, in1);
  }
  
  public void deleteGroup(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.deleteGroup(in0, in1, in2);
  }
  
  public void refreshCustomFields(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    jiraSoapService.refreshCustomFields(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteProject[] getProjectsNoSchemes(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getProjectsNoSchemes(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteVersion addVersion(java.lang.String in0, java.lang.String in1, com.atlassian.jira.rpc.soap.beans.RemoteVersion in2) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.addVersion(in0, in1, in2);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteProject createProjectFromObject(java.lang.String in0, com.atlassian.jira.rpc.soap.beans.RemoteProject in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteValidationException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.createProjectFromObject(in0, in1);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteScheme[] getSecuritySchemes(java.lang.String in0) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemotePermissionException, com.atlassian.jira.rpc.exception.RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getSecuritySchemes(in0);
  }
  
  public com.atlassian.jira.rpc.soap.beans.RemoteIssue[] getIssuesFromFilter(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException, com.atlassian.jira.rpc.exception.RemoteException{
    if (jiraSoapService == null)
      _initJiraSoapServiceProxy();
    return jiraSoapService.getIssuesFromFilter(in0, in1);
  }
  
  
}