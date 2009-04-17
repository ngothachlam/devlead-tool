package com.jonas.testing.jxtreetable;

import java.io.Serializable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class SerialisableDefaultTreeTableModel extends DefaultTreeTableModel implements Serializable {

   public SerialisableDefaultTreeTableModel(DefaultMutableTreeTableNode root, ColumnMapper boardColumnMapping) {
   }

}
