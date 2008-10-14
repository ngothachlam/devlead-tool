package com.jonas.common.logging;

import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MyLogger {

   static {
      BasicConfigurator.configure();
      PropertyConfigurator.configure("properties/log4j.properties");
   }

   public static Logger getLogger(Class className) {
      return Logger.getLogger(className);
   }

   public static void setup(String log4jPropertyLocation) {
      System.out.println("Setting up log4j property location \"" + log4jPropertyLocation + "\" " + new File(log4jPropertyLocation).exists());
      PropertyConfigurator.configure(log4jPropertyLocation);
   }
}
