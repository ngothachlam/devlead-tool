package com.jonas.agile.devleadtool;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jonas.agile.devleadtool.component.DesktopPane;
import com.jonas.agile.devleadtool.component.dialog.ClosingDialog;
import com.jonas.agile.devleadtool.component.dialog.PlannerDialog;
import com.jonas.agile.devleadtool.component.listener.MainFrameListener;
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

//		frame.setSize(new Dimension(1200, 660));
		frame.setSize(new Dimension(1200, 360));
//		SwingUtil.centreWindow(frame);
		frame.setVisible(true);
		frame.addWindowListener(new MainFrameListener(frame));

	}

	private JPanel createPanel(DesktopPane desktop) {
		JPanel contentPane = SwingUtil.getBorderPanel();
		contentPane.add(desktop);
		return contentPane;
	}

	private JMenuItem[] getFileMenuItemArray(final JFrame frame, final DesktopPane desktop) {
		JMenuItem planner = createMenuItem("Planner", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PlannerDialog(frame, desktop);
			}
		});
		JMenuItem exit = createMenuItem("Exit", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ClosingDialog(frame);
			}
		});
		return new JMenuItem[] { planner, exit };
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
