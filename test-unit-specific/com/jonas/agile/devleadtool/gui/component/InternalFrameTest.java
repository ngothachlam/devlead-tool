package com.jonas.agile.devleadtool.gui.component;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.constants.CutoverLength;
import com.jonas.agile.devleadtool.gui.component.MyInternalFrame;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class InternalFrameTest extends JonasTestCase {

   PlannerHelper helperMock = createClassMock(PlannerHelper.class);
   private List<MyInternalFrame> internalFrames = new ArrayList<MyInternalFrame>();

   protected void tearDown() throws Exception {
      super.tearDown();
      clearInternalFrames();
   }

   private void clearInternalFrames() throws PropertyVetoException {
      internalFrames.get(0).closeAll();
   }

   private MyInternalFrame getTestInternalFrame(String title) {
      MyInternalFrame internalFrame = new MyInternalFrame(title, null);
      
      internalFrames.add(internalFrame);
      return internalFrame;
   }

   public void testGetRightMostShouldWork() {
      MyInternalFrame internalFrame = getTestInternalFrame("title");

      assertRightMostFromStringWorks("p.xls", "lludevsup.xls", internalFrame, 5, 0);
      assertRightMostFromStringWorks("p.xls", "p.xls", internalFrame, 5, 0);
      assertRightMostFromStringWorks(".xls", ".xls", internalFrame, 5, -1);
   }
   
   public void testShouldCalculateSameFramesCorrectly() {
      MyInternalFrame internalFrame = getTestInternalFrame("title");
      assertEquals(1, internalFrame.getCountWithSameTitle("title"));
      assertEquals(0, internalFrame.getCountWithSameTitle("titles"));

      MyInternalFrame internalFrame2 = getTestInternalFrame("title");
      assertEquals(2, internalFrame.getCountWithSameTitle("title"));
      assertEquals(0, internalFrame.getCountWithSameTitle("titles"));
      assertEquals(2, internalFrame2.getCountWithSameTitle("title"));
      assertEquals(0, internalFrame2.getCountWithSameTitle("titles"));
   }

   public void testShouldCreateTitleCorrectly() {
      MyInternalFrame internalFrame1 = getTestInternalFrame("title");
      assertEquals("title", internalFrame1.createTitle("title"));
      
      MyInternalFrame internalFrame2 = getTestInternalFrame("title");
      assertEquals("title (1)", internalFrame2.createTitle("title"));
   }
   
   public void testShouldCalculateTitleCorrectly() {
      MyInternalFrame internalFrame = getTestInternalFrame("title");
      assertEquals("title", internalFrame.getTitle());

      internalFrame.setTitleFileName("u.xls", CutoverLength.TEST_5);
      assertEquals("u.xls", internalFrame.getTitle());
      
      internalFrame.setTitleFileName("C:\\Documents\\lludevsup.xls", CutoverLength.TEST_5);
      assertEquals("...p.xls", internalFrame.getTitle());
   }

   public void testShouldCalculateTitleCorrectlyWithDuplicates() {
      MyInternalFrame internalFrame = getTestInternalFrame("title");
      MyInternalFrame internalFrame2 = getTestInternalFrame("titles");
      MyInternalFrame internalFrame3 = getTestInternalFrame("title");
      
      assertEquals("title", internalFrame.getTitle());
      assertEquals("titles", internalFrame2.getTitle());
      assertEquals("title (1)", internalFrame3.getTitle());

      internalFrame.setTitleFileName("C:\\Documents and Settings\\jonasjolofsson\\lludevsup.xls", CutoverLength.TEST_5);

      assertEquals("...p.xls", internalFrame.getTitle());
      assertEquals("titles", internalFrame2.getTitle());
      assertEquals("title (1)", internalFrame3.getTitle());
      
      internalFrame.setTitleFileName("C:\\Documents and Settings\\jonasjolofsson\\lludevsup.xls", CutoverLength.TEST_5);
      
      assertEquals("...p.xls", internalFrame.getTitle());
      assertEquals("titles", internalFrame2.getTitle());
      assertEquals("title (1)", internalFrame3.getTitle());
   }

   private void assertRightMostFromStringWorks(String expected, String fileName, MyInternalFrame internalFrame, int givenCutoverLength, int lengthDiff) {
      String string = internalFrame.getRightMostFromString(fileName, givenCutoverLength);
      assertEquals(expected, string);
      assertEquals(givenCutoverLength + lengthDiff, string.length());
   }

}
