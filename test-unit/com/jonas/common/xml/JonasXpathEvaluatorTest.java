package com.jonas.common.xml;

import java.io.IOException;

import junit.framework.TestCase;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

public class JonasXpathEvaluatorTest extends TestCase {
	JonasXpathEvaluator jonasXpathEvaluator = new JonasXpathEvaluator("/test/child2[@childAttr='childAttrvalue']");
	JonasXpathEvaluator jonasXpathEvaluatorWithIncorrectXpath = new JonasXpathEvaluator("/test/child2[@childAttr='chidldAttrvalue']");
	private Element element;

	public JonasXpathEvaluatorTest() {
		element = new Element("test");
		element.setAttribute(new Attribute("attr", "attrvalue"));
		element.addContent(new Element("child"));
		Element element2 = new Element("child2");
		element2.setAttribute(new Attribute("childAttr", "childAttrvalue"));
		element2.setText("child2Value");
		element.addContent(element2);
	}

	public void testShouldTextRepresentXMLCorrect() {
		assertEquals(getTestString(), jonasXpathEvaluator.getStringRepresentationOfXml(element));
		Element xpathElementFromString = null;
		try {
			xpathElementFromString = jonasXpathEvaluator.getXpathElementFromString(getTestString());
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertEquals("child2", xpathElementFromString.getName());
		assertEquals("child2Value", xpathElementFromString.getText());
		
		assertEquals("child2Value", jonasXpathEvaluator.getElementText(element));
	}
	
	public void testShouldTextRepresentXMLInCorrect() {
		assertEquals(getTestString(), jonasXpathEvaluatorWithIncorrectXpath.getStringRepresentationOfXml(element));
		Element xpathElementFromString = null;
		try {
			xpathElementFromString = jonasXpathEvaluatorWithIncorrectXpath.getXpathElementFromString(getTestString());
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertEquals(null, xpathElementFromString);
		
		assertEquals(null, jonasXpathEvaluatorWithIncorrectXpath.getElementText(element));
	}

	private String getTestString() {
		return "<test attr=\"attrvalue\"><child /><child2 childAttr=\"childAttrvalue\">child2Value</child2></test>";
	}

}
