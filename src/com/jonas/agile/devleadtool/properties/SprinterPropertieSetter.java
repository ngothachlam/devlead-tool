package com.jonas.agile.devleadtool.properties;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

   public void queryAndSetMissingProperties(SprinterPropertiesManager sprintPropMgr, Set<Property> missingProperties) {
      frame = new JFrame();
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      frame.setContentPane(panel);

      SwingUtil.defaultGridBagConstraints(gbc);

      Map<Property, Getter> state = new HashMap<Property, Getter>();

      for (Property prop : missingProperties) {
         gbc.gridx = 0;
         panel.add(new JLabel(prop.getDesc()), gbc);
         gbc.gridx++;
         switch (prop.getType()) {
         case DIRECTORY:
            JTextFieldGetter browsePath = new JTextFieldGetter(30);
            browsePath.setEditable(false);
            panel.add(browsePath, gbc);

            BrowseAction browseAction = new BrowseAction("Browse", "Set the file for " + prop.getDesc(), browsePath, frame, prop.getType());
            JButton browseButton = new JButton(browseAction);
            gbc.gridx++;
            panel.add(browseButton, gbc);

            state.put(prop, browsePath);

            break;
         }
         gbc.gridy++;
      }

      gbc.gridx = 0;
      gbc.gridwidth = 3;
      gbc.anchor = GridBagConstraints.CENTER;

      JButton saveButton = new JButton(new SaveAction("Save", "Save Properties", frame, state, missingProperties, sprintPropMgr));
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
   private final SprinterPropertiesManager sprintPropMgr;

   public SaveAction(String name, String descr, Frame parent, Map<Property, Getter> state, Set<Property> props,
         SprinterPropertiesManager sprintPropMgr) {
      super(name, descr, parent);
      this.state = state;
      this.defaultProperties = props;
      this.sprintPropMgr = sprintPropMgr;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      for (Property property : defaultProperties) {
         Getter getter = state.get(property);
         sprintPropMgr.setProperty(property, getter.getValue());
      }
      if (defaultProperties.size() > 0) {
         try {
            System.out.println("saving...");
            sprintPropMgr.saveProperties();
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