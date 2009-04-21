package com.jonas.agile.devleadtool.properties;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Property {

   public final static Property SAVE_DIRECTORY = new Property("save.directory", PropertyType.DIRECTORY, "Save Directory").setAsDefault();

   private final String keyName;
   private boolean isDefault;
   private final PropertyType propertyType;
   private Set<Property> values = new HashSet<Property>();
   private String description;

   private Property(String keyName, PropertyType propertyType, String description) {
      this.keyName = keyName;
      this.propertyType = propertyType;
      this.description = description;
      values.add(this);
   }

   private Property setAsDefault() {
      isDefault = true;
      return this;
   }

   public PropertyType getType() {
      return propertyType;
   }

   public Set<Property> getDefaultProperties() {
      Set<Property> returnVal = new HashSet<Property>();
      for (Property property : values) {
         if (property.isDefault) {
            returnVal.add(property);
         }
      }
      return returnVal;
   }

   public String getKeyName() {
      return keyName;
   }

   public String getDescription() {
      return description;
   }

   public Object getObjectFromStringValue(String propertyObject) {
      switch (getType()) {
      case DIRECTORY:
         return new File(propertyObject);
      }
      return propertyObject;
   }

}
