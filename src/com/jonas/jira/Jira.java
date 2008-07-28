package com.jonas.jira;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

public class Jira {

	private final String name;
	private final String fixVersion;
	private final String status;
	private final String resolution;

	private Jira(Element e) {
		this(get(e, "key"), get(e, "fixVersion"), get(e, "status"), get(e, "resolution"));
	}

	public String getName() {
		return name;
	}

	public String getFixVersion() {
		return fixVersion;
	}

	public String getStatus() {
		return status;
	}

	public String getResolution() {
		return resolution;
	}

	public Jira(String name, String fixVersion, String status, String resolution) {
		this.name = name;
		this.fixVersion = fixVersion;
		this.status = status;
		this.resolution = resolution;
	}

	public static List<Jira> buildJiras(List<Element> list) {
		List<Jira> jiras = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			jiras.add(new Jira(e));
		}
		return jiras;
	}

	private static String get(Element element, String string) {
		return element.getChildText(string);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[Jira: (");
		sb.append("Name: \"").append(name).append("\"");
		sb.append(", FixVersion: \"").append(fixVersion).append("\"");
		sb.append(", Status: \"").append(status).append("\"");
		sb.append(", Resolution: \"").append(resolution).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
