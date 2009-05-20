package com.jonas.agile.devleadtool.gui.action;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class SaveDataActionTest {

   @Test
   public void shouldCalculateRightSaveFile() {
      SaveDataAction action = new SaveDataAction("", "", null, null, null, null);
      File file = action.getFileForHistoricalSave(new File("bin"), new File("bin//this is an originalfile.xls"));
      assertEquals("HISTORICAL - this is an originalfile.csv", file.getName());
      assertEquals("bin", file.getParentFile().getName());
   }

}
