package com.jonas.ant.db;

import java.sql.DriverManager;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class StopDerbyTask extends Task {

   public void execute() throws BuildException {
      try {
         StartDerbyTask.initialiseDriver();
         DriverManager.getConnection("jdbc:derby:;shutdown=true");
      } catch (Exception e) {
         System.out.println("Derby has been shutdown!");
      }
   }
}
