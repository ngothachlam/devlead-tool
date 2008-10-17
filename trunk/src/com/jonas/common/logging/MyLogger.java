package com.jonas.common.logging;

import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MyLogger {

   private static String log4jPropertyLocation = "properties/log4j.properties";

   static {
      setup(log4jPropertyLocation);
   }

   public static Logger getLogger(Class className) {
      return Logger.getLogger(className);
   }

   public static void setup(String log4jPropertyLoc) {
      log4jPropertyLocation = log4jPropertyLoc;
      boolean exists = new File(log4jPropertyLocation).exists();
      System.out.println("Setting up log4j property location \"" + log4jPropertyLocation + "\" and it is " + (!exists ? "NOT " : "") + "a file!");
      BasicConfigurator.configure();
      PropertyConfigurator.configure(log4jPropertyLocation);
   }
}
