package com.jonas.common.logging;

import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MyLogger {

   private static String log4jPropertyLocation = "properties/log4j.properties";

   private static boolean setup = false;

   public static Logger getLogger(Class className) {
      System.out.println("starting getLogger for " + className);
      if (!setup) {
         synchronized (MyLogger.class) {
            if (!setup) {
               setup = true;
               setup(log4jPropertyLocation);
            }
         }
      }
      Logger logger = Logger.getLogger(className);
      System.out.println("finishing getLogger for " + className);
      return logger;
   }

   public static void setup(String log4jPropertyLoc) {
      System.out.println("start setup!");
      synchronized (MyLogger.class) {
         setup = true;
      }
      MyLogger.log4jPropertyLocation = log4jPropertyLoc;
      boolean exists = new File(log4jPropertyLoc).exists();
      System.out.println("Setting up log4j property location \"" + log4jPropertyLoc + "\" and it is " + (!exists ? "NOT " : "") + "a file!");
      BasicConfigurator.configure();
      PropertyConfigurator.configure(log4jPropertyLoc);
      System.out.println("finished!");
   }
}
