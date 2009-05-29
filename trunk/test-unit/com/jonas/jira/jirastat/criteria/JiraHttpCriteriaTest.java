package com.jonas.jira.jirastat.criteria;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jonas.jira.jirastat.criteria.JiraHttpCriteria;

public class JiraHttpCriteriaTest {

   private JiraHttpCriteria criteria;
   
   @Before
   public void setUp() throws Exception {
      criteria = new JiraHttpCriteria();
   }

   @Test
   public final void testSaveAndReset() {
      assertEquals("", criteria.toString());

      criteria.save();
      assertEquals("", criteria.toString());
      
      criteria.reset(true);
      assertEquals("", criteria.toString());
      
      criteria.reset(false);
      assertEquals("", criteria.toString());
      
      criteria.append("blah");
      criteria.save();
      criteria.append("bluh");
      criteria.save();
      criteria.append("bleh");
      assertEquals("blahbluhbleh", criteria.toString());
      
      criteria.reset(false);
      assertEquals("blahbluh", criteria.toString());
      
      criteria.reset(false);
      assertEquals("blahbluh", criteria.toString());
      
      criteria.reset(true);
      assertEquals("blahbluh", criteria.toString());
      
      criteria.reset(true);
      assertEquals("blah", criteria.toString());
      
      criteria.reset(true);
      assertEquals("", criteria.toString());
      
      criteria.append("blih");
      criteria.save();
      assertEquals("blih", criteria.toString());
      
      criteria.append("bloh");
      criteria.save();
      assertEquals("blihbloh", criteria.toString());
      
      criteria.reset(true);
      assertEquals("blihbloh", criteria.toString());
      
      criteria.reset(true);
      assertEquals("blih", criteria.toString());
   }
   
   @Test 
   public void testShouldAppendBaseUrlOk(){
      assertEquals("", criteria.toString());

      criteria.setHostUrl("a");
      assertEquals("a", criteria.toString());
      
      criteria.setHostUrl("b");
      assertEquals("b", criteria.toString());
      
      criteria.setBaseUrl("c");
      assertEquals("bc", criteria.toString());
   }
   
   @Test 
   public void testShoulAppendSubBaseUrlOk(){
      assertEquals("", criteria.toString());
      
      criteria.setBaseUrl("a");
      assertEquals("a", criteria.toString());
      
      criteria.setBaseUrl("b");
      assertEquals("b", criteria.toString());
      
      criteria.setHostUrl("c");
      assertEquals("cb", criteria.toString());
   }

}
