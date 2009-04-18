package com.jonas.ant.db;

import java.sql.DriverManager;
import java.util.ArrayList;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class StopDatabaseTask extends Task {

   private String url;
   
   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void execute() throws BuildException {
      try {
         String newUrl = url + ";shutdown=true";
         System.out.println(newUrl);
         StartDerbyTask.initialiseDriver();
         DriverManager.getConnection(newUrl);
         System.out.println("Database has been shutdown");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
