package com.jonas.agile.devleadtool.component.tree.xml;

import junit.framework.TestCase;

public class JiraDTOTest extends TestCase {

   public void testShouldCutStringOk() {
      JiraDTO dto = new JiraDTO();

      assertEquals("1234", dto.cutString("1234", 6));
      assertEquals("1234", dto.cutString("1234", 5));
      assertEquals("1234", dto.cutString("1234", 4));
      assertEquals("123...", dto.cutString("1234", 3));
      assertEquals("12...", dto.cutString("1234", 2));
      assertEquals("1...", dto.cutString("1234", 1));
      assertEquals("...", dto.cutString("1234", 0));
      try {
         assertEquals("", dto.cutString("1234", -1));
         assertTrue(false);
      } catch (Exception e) {
      }
   }

}
