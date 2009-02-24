package com.jonas.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import junit.framework.TestCase;

public class StringHelperTest extends TestCase {

   public void testShouldSeparateStringFine(){
      StringHelper helper = new StringHelper();
      
      assertEquals(expected("12-12"), helper.separate("12-12", " \t,"));
      assertEquals(expected("12-12", "12-12"), helper.separate("12-12,12-12", " \t,"));
      assertEquals(expected("12-12", "12-12"), helper.separate("12-12, 12-12", " \t,"));
      assertEquals(expected("12-12", "12-12"), helper.separate(" , 12-12, 12-12  ", " \t,"));
   }
   
   public void testShouldGetPrefixStartAndEndOk(){
      StringHelper helper = new StringHelper();
      
      assertResultDTO(new ResultDTO("12-", "1", "2"), helper.getPrefixStartAndEndNo("12-1<2", "<"));
      assertResultDTO(new ResultDTO("12-", "1", "4"), helper.getPrefixStartAndEndNo("12-1<4", "<"));
   }

   private void assertResultDTO(ResultDTO expectedDto, ResultDTO actualDTO) {
      assertEquals(expectedDto.getStart(), actualDTO.getStart());
      assertEquals(expectedDto.getPrefix(), actualDTO.getPrefix());
      assertEquals(expectedDto.getEnd(), actualDTO.getEnd());
   }
   
   public void testShouldSeparateStringFineWithKeyWorkTO(){
      StringHelper helper = new StringHelper();
      
      assertEquals(expected("12-1, 12-2"), helper.generateSeparateList("12-1<2", "<"));
   }

   private List<String> expected(String... string) {
      List<String> result = new ArrayList<String>(string.length);
      for (String string2 : string) {
         result.add(string2);
      }
      return result;
   }
   
}
