package com.jonas.agile.devleadtool.funtionaltest;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class LoadingBurnUpDataFunctionalTest {

   
   @Test
   public void shouldLoadOk(){
      File burnUpXls = new File("test-data//BurnUpTest.xls");
      assertTrue(burnUpXls.exists());
   }
}
