package com.jonas.ant.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class TestDatabaseTask extends Task {

   private String url;
   private String sql;

   public String getSql() {
      return sql;
   }

   public void setSql(String sql) {
      this.sql = sql;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void execute() throws BuildException {
      Connection connection = null;
      Statement statement = null;
      try {
//         Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
         StartDerbyTask.initialiseDriver();
         connection = DriverManager.getConnection(url);
         statement = connection.createStatement();
         ResultSet rs = statement.executeQuery(sql);

         // Fetch each row from the result set
         while (rs.next()) {
            // Get the data from the row using the column index
            printOutInt(rs, "id");
            printOutString(rs, "name");
         }

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (statement != null) {
            try {
               statement.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
         if (connection != null) {
            try {
               connection.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
      }
   }

   private void printOutString(ResultSet rs, String column) throws SQLException {
      System.out.print(column + ": " );
      String int1 = rs.getString(column);
      System.out.println(int1);
   }
   
   private void printOutInt(ResultSet rs, String column) throws SQLException {
      System.out.print(column + ": " );
      int int1 = rs.getInt(column);
      System.out.println(int1);
   }
}
