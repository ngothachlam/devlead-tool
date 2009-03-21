package com.jonas.common;

import java.util.Calendar;
import junit.framework.TestCase;

public class DateHelperTest extends TestCase {

   DateHelper helper;

   @Override
   protected void setUp() throws Exception {
      helper = new DateHelper();
   }
   
   public void testShouldTestGettinNiceDateAsStringOk(){
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_MONTH, 21);
      calendar.set(Calendar.MONTH, 2);
      calendar.set(Calendar.YEAR, 2009);
      
      assertEquals( "21/03-2009", helper.getDateAsString(calendar.getTime()));
   }
   
   
}
