package com.jonas.agile.devleadtool.gui.jigloo;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please
 * visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS
 * MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class NewJInternalFrame extends javax.swing.JInternalFrame {

   {
      // Set Look & Feel
      try {
         javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private JPanel planPanel;
   private JTable jiraTable;
   private JTable boardTable;
   private JTable planTable;
   private JCheckBox jCheckBox1;
   private JCheckBox jCheckBox2;
   private JTable reconciliationTable;
   private JButton highlightButton;
   private JTextArea jirasTextArea;
   private JLabel jLabel2;
   private JLabel jLabel1;
   private JButton copyButton;
   private JTextField jiraPrefixTextField;
   private JButton addButton;
   private JCheckBox jCheckBox3;
   private JPanel Actions;
   private JButton openButton;
   private JButton syncButton;
   private JPanel leftButtonPanel;
   private JPanel reconciliationPanel;
   private JPanel jiraPanel;
   private JPanel boardPanel;
   private JTabbedPane jTabbedPane1Left;
   private JTabbedPane jTabbedPaneRight;
   private JSplitPane jSplitPane1;

   /**
    * Auto-generated main method to display this JInternalFrame inside a new JFrame.
    */
   public static void main(String[] args) {
      JFrame frame = new JFrame();
      NewJInternalFrame inst = new NewJInternalFrame();
      JDesktopPane jdp = new JDesktopPane();
      jdp.add(inst);
      jdp.setPreferredSize(inst.getPreferredSize());
      frame.setContentPane(jdp);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
   }

   public NewJInternalFrame() {
      super();
      initGUI();
   }

   private void initGUI() {
      try {
         this.setPreferredSize(new java.awt.Dimension(719, 456));
         this.setBounds(0, 0, 719, 456);
         setVisible(true);
         this.setClosable(true);
         this.setIconifiable(true);
         this.setMaximizable(true);
         {
            jSplitPane1 = new JSplitPane();
            getContentPane().add(jSplitPane1, BorderLayout.CENTER);
            jSplitPane1.setPreferredSize(new java.awt.Dimension(453, 353));
            {
               jTabbedPane1Left = new JTabbedPane();
               jTabbedPaneRight = new JTabbedPane();
               JPanel jPanelInnerLeft = new JPanel(new BorderLayout());
               jPanelInnerLeft.add(jTabbedPane1Left, BorderLayout.CENTER);
               jSplitPane1.add(jPanelInnerLeft, JSplitPane.LEFT);
               jTabbedPane1Left.setPreferredSize(new java.awt.Dimension(524, 311));
               {
                  leftButtonPanel = new JPanel();
                  GridBagLayout buttonLayout = new GridBagLayout();
                  jPanelInnerLeft.add(leftButtonPanel, BorderLayout.SOUTH);
                  leftButtonPanel.setPreferredSize(new java.awt.Dimension(524, 28));
                  buttonLayout.rowWeights = new double[] { 0.1, 0.1, 0.1 };
                  buttonLayout.rowHeights = new int[] { 7, 7, 7 };
                  buttonLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0 };
                  buttonLayout.columnWidths = new int[] { 7, 7, 7, 7, 7, 7, 7 };
                  leftButtonPanel.setLayout(buttonLayout);
                  {
                     syncButton = new JButton();
                     GroupLayout AddLayout = new GroupLayout((JComponent) syncButton);
                     leftButtonPanel.add(getSyncButton(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                           new Insets(0, 0, 0, 0), 0, 0));
                     syncButton.setText("Sync");
                     syncButton.setHorizontalAlignment(SwingConstants.LEFT);
                     syncButton.setLayout(AddLayout);
                     AddLayout.setVerticalGroup(AddLayout.createParallelGroup());
                     AddLayout.setHorizontalGroup(AddLayout.createParallelGroup());
                  }
                  {
                     openButton = new JButton();
                     leftButtonPanel.add(getOpenButton(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                           new Insets(0, 0, 0, 0), 0, 0));
                     GroupLayout openButtonLayout = new GroupLayout((JComponent) openButton);
                     openButton.setText("Open");
                     openButton.setLayout(openButtonLayout);
                     openButtonLayout.setVerticalGroup(openButtonLayout.createParallelGroup());
                     openButtonLayout.setHorizontalGroup(openButtonLayout.createParallelGroup());
                  }
               }
               jSplitPane1.add(jTabbedPaneRight, JSplitPane.RIGHT);
               jTabbedPaneRight.setMinimumSize(new java.awt.Dimension(25, 25));
               jTabbedPaneRight.setPreferredSize(new java.awt.Dimension(102, 351));
               {
                  reconciliationPanel = new JPanel();
                  jTabbedPaneRight.addTab("Reconciliation", null, reconciliationPanel, null);
                  {
                     TableModel ReconciliationTableModel = 
                        new DefaultTableModel(
                              new String[][] { { "One", "Two" }, { "Three", "Four" } },
                              new String[] { "Column 1", "Column 2" });
                     reconciliationTable = new JTable();
                     GroupLayout reconciliationTableLayout = new GroupLayout((JComponent)reconciliationTable);
                     reconciliationPanel.add(getReconciliationTable());
                     reconciliationTable.setModel(ReconciliationTableModel);
                     reconciliationTable.setLayout(reconciliationTableLayout);
                     reconciliationTableLayout.setVerticalGroup(reconciliationTableLayout.createSequentialGroup());
                     reconciliationTableLayout.setHorizontalGroup(reconciliationTableLayout.createSequentialGroup());
                  }
               }
               {
                  Actions = new JPanel();
                  jTabbedPaneRight.addTab("Actions", null, Actions, null);
                  GridBagLayout jPanel1Layout = new GridBagLayout();
                  jPanel1Layout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0};
                  jPanel1Layout.rowHeights = new int[] {7, 20, 20, 20, 7, 7, 7, 20, 20, 20};
                  jPanel1Layout.columnWeights = new double[] {0.0, 0.0, 1.0};
                  jPanel1Layout.columnWidths = new int[] {50, 50, 7};
                  Actions.setLayout(jPanel1Layout);
                  {
                     jCheckBox1 = new JCheckBox();
                     Actions.add(jCheckBox1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                     jCheckBox1.setText("jCheckBox1");
                  }
                  {
                     jCheckBox2 = new JCheckBox();
                     Actions.add(jCheckBox2, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                     jCheckBox2.setText("jCheckBox2");
                  }
                  {
                     jCheckBox3 = new JCheckBox();
                     Actions.add(jCheckBox3, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                     jCheckBox3.setText("jCheckBox3");
                  }
                  {
                     jiraPrefixTextField = new JTextField();
                     Actions.add(jiraPrefixTextField, new GridBagConstraints(0, 6, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));
                     jiraPrefixTextField.setText("LLU");
                     jiraPrefixTextField.setPreferredSize(new java.awt.Dimension(90, 19));
                     jiraPrefixTextField.setAlignmentY(0.0f);
                  }
                  {
                     addButton = new JButton();
                     Actions.add(addButton, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 2, 1), 0, 0));
                     BorderLayout addButtonLayout = new BorderLayout();
                     addButton.setText("Add");
                  }
                  {
                     copyButton = new JButton();
                     Actions.add(copyButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));
                     copyButton.setText("Copy");
                  }
                  {
                     jLabel1 = new JLabel();
                     Actions.add(jLabel1, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));
                     jLabel1.setText("Jira Prefix:");
                  }
                  {
                     jLabel2 = new JLabel();
                     Actions.add(jLabel2, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));
                     jLabel2.setText("Jira Numbers:");
                  }
                  {
                     jirasTextArea = new JTextArea();
                     Actions.add(jirasTextArea, new GridBagConstraints(0, 8, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 3, 0, 0), 0, 0));
                  }
                  {
                     highlightButton = new JButton();
                     Actions.add(highlightButton, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 2, 1), 0, 0));
                     highlightButton.setText("Highlight");
                  }
               }
               
               {
                  boardPanel = new JPanel();
                  boardPanel.setLayout(new BorderLayout());
                  jTabbedPane1Left.addTab("Board", null, boardPanel, null);
                  {
                     boardTable = new MyTable("BoardTable", true);
                     TableModel boardTableModel = new BoardTableModel();
                     boardPanel.add(boardTable, BorderLayout.CENTER);
                     boardTable.setModel(boardTableModel);
                  }
               }
               {
                  planPanel = new JPanel();
                  planPanel.setLayout(new BorderLayout());
                  jTabbedPane1Left.addTab("Plan", null, planPanel, null);
                  {
                     planTable = new MyTable("PlanTable", true);
                     TableModel planTableModel = new PlanTableModel();
                     planPanel.add(planTable, BorderLayout.CENTER);
                     planTable.setModel(planTableModel);
                  }
               }
               {
                  jiraPanel = new JPanel();
                  jiraPanel.setLayout(new BorderLayout());
                  jTabbedPane1Left.addTab("Jira", null, jiraPanel, null);
                  {
                     jiraTable = new MyTable("JiraTable", false);
                     TableModel jiraTableModel = new JiraTableModel();
                     jiraPanel.add(jiraTable, BorderLayout.CENTER);
                     jiraTable.setModel(jiraTableModel);
                  }
               }
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public JButton getSyncButton() {
      return syncButton;
   }

   public JButton getOpenButton() {
      return openButton;
   }

   public JTable getBoardTable() {
      return boardTable;
   }

   public JTable getPlanTable() {
      return planTable;
   }

   public JTable getJiraTable() {
      return jiraTable;
   }
   
   public JButton getAddButton() {
      return addButton;
   }
   
   public JTextField getJiraPrefixTextField() {
      return jiraPrefixTextField;
   }
   
   public JButton getJButton3() {
      return copyButton;
   }
   
   public JTextArea getJirasTextArea() {
      return jirasTextArea;
   }
   
   public JButton getHighlightButton() {
      return highlightButton;
   }
   
   public JTable getReconciliationTable() {
      return reconciliationTable;
   }

}
