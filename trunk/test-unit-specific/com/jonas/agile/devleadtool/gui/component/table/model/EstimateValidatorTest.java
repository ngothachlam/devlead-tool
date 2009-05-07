package com.jonas.agile.devleadtool.gui.component.table.model;

import org.junit.Before;
import org.junit.Test;


public class EstimateValidatorTest {

   EstimateValidator validator;
   
   @Before
   public void setUp() throws Exception {
      validator = new EstimateValidator(new BoardCellColorHelper());
   }
   
   @Test
   public void shouldCalculateDEstOk(){
      validator.validateDEst(null, 0, new ValueGetterTestImpl(null, null));
   }

   private class ValueGetterTestImpl implements ValueGetter{

      public ValueGetterTestImpl(Object type, Object boardStatus) {
         super();
         this.type = type;
         this.boardStatus = boardStatus;
      }

      private Object type;
      private Object boardStatus;

      @Override
      public Object getBoardStatus(int row) {
         return boardStatus;
      }

      @Override
      public Object getType(int row) {
         return type;
      }
      
   }
   
}

