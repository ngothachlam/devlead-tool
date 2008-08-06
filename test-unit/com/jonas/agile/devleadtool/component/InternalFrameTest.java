package com.jonas.agile.devleadtool.component;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

import com.jonas.agile.devleadtool.PlannerHelper;

public class InternalFrameTest extends TestCase {

	PlannerHelper helperMock = EasyMock.createMock(PlannerHelper.class);

	public void testShouldCalculateTitleCorrectly() {
		InternalFrame internalFrame = new InternalFrame(helperMock, "title");
		InternalFrame internalFrame2 = new InternalFrame(helperMock, "title");
		InternalFrame internalFrame3 = new InternalFrame(helperMock, "titles");
		helperMock.setActiveInternalFrame(internalFrame);
		helperMock.setActiveInternalFrame(internalFrame2);
		helperMock.setActiveInternalFrame(internalFrame3);
		EasyMock.replay(helperMock);
		assertEquals("title", internalFrame.getTitle());
		assertEquals("title (1)", internalFrame2.getTitle());
		assertEquals("titles", internalFrame3.getTitle());
		internalFrame.setExcelFile("C:\\Documents and Settings\\jonasjolofsson\\lludevsup.xls");
		assertEquals("title - ...ttings\\jonasjolofsson\\lludevsup.xls", internalFrame.getTitle());
		assertEquals("title (1)", internalFrame2.getTitle());
		assertEquals("titles", internalFrame3.getTitle());
	}

	public void testGetRightMostShouldWork() {
		InternalFrame internalFrame = new InternalFrame(helperMock, "title");
		EasyMock.replay(helperMock);
		assertSettingTitleIsCorrect("p.xls", "lludevsup.xls", internalFrame, 5, 0);
		assertSettingTitleIsCorrect("p.xls", "p.xls", internalFrame, 5, 0);
		assertSettingTitleIsCorrect(".xls", ".xls", internalFrame, 5, -1);
	}

	private void assertSettingTitleIsCorrect(String expected, String fileName, InternalFrame internalFrame,
			int givenCutoverLength, int lengthDiff) {
		String string = internalFrame.getRightMostFromString(fileName, givenCutoverLength);
		assertEquals(expected, string);
		assertEquals(givenCutoverLength + lengthDiff, string.length());
	}
}
