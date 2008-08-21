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

	protected Element getXpathNodesFirstElement(String stringRepresentationOfXml) throws JDOMException, IOException {
		List<Element> selectNodes = getXpathNodes(stringRepresentationOfXml);
		return selectNodes.size() > 0 ? selectNodes.get(0) : null;
	}

	public String getElementText(Element element) {
		String stringRepresentationOfXml = getStringRepresentationOfXml(element);
		try {
			Element xPathNode = getXpathNodesFirstElement(stringRepresentationOfXml);
			return xPathNode != null ? xPathNode.getText() : null;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

   public List<Element> getXpathNodes(String stringRepresentationOfXml) throws JDOMException, IOException {
      SAXBuilder sb = new SAXBuilder(false);
      Document doc = sb.build(new StringReader(stringRepresentationOfXml));
      List<Element> selectNodes = xpath.selectNodes(doc);
      return selectNodes;
   }
}
