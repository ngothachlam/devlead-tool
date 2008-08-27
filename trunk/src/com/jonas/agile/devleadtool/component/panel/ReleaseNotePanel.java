package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.tree.model.ReleaseTreeModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.testHelpers.TryoutTester;

public class ReleaseNotePanel extends MyComponentPanel {

	private final PlannerHelper helper;
	private Logger log = MyLogger.getLogger(ReleaseNotePanel.class);

	public ReleaseNotePanel(PlannerHelper client) {
		this(client, new ReleaseTreeModel(null));
	}

	public ReleaseNotePanel(PlannerHelper helper, ReleaseTreeModel planModel) {
		super(new BorderLayout());
		this.helper = helper;
		ReleaseTreeModel model = planModel;

		JTree tree = new JTree(model);
		JScrollPane scrollpane = new JScrollPane(tree);

		this.addCenter(scrollpane);
		this.addSouth(getBottomPanel());
	}

	public static void main(String[] args) {
		JFrame frame = TryoutTester.getFrame();
		JPanel panel = new ReleaseNotePanel(new PlannerHelper(frame, "test"));
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	private Component getBottomPanel() {
		JPanel buttons = new JPanel();
		JButton refreshFixVersions = new JButton("");

		buttons.add(refreshFixVersions);
		return buttons;
	}

}