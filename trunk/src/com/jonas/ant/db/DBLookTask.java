package com.jonas.ant.db;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.derby.tools.dblook;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class DBLookTask extends Task {

   private String url; // -d
   private String dest; // -o
   private String schema; // -z
   private Vector tables; // -t
   private boolean append; // -append
   private boolean verbose; // -verbose
   private boolean noview; // -noview

   public boolean isAppend() {
      return append;
   }

   public void setAppend(boolean append) {
      this.append = append;
   }

   public String getDest() {
      return dest;
   }

   public void setDest(String dest) {
      this.dest = dest;
   }

   public boolean isNoview() {
      return noview;
   }

   public void setNoview(boolean noview) {
      this.noview = noview;
   }

   public String getSchema() {
      return schema;
   }

   public void setSchema(String schema) {
      this.schema = schema;
   }

   public void setTables(String tables) {
      if (this.tables == null)
         this.tables = new Vector();
      StringTokenizer st = new StringTokenizer(tables, " ");
      while (st.hasMoreTokens()) {
         this.tables.add(st.nextToken());
      }
   }

   public String getTables() {
      StringBuffer buf = new StringBuffer();
      if (tables != null) {
         for (int n = 0; n < tables.size(); n++) {
            buf.append(tables.get(n) + " ");
         }
      }
      return buf.toString();
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public boolean isVerbose() {
      return verbose;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public void execute() throws BuildException {
      ArrayList args = new ArrayList();
      args.add("-d");
      args.add(url);

      if (dest != null) {
         args.add("-o");
         args.add(dest);
      }

      if (schema != null) {
         args.add("-z");
         args.add(schema);
      }

      if (tables != null) {
         args.add("-t");
         for (int n = 0; n < tables.size(); n++) {
            args.add(tables.get(n));
         }
      }

      if (append)
         args.add("-append");

      if (verbose)
         args.add("-verbose");

      if (noview)
         args.add("-noview");

      String[] argsarray = new String[args.size()];
      argsarray = (String[]) args.toArray(argsarray);
      try {
         StartDerbyTask.initialiseDriver();
         dblook.main(argsarray);
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
   }
}
