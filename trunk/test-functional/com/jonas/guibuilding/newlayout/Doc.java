/**
 * 
 */
package com.jonas.guibuilding.newlayout;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.DropMode;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

class Doc extends InternalFrameAdapter implements ActionListener {
   /**
    * 
    */
   private final TopLevelTransferHandlerDemo topLevelTransferHandlerDemo;
   String name;
   JInternalFrame frame;
   TransferHandler th;
   JTextArea area;

   public Doc(TopLevelTransferHandlerDemo topLevelTransferHandlerDemo, File file) {
      this.topLevelTransferHandlerDemo = topLevelTransferHandlerDemo;
      this.name = file.getName();
      try {
         init(file.toURI().toURL());
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
   }

   public Doc(TopLevelTransferHandlerDemo topLevelTransferHandlerDemo, String name) {
      this.topLevelTransferHandlerDemo = topLevelTransferHandlerDemo;
      this.name = name;
      init(getClass().getResource(name));
   }

   private void init(URL url) {
      frame = new JInternalFrame(name);
      frame.addInternalFrameListener(this);
      this.topLevelTransferHandlerDemo.listModel.add(this.topLevelTransferHandlerDemo.listModel.size(), this);

      area = new JTextArea();
      area.setMargin(new Insets(5, 5, 5, 5));

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
         String in;
         while ((in = reader.readLine()) != null) {
            area.append(in);
            area.append("\n");
         }
         reader.close();
      } catch (Exception e) {
         e.printStackTrace();
         return;
      }

      th = area.getTransferHandler();
      area.setFont(new Font("monospaced", Font.PLAIN, 12));
      area.setCaretPosition(0);
      area.setDragEnabled(true);
      area.setDropMode(DropMode.INSERT);
      frame.getContentPane().add(new JScrollPane(area));
      this.topLevelTransferHandlerDemo.dp.add(frame);
      frame.show();
      frame.setSize(400, 300);
      frame.setResizable(true);
      frame.setClosable(true);
      frame.setIconifiable(true);
      frame.setMaximizable(true);
      frame.setLocation(TopLevelTransferHandlerDemo.left, TopLevelTransferHandlerDemo.top);
      TopLevelTransferHandlerDemo.incr();
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            select();
         }
      });
      this.topLevelTransferHandlerDemo.checkBox_removeTHFromListAndText.addActionListener(this);
      setNullTH();
   }

   public void internalFrameClosing(InternalFrameEvent event) {
      this.topLevelTransferHandlerDemo.listModel.removeElement(this);
      this.topLevelTransferHandlerDemo.checkBox_removeTHFromListAndText.removeActionListener(this);
   }

   public void internalFrameOpened(InternalFrameEvent event) {
      int index = this.topLevelTransferHandlerDemo.listModel.indexOf(this);
      this.topLevelTransferHandlerDemo.list.getSelectionModel().setSelectionInterval(index, index);
   }

   public void internalFrameActivated(InternalFrameEvent event) {
      int index = this.topLevelTransferHandlerDemo.listModel.indexOf(this);
      this.topLevelTransferHandlerDemo.list.getSelectionModel().setSelectionInterval(index, index);
   }

   public String toString() {
      return name;
   }

   public void select() {
      try {
         frame.toFront();
         frame.setSelected(true);
      } catch (java.beans.PropertyVetoException e) {
      }
   }

   public void actionPerformed(ActionEvent ae) {
      setNullTH();
   }

   public void setNullTH() {
      if (this.topLevelTransferHandlerDemo.checkBox_removeTHFromListAndText.isSelected()) {
         area.setTransferHandler(null);
      } else {
         area.setTransferHandler(th);
      }
   }
}