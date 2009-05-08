package com.jonas.agile.devleadtool.gui.component.table.model;

import java.util.HashSet;
import java.util.Set;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;

public class BoardCellColorHelper {

   private static BoardCellColorHelper instance = new BoardCellColorHelper();
   private Set<BoardStatusValue> requiredDevEstimates = new HashSet<BoardStatusValue>();
   private Set<BoardStatusValue> requiredDevRemains = new HashSet<BoardStatusValue>();
   private Set<BoardStatusValue> requiredDevActuals = new HashSet<BoardStatusValue>();
   private Set<BoardStatusValue> requiredBlankDevActuals = new HashSet<BoardStatusValue>();
   private Set<BoardStatusValue> requiredQARemains = new HashSet<BoardStatusValue>();

   public BoardCellColorHelper() {
      requiredDevEstimates.add(BoardStatusValue.Approved);
      requiredDevEstimates.add(BoardStatusValue.Bug);
      requiredDevEstimates.add(BoardStatusValue.Complete);
      requiredDevEstimates.add(BoardStatusValue.ForShowCase);
      requiredDevEstimates.add(BoardStatusValue.InProgress);
      requiredDevEstimates.add(BoardStatusValue.Open);
      requiredDevEstimates.add(BoardStatusValue.Resolved);

      requiredDevRemains.add(BoardStatusValue.InProgress);
      
      requiredQARemains.add(BoardStatusValue.InProgress);
      requiredQARemains.add(BoardStatusValue.Resolved);

      requiredDevActuals.add(BoardStatusValue.Approved);
      requiredDevActuals.add(BoardStatusValue.Complete);
      requiredDevActuals.add(BoardStatusValue.ForShowCase);
      requiredDevActuals.add(BoardStatusValue.Resolved);

      requiredBlankDevActuals.add(BoardStatusValue.Bug);
      requiredBlankDevActuals.add(BoardStatusValue.InProgress);
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

   public Set<BoardStatusValue> getRequiredQARemains() {
      return requiredQARemains;
   }
}