package com.jonas.common;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class HyperLinker {
   private static final Logger log = MyLogger.getLogger(HyperLinker.class);

   public static boolean displayURL(String url) throws URISyntaxException, IOException {
      if (Desktop.isDesktopSupported()) {
         Desktop desktop = Desktop.getDesktop();
         if (desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri = new URI(url);
            desktop.browse(uri);
            return true;
         }
      }
      return false;
   }
}
