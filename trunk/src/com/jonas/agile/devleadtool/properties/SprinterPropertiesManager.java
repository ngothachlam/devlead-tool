package com.jonas.agile.devleadtool.properties;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SprinterPropertiesManager {

   private final SprinterProperties properties;
   private final SprinterPropertieSetter sprinterPropertySetter;

   public SprinterPropertiesManager(SprinterProperties properties, SprinterPropertieSetter sprinterPropertySetter) {
      this.properties = properties;
      this.sprinterPropertySetter = sprinterPropertySetter;
   }

   public void loadProperties() throws IOException {
      File file = properties.getFile();
      if (!file.exists()) {
         file.createNewFile();
         properties.initiateNewProperties();
         setDefaultProperties();
      } else {
         properties.loadProperties();
         checkAndSetMissingDefaultProperties();
      }
   }

   public void saveProperties() throws IOException {
      properties.saveProperties();
   }

   private void checkAndSetMissingDefaultProperties() {
      Set<Property> defaultProperties = Property.SAVE_DIRECTORY.getDefaultProperties();
      Set<Property> emptyDefaultProperties = new HashSet<Property>();

      for (Property property : defaultProperties) {
         String value = properties.getProperty(property);
         if (value == null) {
            emptyDefaultProperties.add(property);
         }
      }
      if (emptyDefaultProperties.size() > 0) {
         sprinterPropertySetter.queryAndSetDefaultProperties(this, emptyDefaultProperties);
      }
   }

   private void setDefaultProperties() {
      Set<Property> defaultProperties = Property.SAVE_DIRECTORY.getDefaultProperties();
      sprinterPropertySetter.queryAndSetDefaultProperties(this, defaultProperties);
   }

   public void setProperty(Property property, String temporaryValue) {
      properties.setProperty(property, temporaryValue);
   }

}
