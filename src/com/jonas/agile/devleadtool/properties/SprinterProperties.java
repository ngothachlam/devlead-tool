package com.jonas.agile.devleadtool.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SprinterProperties {

   private Properties properties;
   private File file;

   public SprinterProperties(File file) {
      this.file = file;
   }

   public void saveProperties() throws IOException {
      FileOutputStream outStream = null;
      try {
         outStream = new FileOutputStream(file);
         properties.store(outStream, null);
      } finally {
         if (outStream != null) {
            outStream.close();
         }
      }
   }

   public void loadProperties() throws IOException {
      initiateNewProperties();
      FileInputStream inStream = null;
      try {
         inStream = new FileInputStream(file);
         properties.load(inStream);
      } finally {
         if (inStream != null)
            inStream.close();
      }
   }

   public void initiateNewProperties() {
      properties = new Properties();
   }

   public void setProperty(Property key, Object value) {
      properties.put(key.getKeyName(), value);
   }

   public File getFile() {
      return file;
   }

   public String getProperty(Property key) {
      return (String) properties.get(key.getKeyName());
   }

   public Object getPropertyObject(Property key) {
      String object = (String) properties.get(key.getKeyName());
      if (object == null)
         return null;
      return key.getObjectFromStringValue(object);
   }

}
