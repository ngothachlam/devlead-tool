package com.jonas.netbeans.swingbuilding;

import java.io.File;
import junit.framework.TestCase;

public class ResourceMapTest extends TestCase {

   ResourceMap resourceMap;

   protected void setUp() throws Exception {
      super.setUp();
      resourceMap = new ResourceMap();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldSetRightXMLFile() {
      resourceMap.setForm(new File("src//com//jonas//guibuilding//DesktopView.form"));
      assertEquals("C:\\Documents and Settings\\jonasjolofsson\\My Documents\\workspace\\devlead-tool\\src\\com\\jonas\\guibuilding\\DesktopView.form",
            resourceMap.getForm().getAbsolutePath());
   }

   public void testShouldGetRightResourceString() {
      assertEquals("refreshButton.text", resourceMap.getString("refreshButton.text"));
   }
}
