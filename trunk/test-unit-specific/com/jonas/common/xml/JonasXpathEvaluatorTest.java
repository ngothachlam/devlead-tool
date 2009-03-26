package com.jonas.common.xml;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class JonasXpathEvaluatorTest extends JonasTestCase {
	JonasXpathEvaluator jonasXpathEvaluator = new JonasXpathEvaluator("/test/child2[@childAttr='childAttrvalue']", new XMLOutputter());
	JonasXpathEvaluator jonasXpathEvaluatorWithIncorrectXpath = new JonasXpathEvaluator("/test/child2[@childAttr='chidldAttrvalue']", new XMLOutputter());
	JonasXpathEvaluator jonasXpathEvaluatorWithAttribute = new JonasXpathEvaluator("/test/child2/@childAttr", new XMLOutputter());
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
		Object xpathElementFromString = null;
		try {
			xpathElementFromString = jonasXpathEvaluator.getXpathNodesFirstElement(getTestString());
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		Element returnedElement = (Element) xpathElementFromString;
		assertEquals("child2", returnedElement.getName());
		assertEquals("child2Value", returnedElement.getText());
		
		assertEquals("child2Value", jonasXpathEvaluator.getElementText(element));
	}
	
	public void testShouldTextRepresentXMLWithAttributeCorrect() {
	   assertEquals(getTestString(), jonasXpathEvaluatorWithAttribute.getStringRepresentationOfXml(element));
	   Object xpathElementFromString = null;
	   try {
	      xpathElementFromString = jonasXpathEvaluatorWithAttribute.getXpathNodesFirstElement(getTestString());
	   } catch (Exception e) {
	      e.printStackTrace();
	      assertTrue(false);
	   }
	   Attribute returnedAttribute = (Attribute) xpathElementFromString;
	   assertEquals("childAttr", returnedAttribute.getName());
	   assertEquals("childAttrvalue", returnedAttribute.getValue());
	   
	   assertEquals("childAttrvalue", jonasXpathEvaluatorWithAttribute.getElementText(element));
	}
	
	public void testShouldTextRepresentXMLInCorrect() {
		assertEquals(getTestString(), jonasXpathEvaluatorWithIncorrectXpath.getStringRepresentationOfXml(element));
		Object xpathElementFromString = null;
		try {
			xpathElementFromString = jonasXpathEvaluatorWithIncorrectXpath.getXpathNodesFirstElement(getTestString());
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
