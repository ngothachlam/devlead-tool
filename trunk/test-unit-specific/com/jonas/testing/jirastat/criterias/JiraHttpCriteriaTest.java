package com.jonas.testing.jirastat.criterias;

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
      criteria.append("blah");
      criteria.save();
      criteria.append("bluh");
      assertEquals("blahbluh", criteria.toString());
      criteria.reset();
      assertNotSame("blahbluh", criteria.toString());
      assertEquals("blah", criteria.toString());
      
   }

}
