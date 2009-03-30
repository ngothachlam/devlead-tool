package com.jonas.agile.devleadtool.gui.component.table.model;

import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import junit.framework.TestCase;

public class ReconciliationTableModelTest extends TestCase {

   ReconciliationTableModel model;
   
   
   protected void setUp() throws Exception {
      super.setUp();
      model = new ReconciliationTableModel(null);
   }
   
   public void testShouldCalculateEqualsOk(){
      assertTrue(model.isEqual(null, null));
      assertTrue(model.isEqual(null, " "));
      assertFalse(model.isEqual(null, "merge"));
      assertFalse(model.isEqual("merge", null));
      assertFalse(model.isEqual("mErge", null));
      assertTrue(model.isEqual(" ", null));
      assertTrue(model.isEqual(" ", " "));
      assertTrue(model.isEqual(" ", "  "));
      assertTrue(model.isEqual("  ", " "));
      assertTrue(model.isEqual("0", "0.0"));
      assertTrue(model.isEqual("0.0", "0"));
      assertFalse(model.isEqual("0.1", "0"));
      assertFalse(model.isEqual("0.0", "0.1"));
      assertTrue(model.isEqual("1", "1.0"));
      assertTrue(model.isEqual("1.1", "1.1"));
      assertFalse(model.isEqual("1.2", "1.1"));
      assertFalse(model.isEqual("1.1", "1.2"));
      assertFalse(model.isEqual("blah", "blah2"));
      assertFalse(model.isEqual("blah2", "blah"));
      assertTrue(model.isEqual("blah", "blah"));
      assertTrue(model.isEqual("blah", " blAh "));
      assertTrue(model.isEqual(BoardStatusValue.Open, BoardStatusValue.Open));
      assertFalse(model.isEqual(BoardStatusValue.Resolved, BoardStatusValue.Open));
      assertFalse(model.isEqual(BoardStatusValue.Open, BoardStatusValue.Resolved));
      assertTrue(model.isEqual(BoardStatusValue.Complete, BoardStatusValue.Complete));
      System.out.println("Finished!");
   }

}
