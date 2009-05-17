package com.jonas.agile.devleadtool.funtionaltest;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.jonas.agile.devleadtool.burndown.BurnUpDao;

public class LoadingBurnUpDataFunctionalTest {

   
   @Test
   public void shouldLoadOk(){
      File burnUpXls = new File("test-data//BurnUpTest.xls");
      BurnUpDao dao = new BurnUpDao();
      
      
      assertTrue(burnUpXls.exists());
   }
}
