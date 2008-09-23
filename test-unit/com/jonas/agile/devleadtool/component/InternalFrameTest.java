package com.jonas.agile.devleadtool.component;

import java.util.ArrayList;
import java.util.List;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class InternalFrameTest extends JonasTestCase {

   PlannerHelper helperMock = createClassMock(PlannerHelper.class);
   private List<InternalFrame> internalFrames = new ArrayList<InternalFrame>();

   protected void tearDown() throws Exception {
      super.tearDown();
      clearInternalFrames();
   }

   private void clearInternalFrames() {
      for (InternalFrame internalFrame : internalFrames) {
         internalFrame.close();
      }
   }

   private InternalFrame getTestInternalFrame(String title) {
      InternalFrame internalFrame = new InternalFrame(title);
      internalFrames.add(internalFrame);
      return internalFrame;
   }

   public void testGetRightMostShouldWork() {
      InternalFrame internalFrame = getTestInternalFrame("title");

      assertRightMostFromStringWorks("p.xls", "lludevsup.xls", internalFrame, 5, 0);
      assertRightMostFromStringWorks("p.xls", "p.xls", internalFrame, 5, 0);
      assertRightMostFromStringWorks(".xls", ".xls", internalFrame, 5, -1);
   }

   public void testShouldCalculateTitleCorrectly() {
      InternalFrame internalFrame = getTestInternalFrame("title");
      assertEquals("title", internalFrame.getTitle());

      internalFrame.setFileName("C:\\Documents\\lludevsup.xls", 5);
      assertEquals("title - ...p.xls", internalFrame.getTitle());
   }

   public void testShouldCalculateSameFramesCorrectly() {
      InternalFrame internalFrame = getTestInternalFrame("title");
      assertEquals(1, internalFrame.getCountWithSameTitle("title"));
      assertEquals(0, internalFrame.getCountWithSameTitle("titles"));

      InternalFrame internalFrame2 = getTestInternalFrame("title");
      assertEquals(2, internalFrame.getCountWithSameTitle("title"));
      assertEquals(0, internalFrame.getCountWithSameTitle("titles"));
      assertEquals(2, internalFrame2.getCountWithSameTitle("title"));
      assertEquals(0, internalFrame2.getCountWithSameTitle("titles"));
   }
   
   public void testShouldCreateTitleCorrectly() {
      InternalFrame internalFrame1 = getTestInternalFrame("title");
      assertEquals("title", internalFrame1.createTitle("title"));
      
      InternalFrame internalFrame2 = getTestInternalFrame("title");
      assertEquals("title (1)", internalFrame2.createTitle("title"));
   }

   public void testShouldCalculateTitleCorrectlyWithDuplicates() {
      InternalFrame internalFrame = getTestInternalFrame("title");
      InternalFrame internalFrame2 = getTestInternalFrame("titles");
      InternalFrame internalFrame3 = getTestInternalFrame("title");
      
      assertEquals("title", internalFrame.getTitle());
      assertEquals("titles", internalFrame2.getTitle());
      assertEquals("title (1)", internalFrame3.getTitle());

      internalFrame.setExcelFile("C:\\Documents and Settings\\jonasjolofsson\\lludevsup.xls");

      assertEquals("title - ...ttings\\jonasjolofsson\\lludevsup.xls", internalFrame.getTitle());
      assertEquals("titles", internalFrame2.getTitle());
      assertEquals("title (1)", internalFrame3.getTitle());
      
      internalFrame.setExcelFile("C:\\Documents and Settings\\jonasjolofsson\\lludevsup.xls");
      
      assertEquals("title - ...ttings\\jonasjolofsson\\lludevsup.xls", internalFrame.getTitle());
      assertEquals("titles", internalFrame2.getTitle());
      assertEquals("title (1)", internalFrame3.getTitle());
   }

   private void assertRightMostFromStringWorks(String expected, String fileName, InternalFrame internalFrame, int givenCutoverLength, int lengthDiff) {
      String string = internalFrame.getRightMostFromString(fileName, givenCutoverLength);
      assertEquals(expected, string);
      assertEquals(givenCutoverLength + lengthDiff, string.length());
   }

}
