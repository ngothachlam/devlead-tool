package com.jonas.stats.charts.common.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import com.jonas.agile.devleadtool.burndown.ContentsDto;

public class DBDao implements Dao {

   private String sql;
   private String host;
   private String port;
   private String user;
   private String password;

   public DBDao(String sql) {
      this.sql = sql;
      host = "10.155.57.51";
      port = "3106";
      user = "llusa";
      password = "password";
   }

   @Override
   public ContentsDto loadContents() throws IOException {
      Connection con = null;
      Statement stmt = null;
      ResultSet rs = null;
      try {
         Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
         String url = "jdbc:sybase:Tds:" + host + ":" + port;

         con = DriverManager.getConnection(url, user, password);
         stmt = con.createStatement();
         rs = stmt.executeQuery(sql);
         
         ResultSetMetaData md = rs.getMetaData();
         md.getColumnCount();

         Vector<Object> header = new Vector<Object>(md.getColumnCount());
         Vector<Vector<Object>> body = new Vector<Vector<Object>>();

         for (int colCounter = 1; colCounter <= md.getColumnCount(); colCounter++) {
            String colName = md.getColumnName(colCounter);
            header.add(colName);
         }

         while (rs.next()) {
            Vector<Object> row = createRow(rs, md.getColumnCount());
            body.add(row);
         }

         return new ContentsDto(header, body);
      } catch (Exception e) {
         throw new RuntimeException(e);
      } finally {
         if (rs != null) {
            try {
               rs.close();
            } catch (SQLException e) {
               throw new RuntimeException(e);
            }
         }
         if (stmt != null) {
            try {
               stmt.close();
            } catch (SQLException e) {
               throw new RuntimeException(e);
            }
         }
         
         if (con != null) {
            try {
               con.close();
            } catch (SQLException e) {
               throw new RuntimeException(e);
            }
         }
      }
   }

   public Vector<Object> createRow(ResultSet rs, int colCount) throws SQLException {
      Vector<Object> result = new Vector<Object>(colCount);

      System.out.print("Read in [");
      for (int col = 1; col <= colCount; col++) {
         Object object = rs.getObject(col);
         result.add(object);
         System.out.print("[" + object + "]");
      }
      System.out.println("]");

      return result;
   }

}
