package com.jonas.agile.devleadtool.gui.component.tree.xml;

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
