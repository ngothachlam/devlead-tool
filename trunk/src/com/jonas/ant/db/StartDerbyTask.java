package com.jonas.ant.db;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


public class StartDerbyTask extends Task {

   public void execute() throws BuildException {
      try {
         Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
         System.out.println("Derby has been started!");
      } catch (Exception e) {
         System.out.println("Derby could not start. This is most likely " + "due to missing Derby JAR files. Please check your classpath"
               + "and try again.");
         throw new BuildException(e);
      }
   }
}
