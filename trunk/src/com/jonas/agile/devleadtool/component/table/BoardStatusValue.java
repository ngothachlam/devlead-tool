package com.jonas.agile.devleadtool.component.table;

public enum BoardStatusValue {
	UnKnown, Empty {
		@Override
		public String toString() {
			return "";
		}
	},
	Open, InProgress, Bugs, Resolved, Complete;

}
