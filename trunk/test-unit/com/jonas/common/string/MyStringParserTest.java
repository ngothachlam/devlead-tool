package com.jonas.common.string;

import java.util.List;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class MyStringParserTest extends JonasTestCase {

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }
   
   public void testShouldSeparateStringsOk(){
      MyStringParser parser = new MyStringParser();
      List<String> separatedStrings = parser.separateString("llu-1 llu-2, llu-3;llu-4,llu-5");
      
      assertEquals(5, separatedStrings.size());
   }

}
