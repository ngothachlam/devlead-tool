/**
 * 
 */
package com.jonas.agile.devleadtool.junitutils;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

public final class TestIterator implements Iterator {
	private int i = 0;
	private List<Element> list;
	private final int max;

	public TestIterator(List<Element> list, int max) {
		this.list = list;
		this.max = max;
	}

	public boolean hasNext() {
		return i < max;
	}

	public Object next() {
		return list.get(i++);
	}

	public void remove() {
		i++;
	}
}