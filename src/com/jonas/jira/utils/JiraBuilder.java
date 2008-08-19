package com.jonas.jira.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraBuilder {

	private static final Logger log = MyLogger.getLogger(JiraBuilder.class);

	private static final List<XPathImplementor> jiraXpathActions = new ArrayList<XPathImplementor>();

	static {
		XpathAction xpathAction = new XpathAction() {
			public void XPathValueFound(String xpathValue, JiraIssue jira) {
				jira.setBuildNo(xpathValue);
			}
		};
		String xPath = "/item/customfields/customfield[@id='customfield_10160']/customfieldvalues/customfieldvalue";
		jiraXpathActions.add(new XPathImplementor(xPath, xpathAction));
	}

	public static JiraIssue buildJira(Element e, List<JiraVersion> fixVersions) {
		JiraIssue jira = buildJira(e);
		jira.addFixVersions(fixVersions.get(0));
		if (fixVersions.size() > 1) {
			log.error("Cannot handle more than one fix version at the moment for " + jira.getKey());
		}
		return jira;
	}

	public static JiraIssue buildJira(Element e) {
		return new JiraIssue(get(e, "key"), get(e, "summary"), get(e, "status"), get(e, "resolution"));
	}

	public static String get(Element element, String string) {
		return element.getChildText(string);
	}

	public static List<JiraIssue> buildJiras(List<Element> list) {
		List<JiraIssue> jiras = new ArrayList<JiraIssue>();
		for (Iterator<Element> iterator = list.iterator(); iterator.hasNext();) {
			Element e = iterator.next();
			List<Element> fixVersionStrings = e.getChildren("fixVersion");
			List<JiraVersion> versions = new ArrayList<JiraVersion>();
			for (Iterator<Element> iterator2 = fixVersionStrings.iterator(); iterator2.hasNext();) {
				Element element = iterator2.next();
				JiraVersion versionByName = JiraVersion.getVersionByName(element.getText());
				// TODO separate logic!
				if (versionByName == null) {
					// FIXME use id to get JiraVersion!!
					JiraProject projectByKey = JiraProject.getProjectByKey(PlannerHelper.getProjectKey(get(e, "key")));
					try {
						projectByKey.getJiraClient().getFixVersionsFromProject(projectByKey, false);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				versionByName = JiraVersion.getVersionByName(element.getText());
				JiraIssue.log.debug("Getting version byName: \"" + element.getText() + "\" is \"" + versionByName + "\"");
				versions.add(versionByName);
			}
			JiraIssue jiraIssue = buildJira(e, versions);

			for (XPathImplementor xPathImplementor : jiraXpathActions) {
				xPathImplementor.execute(e, jiraIssue);
			}

			jiras.add(jiraIssue);

		}
		return jiras;
	}
}

interface XpathAction {
	public void XPathValueFound(String xpathValue, JiraIssue jira);
}

class XPathImplementor {

	private final XpathAction xpathAction;
	private JonasXpathEvaluator xpathEvaluator;

	public XPathImplementor(String xpathExpression, XpathAction xpathAction) {
		this.xpathAction = xpathAction;
		xpathEvaluator = new JonasXpathEvaluator(xpathExpression);
	}

	public void execute(Element element, JiraIssue jira) {
		xpathAction.XPathValueFound(xpathEvaluator.getElementText(element), jira);
	}

}
