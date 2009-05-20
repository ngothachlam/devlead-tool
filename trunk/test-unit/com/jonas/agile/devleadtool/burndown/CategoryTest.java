package com.jonas.agile.devleadtool.burndown;

import static org.junit.Assert.*;

import org.junit.Test;


public class CategoryTest {

   @Test
   public void testShouldCompareOk(){
      assertEquals(0, new Category("Real Progression").compareTo(new Category("Ideal Progression")));
      assertEquals(-3, new Category("Open").compareTo(new Category("Resolved")));
      assertEquals(2, new Category("Complete").compareTo(new Category("InProgress")));
      assertEquals(1, new Category("Complete").compareTo(new Category("Blah")));
      assertEquals(-1, new Category("Blah").compareTo(new Category("Complete")));
   }
   
}
