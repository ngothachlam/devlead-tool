package com.jonas.agile.devleadtool.properties;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.common.swing.SwingUtil;

public class SprinterPropertieSetter {

   private JFrame parentFrame;
   private JFrame frame;

   public SprinterPropertieSetter() {
   }

   public void queryAndSetDefaultProperties(SprinterPropertiesManager sprinterPropertiesManager, Set<Property> defaultProperties) {
      frame = new JFrame();
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      frame.setContentPane(panel);

      SwingUtil.defaultGridBagConstraints(gbc);

      Map<Property, Getter> state = new HashMap<Property, Getter>();

      for (Property property : defaultProperties) {
         gbc.gridx = 0;
         panel.add(new JLabel(property.getDescription()), gbc);
         gbc.gridx++;
         switch (property.getType()) {
         case DIRECTORY:
            JTextFieldGetter browsePath = new JTextFieldGetter(30);
            browsePath.setEditable(false);
            panel.add(browsePath, gbc);

            BrowseAction browseAction = new BrowseAction("Browse", "Browse to set the file for "
                  + property.getDescription(), browsePath, frame, property.getType());
            JButton browseButton = new JButton(browseAction);
            gbc.gridx++;
            panel.add(browseButton, gbc);

            state.put(property, browsePath);

            break;
         }
         gbc.gridy++;
      }

      gbc.gridx = 0;
      gbc.gridwidth = 3;
      gbc.anchor = GridBagConstraints.CENTER;

      JButton saveButton = new JButton(new SaveAction("Save", "Save Properties", frame, state, defaultProperties, sprinterPropertiesManager));
      panel.add(saveButton, gbc);

      frame.setResizable(false);
      frame.pack();
      SwingUtil.centreWindowWithinWindow(frame, parentFrame);
      frame.setVisible(true);
   }

   public void setFrameForDefaultPropertiesQuery(JFrame frame) {
      this.parentFrame = frame;
   }
}


class JTextFieldGetter extends JTextField implements Getter {

   public JTextFieldGetter(int i) {
      super(i);
   }

   @Override
   public String getValue() {
      return this.getText();
   }

}


interface Getter {
   public String getValue();
}


class SaveAction extends BasicAbstractGUIAction {

   private final Map<Property, Getter> state;
   private final Set<Property> defaultProperties;
   private final SprinterPropertiesManager sprinterPropertiesManager;

   public SaveAction(String name, String description, Frame parentFrame, Map<Property, Getter> state, Set<Property> defaultProperties,
         SprinterPropertiesManager sprinterPropertiesManager) {
      super(name, description, parentFrame);
      this.state = state;
      this.defaultProperties = defaultProperties;
      this.sprinterPropertiesManager = sprinterPropertiesManager;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      for (Property property : defaultProperties) {
         Getter getter = state.get(property);
         sprinterPropertiesManager.setProperty(property, getter.getValue());
      }
      if (defaultProperties.size() > 0) {
         try {
            System.out.println("saving...");
            sprinterPropertiesManager.saveProperties();
         } catch (IOException e1) {
            throw new RuntimeException(e1);
         }
      }
   }

}


class BrowseAction extends BasicAbstractGUIAction {
   private JFileChooser fc;
   private final JTextField browsePath;

   public BrowseAction(String name, String description, JTextField browsePath, Frame parentFrame, final PropertyType propertyType) {
      super(name, description, parentFrame);
      this.browsePath = browsePath;
      Runnable runnable = new Runnable() {

         public void run() {
            synchronized (this) {
               fc = new JFileChooser();
               switch (propertyType) {
               case DIRECTORY:
                  fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                  break;
               }
            }
         }

      };
      Thread thread = new Thread(runnable);
      thread.start();
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      if (fc == null) {
         synchronized (this) {
         }
      }
      int result = fc.showOpenDialog(getParentFrame());
      if (result == JFileChooser.APPROVE_OPTION) {
         browsePath.setText(fc.getSelectedFile().getAbsolutePath());
      }
   }
}