package com.jonas.agile.devleadtool;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class MyStatusBar extends JPanel {

   private static MyStatusBar instance;

   private Logger log = MyLogger.getLogger(MyStatusBar.class);

   private MessageClearer messageClearer;
   private JLabel statusMessage;
   
   private MyStatusBar() {
      super(new GridLayout(0, 1, 0, 0));
      statusMessage = new JLabel(" ");
      Font font = statusMessage.getFont();
      Font newFont = new Font(font.getName(), font.getStyle(), font.getSize() - 1);
      statusMessage.setFont(newFont);
      messageClearer = new MessageClearer(2000, statusMessage);
      this.add(statusMessage);
   }

   public static MyStatusBar getInstance() {
      if (instance == null) {
         synchronized (MyStatusBar.class) {
            if (instance == null) {
               instance = new MyStatusBar();
            }
         }
      }
      return instance;
   }

   public void setMessage(final String message) {
      statusMessage.setText(message);
      messageClearer.clearMessageAfterDelay();
   }
}

class MessageClearer implements ActionListener {
   
   private Logger log = MyLogger.getLogger(MessageClearer.class);
   
   private final int delay;
   private volatile int queue = 0;
   private JLabel statusMessage;
   
   public MessageClearer(int delay, JLabel statusMessage) {
      this.delay = delay;
      this.statusMessage = statusMessage;
      if (delay < 1)
         throw new RuntimeException("Delay has to be higher than zero!");
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      queue--;
      if (queue == 0)
         statusMessage.setText(" ");
      log.debug("clearing message! Queue is now " + queue);
   }
   
   public void clearMessageAfterDelay() {
      queue++;
      log.debug("setting message to! Queue is now " + queue);
      Timer timer = new Timer(delay, this);
      timer.setRepeats(false);
      timer.start();
   }
}