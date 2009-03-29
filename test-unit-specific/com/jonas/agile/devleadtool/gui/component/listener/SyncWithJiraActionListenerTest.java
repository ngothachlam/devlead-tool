package com.jonas.agile.devleadtool.gui.component.listener;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import org.easymock.EasyMock;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.gui.listener.SyncWithJiraListener;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class SyncWithJiraActionListenerTest extends JonasTestCase {

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }
   
   public void testShouldSyncCorrectColumns(){
      MyTable mock_table = createClassMock(MyTable.class);
      PlannerHelper mock_helper = createClassMock(PlannerHelper.class);
      ActionEvent mock_actionEvent =  createClassMock(ActionEvent.class);
      SyncWithJiraActionListenerListener mock_listener =  createClassMock(SyncWithJiraActionListenerListener.class);
      SyncWithJiraListener listener = new SyncWithJiraListener(mock_table, mock_helper);
    
      listener.addListener(mock_listener);
      
      int[] selectedColums = new int[]{0,1};
      JFrame frame = new JFrame();
      EasyMock.expect(mock_table.getSelectedColumns()).andReturn(selectedColums).anyTimes();
      EasyMock.expect(mock_helper.getParentFrame()).andReturn(frame).anyTimes();
//      mock_listener.jiraSynced("jira-1", 0);
//      mock_listener.jiraSyncedCompleted();
      
      replay();
      
      listener.actionPerformed(mock_actionEvent);
      
      verify();
   }

}
