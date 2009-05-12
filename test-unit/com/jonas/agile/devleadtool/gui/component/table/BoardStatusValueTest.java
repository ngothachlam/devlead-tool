package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;

import junit.framework.TestCase;

public class BoardStatusValueTest extends TestCase {

   public void testShouldFindTheBoardStatusValueBasedOnToString(){
      assertEquals(BoardStatusValue.get("0. UnKnown"), BoardStatusValue.UnKnown);
      assertEquals(BoardStatusValue.get("Unknown"), BoardStatusValue.UnKnown);
   }
}
