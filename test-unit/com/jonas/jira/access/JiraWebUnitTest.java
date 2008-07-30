package com.jonas.jira.access;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.sourceforge.jwebunit.junit.WebTester;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class JiraWebUnitTest extends TestCase {

	WebTester tester = new WebTester();
	WebClient webClient = new WebClient(BrowserVersion.FIREFOX_2);

	public void setUp() {
		// tester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
		// tester.getTestContext().setBaseUrl("http://www.google.com");
	}

	 public void tsestSearch() throws Exception {
	 tester.beginAt("/");
	 tester.setTextField("q", "htmlunit");
	 tester.submit("btnG");
	 tester.clickLinkWithText("HtmlUnit");
	 tester.assertTitleEquals("HtmlUnit - Welcome to HtmlUnit");
	 tester.assertLinkPresentWithText("Get started");
	 }

	 public void tessstHtsdfmlUnitTester() throws IOException {
	 final HtmlPage page = loginToJira();
	 // Page page2 = webClient.getPage(url);
	 // Page page2 = webClient.getPage("http://10.155.38.105" + url);
	 }

	 public void tesHtsdfmlUnitTester() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
	 final HtmlPage page3 = navigateToSearchIssuesScreen();
	
	 setSelectedOptionInSelect((HtmlSelect) page3.getHtmlElementById("pid"), new String[] { "10070" });
	 HtmlPage page4 = (HtmlPage) ((ClickableElement) page3.getHtmlElementById("projectRefreshPanel")).click();
	
	 setSelectedOptionInSelect((HtmlSelect) page4.getHtmlElementById("fixfor"), new String[] { "11382" });
	 HtmlPage page5 = (HtmlPage) ((ClickableElement) page4.getHtmlElementById("searchButton")).click();
	
	 printAllDocumentInfo(page5);
	 // printAllDocumentInfo(projectSelector.getOptions());
	 // projectSelector.setAttribute(attributeName, attributeValue)
	
	 // <select name="fixfor" id="fixfor" class="standardInputField" multiple="multiple" size="5">
	 // <option class="headerOption" value="">Any</option>
	 // <option class="headerOption" value="-1">No Fix Version</option> <option value="-2" class="sectionHeaderOption">Unreleased
	 // Versions</option>
	 // <option value="11382">Version 10</option>
	 // <option value="11432">Version 11</option>
	 // <option value="11449">Version 11 - Next Sprint (3)</option>
	 // <option value="11388">Backlog</option>
	 //
	 // <option value="11458">Pam's Backlog</option>
	 // <option value="-3" class="sectionHeaderOption">Released Versions</option>
	 // <option value="11264">Version 9</option>
	
	 }

	 private void setSelectedOptionInSelect(HtmlSelect projectSelector, String[] optionValuesToSelect) {
	 List selectedOptions = projectSelector.getSelectedOptions();
	 for (Iterator iterator = selectedOptions.iterator(); iterator.hasNext();) {
	 projectSelector.setSelectedAttribute(projectSelector.getOptionByValue(((HtmlOption) iterator.next()).getValueAttribute()),
	 false);
	 }
	 for (int i = 0; i < optionValuesToSelect.length; i++) {
	 projectSelector.setSelectedAttribute(projectSelector.getOptionByValue(optionValuesToSelect[i]), true);
	 }
	 }

	 private HtmlPage navigateToSearchIssuesScreen() throws IOException, MalformedURLException {
	 final HtmlPage page2 = loginToJira();
	
	 HtmlAnchor find_issues_link = (HtmlAnchor) page2.getHtmlElementById("find_link");
	 return (HtmlPage) find_issues_link.click();
	 }

	 private HtmlPage loginToJira() throws IOException, MalformedURLException {
	 final HtmlPage page = (HtmlPage) webClient.getPage("http://10.155.38.105/jira/secure/Dashboard.jspa");
	 assertEquals("AOL Agile User Story & Bug Tracking - AOL Agile User Story & Bug Tracking", page.getTitleText());
	
	 // Get the form that we are dealing with and within that form,
	 // find the submit button and the field that we want to change.
	 final HtmlForm form = page.getFormByName("loginform");
	
	 final HtmlTextInput textField_userName = (HtmlTextInput) form.getInputByName("os_username");
	 final HtmlPasswordInput textField_passWord = (HtmlPasswordInput) form.getInputByName("os_password");
	 final ClickableElement button = (ClickableElement) form.getInputByValue("Log In");
	
	 // Change the value of the text field
	 textField_userName.setValueAttribute("soaptester");
	 textField_passWord.setValueAttribute("soaptester");
	
	 // Now submit the form by clicking the button and get back the second page.
	 return (HtmlPage) button.click();
	 }

	 private void printAllDocumentInfo(final DomNode form) {
	 System.out.println("\n\n*******************\n\n");
	 for (Iterator iterator = form.getAllHtmlChildElements(); iterator.hasNext();) {
	 System.out.println(iterator.next());
	 }
	 }

}
