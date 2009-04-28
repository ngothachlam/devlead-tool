package com.jonas.agile.devleadtool.properties;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;

public class SprinterPropertiesManager {

   private final SprinterProperties properties;
   private final SprinterPropertieSetter sprinterPropertySetter;

   public SprinterPropertiesManager(SprinterProperties properties, SprinterPropertieSetter sprinterPropertySetter) {
      this.properties = properties;
      this.sprinterPropertySetter = sprinterPropertySetter;
   }

   public void loadProperties(PlannerHelper helper, ExcelSprintDao sprintDao) throws IOException {
      File file = properties.getFile();
      if (!file.exists()) {
         file.createNewFile();
         properties.createNewPropertiesCache();
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
      Set<Property> props = Property.SAVE_DIRECTORY.getProperties();
      Set<Property> emptyDefaultProperties = new HashSet<Property>();

      for (Property property : props) {
         String value = properties.getProperty(property);
         if (value == null) {
            if (property.isDefault()) {
               emptyDefaultProperties.add(property);
            }
         } else {
            Property.initialise();
         }
      }
      if (emptyDefaultProperties.size() > 0) {
         sprinterPropertySetter.queryAndSetMissingProperties(this, emptyDefaultProperties);
      }
   }

   private void setDefaultProperties() {
      Set<Property> defaultMissingProperties = Property.SAVE_DIRECTORY.getDefaultProperties();
      sprinterPropertySetter.queryAndSetMissingProperties(this, defaultMissingProperties);
   }

   public void setProperty(Property property, String temporaryValue) {
      properties.setProperty(property, temporaryValue);
   }

}
