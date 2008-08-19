package com.jonas.common.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

public class JonasXpathEvaluator {

	private XPath xpath;

	public JonasXpathEvaluator(String xpathExpression) {
		try {
			xpath = XPath.newInstance(xpathExpression);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	protected String getStringRepresentationOfXml(Element element) {
		StringWriter stringWriter = new StringWriter();
		try {
			new XMLOutputter().output(element, stringWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringWriter.getBuffer().toString();
	}

	protected Element getXpathElementFromString(String stringRepresentationOfXml) throws JDOMException, IOException {
		SAXBuilder sb = new SAXBuilder(false);
		Document doc = sb.build(new StringReader(stringRepresentationOfXml));
		List<Element> selectNodes = xpath.selectNodes(doc);
		return selectNodes.get(0);
	}
	
	public String getElementText(Element element){
		String stringRepresentationOfXml = getStringRepresentationOfXml(element);
		try {
			Element xPathNode = getXpathElementFromString(stringRepresentationOfXml);
			return xPathNode.getText();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
