For getting xml back from Jira to see what it looks like: use the JiraXMLHelper!

For testing soap and httpclient interaction. Try JiraSoapClientFunctionalTest and ... respectively. 


TODO:

*Add a new sprint screen: 
	* Updates board sprint to new sprint number if in-development or lower
*Move project from being a dropdown in jira to a textfield with an id
	* let the id be stored in the swing app against a name so that it can be renamed!

Jira Table: 
* Red when build number not filled out and resolved=<
* Add Project to the Board Table
* Add regex to filter ren when fixversion is not of type: ^jiraproject-[0-9]+%

BoardTable:
* Add Sprint to the Board Table
* Add Project to the Board Table
* Add colour coding between jira board and board regarding 
	* jirastatus vs boardstatus
	* sprint
	* project not filled in

	
Tree: 
* Add ordering
* Move buttons from below tree to become right click!
* Add sprint management to the database or save the tree! 
* Add a highlight on rigth click: "higlight parents that have "Resolved", "Closed", etc jiras
* Add better and more dynamic DnD tree support
	* Add some different views: (existing is: LLU\Sprint\FixVersion\Jira
		* LLU\FixVersion\Sprint\jira
		* LLU\Project\FixVersion\jira
		* LLU\Project\Sprint\jira
* Better Info on Tree 
	* When you select Node info
		* when doing this on a Sprint - display the sprintInfo, 
		* when doing this on a Jira - display the JiraInfo,
		* ... project
		* ... fix version
	* 