package com.jonas.testing.jxtreetable.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DerbyDaoImpl {
   public String driver = "org.apache.derby.jdbc.EmbeddedDriver"; 
   public String protocol = "jdbc:derby:"; 

   public void getSprint() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
      Class.forName(driver).newInstance();
      
      Properties props;
      Connection conn = DriverManager.getConnection(protocol + "SprinterDB;"); 
      
   }
   
}
