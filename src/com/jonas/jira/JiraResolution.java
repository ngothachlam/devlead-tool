package com.jonas.jira;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.rpc.soap.beans.RemoteResolution;

public class JiraResolution {

	@Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((description == null) ? 0 : description.hashCode());
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      JiraResolution other = (JiraResolution) obj;
      if (description == null) {
         if (other.description != null)
            return false;
      } else if (!description.equals(other.description))
         return false;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

   private final static Map<String, JiraResolution> jiraResolutions = new HashMap<String, JiraResolution>();
	
   //Use: http://jira.atlassian.com/secure/IssueNavigator.jspa?reset=true&resolution=-1&tempMax=1 to try!!
	//TODO: Add preset resolutions!!
	
	private final String id;
	private final String name;
	private final String description;

	private JiraResolution(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public static int getAmount() {
		return jiraResolutions.size();
	}
	
	public static JiraResolution getResolution(String id) {
		return jiraResolutions.get(id);
	}

	public static void setResolutions(RemoteResolution[] remoteResolutions) {
		for (RemoteResolution remoteResolution : remoteResolutions) {
			jiraResolutions.put(remoteResolution.getId(), new JiraResolution(remoteResolution.getId(), remoteResolution.getName(),
					remoteResolution.getDescription()));
		}
	}

	public String getName() {
		return name;
	}
}
