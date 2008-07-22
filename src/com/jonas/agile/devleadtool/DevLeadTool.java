package com.jonas.agile.devleadtool;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.dialog.ClosingDialog;
import com.jonas.agile.devleadtool.component.dialog.PlannerDialog;
import com.jonas.agile.devleadtool.component.listener.MainFrameListener;
import com.jonas.agile.devleadtool.component.panel.LoadDialog;
import com.jonas.agile.devleadtool.component.panel.SaveDialog;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.common.SwingUtil;

public class DevLeadTool {
	
	DevLeadTool() {
	}

	public void start() {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
				makeUI();
		// }
		// });
	}

	private void makeUI() {
		JFrame frame = new JFrame("Jonas' Agile Dev Lead Tool");

		DesktopPane desktop = new DesktopPane();
		JPanel contentPane = createPanel(desktop);

		frame.setJMenuBar(createMenuBar(frame, desktop));
		frame.setContentPane(contentPane);

		frame.setSize(new Dimension(1200, 360));
		frame.setVisible(true);
		frame.addWindowListener(new MainFrameListener(frame));

	}

	private JPanel createPanel(DesktopPane desktop) {
		JPanel contentPane = SwingUtil.getBorderPanel();
		contentPane.add(desktop);
		return contentPane;
	}

	private JMenuItem[] getFileMenuItemArray(final JFrame frame, final DesktopPane desktop) {
		final PlannerHelper plannerHelper = new PlannerHelper("Planner");
		final PlannerDAO plannerDAO = new PlannerDAOExcelImpl();
		
		JMenuItem planner = createMenuItem("New Planner", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlannerDialog plannerDialog = new PlannerDialog(frame, desktop, plannerHelper);
			}
		});
		JMenuItem open = createMenuItem("Open Planner", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoadDialog(plannerDAO, frame, plannerHelper);
			}
		});
		JMenuItem save = createMenuItem("Save Planner", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SaveDialog(plannerDAO, frame, plannerHelper);
			}
		});
		JMenuItem exit = createMenuItem("Exit", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ClosingDialog(frame);
			}
		});
		return new JMenuItem[] { planner, open, save, exit };
	}

	private JMenu createFileMenu(String title, JMenuItem[] menuItemList) {
		JMenu fileMenu = new JMenu(title);
		for (int i = 0; i < menuItemList.length; i++) {
			fileMenu.add(menuItemList[i]);
		}
		return fileMenu;
	}

	private JMenuItem createMenuItem(String menuTitle, ActionListener actionListener) {
		JMenuItem menuItem = new JMenuItem(menuTitle);
		menuItem.addActionListener(actionListener);
		return menuItem;
	}

	private JMenuBar createMenuBar(final JFrame frame, DesktopPane desktop) {
		JMenu fileMenuFile = createFileMenu("File", getFileMenuItemArray(frame, desktop));
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenuFile);
		return menuBar;
	}

}
