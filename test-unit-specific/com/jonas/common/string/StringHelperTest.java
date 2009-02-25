package com.jonas.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import junit.framework.TestCase;

public class StringHelperTest extends TestCase {

   private StringHelper helper;

   @Override
   protected void setUp() throws Exception {
      helper = new StringHelper();
   }

   public void testShouldSeparateStringFine() {
      assertEquals(getListFromArray("12-12"), helper.separate("12-12", " \t,"));
      assertEquals(getListFromArray("12-12", "12-12"), helper.separate("12-12,12-12", " \t,"));
      assertEquals(getListFromArray("12-12", "12-12"), helper.separate("12-12, 12-12", " \t,"));
      assertEquals(getListFromArray("12-12", "12-12"), helper.separate(" , 12-12, 12-12  ", " \t,"));
      assertEquals(getListFromArray("12-2<3", "13-11"), helper.separate(" , 12-2<3 , 13-11  ", " \t,"));
      assertEquals(getListFromArray("12-1<2"), helper.separate("12-1<2", " \t,"));
      assertEquals(getListFromArray("12-1<2", "13-1"), helper.separate("12-1<2, 13-1", " \t,"));
   }

   public void testShouldGetPrefixStartAndEndOk() {
      assertResultDTO(new ResultDTO("12-1", null, null), helper.getPrefixStartAndEndNo("12-1", "<"));
      assertResultDTO(new ResultDTO("12-", "1", "2"), helper.getPrefixStartAndEndNo("12-1<2", "<"));
      assertResultDTO(new ResultDTO("12-", "1", "4"), helper.getPrefixStartAndEndNo("12-1<4", "<"));
      try {
         assertResultDTO(new ResultDTO("12-1", null, null), helper.getPrefixStartAndEndNo("12-1<", "<"));
         assertTrue("should not be able to parse the \"12-1<\" string", false);
      } catch (ParseException e) {

      }
   }

   public void testShouldReturnCorrectStartAndEndList() {
      ResultDTO argumentDto = new ResultDTO("12-", "1", "2");
      List<String> actualList = helper.getListFromStartAndEnd(argumentDto);
      assertList(actualList, "12-1", "12-2");

      argumentDto = new ResultDTO("1-", "1", "3");
      actualList = helper.getListFromStartAndEnd(argumentDto);

      assertList(actualList, "1-1", "1-2", "1-3");
   }

   public void testShouldSeparateStringFineWithKeyWorkTO() {
      try {
         assertEquals(getListFromArray("12-1"), helper.generateSeparateList("12-1<"));
         assertTrue("should not be able to parse the \"12-1<\" string", false);
      } catch (ParseException e) {

      }
      assertEquals(getListFromArray("12-1", "12-2"), helper.generateSeparateList("12-1<2"));
      assertEquals(getListFromArray("12-1", "12-2", "13-1"), helper.generateSeparateList("12-1<2, 13-1"));
      assertEquals(getListFromArray("12-1", "12-2", "13-1"), helper.generateSeparateList("12-1<2, 13-1"));
   }

   private void assertList(List<String> actualList, String... expectedListEntries) {
      for (int i = 0; i < expectedListEntries.length; i++) {
         assertEquals(expectedListEntries[i], actualList.get(i));
      }
      assertEquals(expectedListEntries.length, actualList.size());
   }

   private void assertResultDTO(ResultDTO expectedDto, ResultDTO actualDTO) {
      assertEquals(expectedDto.getStart(), actualDTO.getStart());
      assertEquals(expectedDto.getPrefix(), actualDTO.getPrefix());
      assertEquals(expectedDto.getEnd(), actualDTO.getEnd());
   }

   private List<String> getListFromArray(String... string) {
      List<String> result = new ArrayList<String>(string.length);
      for (String string2 : string) {
         result.add(string2);
      }
      return result;
   }

}
