package com.jonas.agile.devleadtool.component.table.model;

import java.util.HashSet;
import java.util.Set;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;

public class BoardCellColorHelper {

   private static BoardCellColorHelper instance = new BoardCellColorHelper();
   private Set<BoardStatusValue> requiredDevEstimates = new HashSet<BoardStatusValue>();
   private Set<BoardStatusValue> requiredDevRemains = new HashSet<BoardStatusValue>();
   private Set<BoardStatusValue> requiredDevActuals = new HashSet<BoardStatusValue>();
   private Set<BoardStatusValue> requiredBlankDevActuals = new HashSet<BoardStatusValue>();

   private BoardCellColorHelper() {
      requiredDevEstimates.add(BoardStatusValue.Approved);
      requiredDevEstimates.add(BoardStatusValue.Bug);
      requiredDevEstimates.add(BoardStatusValue.Complete);
      requiredDevEstimates.add(BoardStatusValue.ForShowCase);
      requiredDevEstimates.add(BoardStatusValue.InDevProgress);
      requiredDevEstimates.add(BoardStatusValue.InQAProgress);
      requiredDevEstimates.add(BoardStatusValue.Open);
//      requiredDevEstimates.add(BoardStatusValue.Parked);
      requiredDevEstimates.add(BoardStatusValue.Resolved);

      requiredDevRemains.add(BoardStatusValue.InDevProgress);

      requiredDevActuals.add(BoardStatusValue.Approved);
      requiredDevActuals.add(BoardStatusValue.Complete);
      requiredDevActuals.add(BoardStatusValue.ForShowCase);
      requiredDevActuals.add(BoardStatusValue.InQAProgress);
      requiredDevActuals.add(BoardStatusValue.Resolved);

      requiredBlankDevActuals.add(BoardStatusValue.Bug);
      requiredBlankDevActuals.add(BoardStatusValue.InDevProgress);
      requiredBlankDevActuals.add(BoardStatusValue.Open);
   }

   public Set<BoardStatusValue> getRequiredDevRemains() {
      return requiredDevRemains;
   }

   public Set<BoardStatusValue> getRequiredQAEstimates() {
      return requiredDevEstimates;
   }

   public static BoardCellColorHelper getInstance() {
      return instance;
   }

   public Set<BoardStatusValue> getRequiredDevActuals() {
      return requiredDevActuals;
   }

   public Set<BoardStatusValue> getRequiredBlankDevActuals() {
      return requiredBlankDevActuals;
   }

   public Set<BoardStatusValue> getRequiredDevEstimates() {
      return requiredDevEstimates;
   }
}