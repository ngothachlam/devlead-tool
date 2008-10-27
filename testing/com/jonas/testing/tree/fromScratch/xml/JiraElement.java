package com.jonas.testing.tree.fromScratch.xml;

public enum JiraElement {
   fixVersion, key, Sprint, item, status, resolution, description;

   public static JiraElement get(String name) {
      for (JiraElement element : JiraElement.values()) {
         if (element.toString().equals(name))
            return element;
      }
      return null;
   }

}
