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

   public DBDao() {
   }

   @Override
   public ContentsDto loadContents() throws IOException {
      try {
         Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
         String url = "jdbc:sybase:Tds:10.155.57.51:3106";
         String user = "llusa";
         String password = "password";
         Connection con = DriverManager.getConnection(url, user, password);

         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM copy_inventory..mlc_response_audit ORDER BY id DESC");
         ResultSetMetaData md = rs.getMetaData();
         md.getColumnCount();

         int rowCounter = 0;
         Vector<Object> header = null;
         Vector<Vector<Object>> body = new Vector<Vector<Object>>();

         while (rs.next()) {
            Vector<Object> row = createRow(rs, md.getColumnCount());
            if (rowCounter++ == 0) {
               header = row;
            } else {
               body.add(row);
            }
         }
         return new ContentsDto(header, body);
      } catch (SQLException e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   public Vector<Object> createRow(ResultSet rs, int colCount) throws SQLException {
      Vector<Object> result = new Vector(colCount);

      for (int col = 1; col <= colCount; col++) {
         Object object = rs.getObject(col);
         result.add(object);
         System.out.println("Read in " + object + " from database");
      }

      return result;
   }

}
