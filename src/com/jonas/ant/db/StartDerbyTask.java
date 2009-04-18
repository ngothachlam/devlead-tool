package com.jonas.ant.db;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class StartDerbyTask extends Task {

   private static Class<?> driver = null;

   public void execute() throws BuildException {
      try {
         driver = null;
         initialiseDriver();
      } catch (Exception e) {
         System.out.println("Derby could not start. Most likely " + "due to missing Derby JAR files in classpath.");
         throw new BuildException(e);
      }
   }

   public static void initialiseDriver() throws ClassNotFoundException {
      if (driver == null) {
         synchronized (StartDerbyTask.class) {
            if (driver == null) {
               driver = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
               System.out.println("Derby has been started!");
            }
         }
      }
   }
}
