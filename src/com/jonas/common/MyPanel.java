package com.jonas.common;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class MyPanel extends JPanel {

	public MyPanel(LayoutManager layoutManager) {
		super(layoutManager);
	}

	public final void addNorth(Component component) {
		super.add(component, BorderLayout.NORTH);
	}

	public final void addEast(Component component) {
		super.add(component, BorderLayout.EAST);
	}

	public final void addSouth(Component component) {
		super.add(component, BorderLayout.SOUTH);
	}

	public final void addWest(Component component) {
		super.add(component, BorderLayout.WEST);
	}

	public final void addCenter(Component component) {
		super.add(component, BorderLayout.CENTER);
	}

	public final MyPanel bordered() {
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return this;
	}

	public final MyPanel bordered(int top, int left, int bottom, int right) {
		setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		return this;
	}

	public final MyPanel bordered(Border border) {
		setBorder(border);
		return this;
	}
}
