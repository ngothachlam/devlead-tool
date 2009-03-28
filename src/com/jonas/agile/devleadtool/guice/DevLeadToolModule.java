package com.jonas.agile.devleadtool.guice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.name.Names;
import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.gui.component.frame.main.MainFrame;

public class DevLeadToolModule extends AbstractModule {

   @Override
   protected void configure() {
      try {
         loadProperties(super.binder());
         
         bind(MainFrame.class).asEagerSingleton();
         bind(DesktopPane.class).asEagerSingleton();
         
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   private void loadProperties(Binder binder) throws FileNotFoundException {
      File file = new File("properties/app.properties");
      InputStream stream = new FileInputStream(file);
      Properties appProperties = new Properties();
      try {
         appProperties.load(stream);
         Names.bindProperties(binder, appProperties);
      } catch (IOException e) {
         binder.addError(e);
      }
   }

}
