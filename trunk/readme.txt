For getting xml back from Jira to see what it looks like: use the JiraXMLHelper!

For testing soap and httpclient interaction. Try JiraSoapClientFunctionalTest and ... respectively. 

TODO:
* Present Board Panel's board stats
	User gives: Length of Sprint, Where in current sprint
	Given from panel: Total Dev Estimates, Total QA Estimates, Remaining Dev Est, Remaining QA Estimates
	Calculate further: Estimated length of sprint
	Add further: Show no of bugs, merges in sprint as well. 
* Add Types: Bug, Merge, Other (to be parsed in board stats)


*BUG: The file name is not loaded onto the planner on the first load. 

*Add a new sprint screen: 
	* Updates board sprint to new sprint number if in-development or lower
*Move project from being a dropdown in jira to a textfield with an id
	* let the id be stored in the swing app against a name so that it can be renamed!

Jira Table: 
* Red when build number not filled out and resolved=<
* Add regex to filter red when fixversion is not of type: ^jiraproject-[0-9]+%
* Add colour coding between jira board and board regarding 
	* jirastatus vs boardstatus
	* sprint
	* project not filled in

BoardTable:
* Add a 'mark when adding' to the add panel so that you know who has been added or updated
* Add Sprint to the Board Table
* Add Project to the Board Table
* Add a "download jira's board representation"
	* Get all issues with the specified sprint
	* Trawl through all non-closed issues with a sprint filled out
		* (Maybe by having "add outstanding jiras to sprintboard" by selecting sprints in the tree?)
	
Tree: 
* Sprintstats panel is very likely to have a bug if a jira has several fix versions that it double adds the estimate and actual in the SprintInfo panel?
* Add ordering
* Move buttons from below tree to become right click!
* Add sprint management to the database or save the tree! 
* Add a highlight on right click: "highlight parents that have "Resolved", "Closed", etc jiras
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
* DONE: Adding sprints by comma separation
* DONE: Copy to table on rightclick functionality implemented