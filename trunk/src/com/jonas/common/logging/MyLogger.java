package com.jonas.common.logging;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MyLogger {

   private static String log4jPropertyLocation = "properties/log4j.properties";

   private static boolean setup = false;
   private final static Map<Class<?>, Logger> logs = new HashMap<Class<?>, Logger>();

   public static Logger getLogger(Class className) {
      if (!setup) {
         synchronized (MyLogger.class) {
            if (!setup) {
               setup = true;
               setup(log4jPropertyLocation);
            }
         }
      }
      return Logger.getLogger(className);
   }

   public static void setup(String log4jPropertyLoc) {
      synchronized (MyLogger.class) {
         setup = true;
      }
      MyLogger.log4jPropertyLocation = log4jPropertyLoc;
      boolean exists = new File(log4jPropertyLoc).exists();
      System.out.println("Setting up log4j property location \"" + log4jPropertyLoc + "\" and it is " + (!exists ? "NOT " : "") + "a file!");
      BasicConfigurator.configure();
      PropertyConfigurator.configure(log4jPropertyLoc);
   }

   public static void warn(Class<?> class1, String string) {
      Logger logger = logs.get(class1);
      if (logger == null) {
         synchronized (logs) {
            if (!logs.containsKey(class1)) {
               logger = getLogger(class1);
               logs.put(class1, logger);
            }
         }
      }
      logger.warn(string);
   }
}
