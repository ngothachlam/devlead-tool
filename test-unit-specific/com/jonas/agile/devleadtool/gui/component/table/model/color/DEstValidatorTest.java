package com.jonas.agile.devleadtool.gui.component.table.model.color;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;

public class DEstValidatorTest {

   Validator validator = new DEstValidator();

   @Before
   public void setUp() throws Exception {
   }

   @Test
   public void testValidate() {
      assertException("If issueType and boardStatus is null - cannot calculate validity!", null, null, null);
      assertException("If issueType is null - cannot calculate validity!", null, null, BoardStatusValue.Failed);
      assertException("If issueType is null - cannot calculate validity!", null, null, BoardStatusValue.Open);
      assertException("If issueType is null - cannot calculate validity!", "", null, BoardStatusValue.Failed);
      assertException("If issueType is null - cannot calculate validity!", "", null, BoardStatusValue.Open);
      assertException("If boardStatus is null - cannot calculate validity!", null, IssueType.BUG, null);
      assertException("If boardStatus is null - cannot calculate validity!", null, IssueType.DEV, null);
      assertException("If boardStatus is null - cannot calculate validity!", "", IssueType.BUG, null);
      assertException("If boardStatus is null - cannot calculate validity!", "", IssueType.DEV, null);

      assertEquals("If issueType is bug - should not have a dEst value!", ValidatorResponse.PASS, validator.validate(null, IssueType.BUG, null));
      assertEquals("If issueType is bug - should not have a dEst value!", ValidatorResponse.PASS, validator.validate("", IssueType.BUG, null));
      assertEquals("If issueType is bug - should not have a dEst value!", ValidatorResponse.FAIL, validator.validate("blah", IssueType.BUG, null));
      assertEquals("If issueType is bug - should not have a dEst value!", ValidatorResponse.FAIL, validator.validate("1.0", IssueType.BUG, null));

      assertEquals(ValidatorResponse.PASS, validator.validate("", null, BoardStatusValue.Approved));
      assertEquals(ValidatorResponse.PASS, validator.validate("", null, BoardStatusValue.Open));

   }

   private void assertException(String string, Object status, IssueType type, Object value) {
      try {
         assertEquals(ValidatorResponse.PASS, validator.validate(value, type, status));
         assertTrue(string, false);
      } catch (NullPointerException e) {
      }
   }

}
