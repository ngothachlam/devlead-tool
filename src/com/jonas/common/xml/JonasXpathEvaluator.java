package com.jonas.common.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import org.jdom.Attribute;
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
         // xpath.addNamespace("xmlns", "http://www.w3.org/2001/XMLSchema");
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

   protected Object getXpathNodesFirstElement(String stringRepresentationOfXml) throws JDOMException, IOException {
      List<Object> selectNodes = getXpathNodes(stringRepresentationOfXml);
      return selectNodes.size() > 0 ? selectNodes.get(0) : null;
   }

   public String getElementText(Element element) {
      String stringRepresentationOfXml = getStringRepresentationOfXml(element);
      try {
         String string = null;
         Object xPathNode = getXpathNodesFirstElement(stringRepresentationOfXml);
         if (xPathNode instanceof Element) {
            Element tempElement = (Element) xPathNode;
            string = xPathNode != null ? tempElement.getText() : null;
         } else if (xPathNode instanceof Attribute) {
            Attribute tempAttribute = (Attribute) xPathNode;
            string = xPathNode != null ? tempAttribute.getValue() : null;
         } else if (xPathNode != null) {
            throw new UnsupportedOperationException("xPathNode is of unsupported class: " + xPathNode.getClass());
         }
         return string;
      } catch (JDOMException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;

   }

   private List<Object> getXpathNodes(String stringRepresentationOfXml) throws JDOMException, IOException {
      SAXBuilder sb = new SAXBuilder(false);
      Document doc = sb.build(new StringReader(stringRepresentationOfXml));
      return xpath.selectNodes(doc);
   }
   
   public List<Element> getXpathElements(String stringRepresentationOfXml) throws JDOMException, IOException {
      SAXBuilder sb = new SAXBuilder(false);
      Document doc = sb.build(new StringReader(stringRepresentationOfXml));
      return xpath.selectNodes(doc);
   }
}
