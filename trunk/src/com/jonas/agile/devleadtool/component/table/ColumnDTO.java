package com.jonas.agile.devleadtool.component.table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class ColumnDTO<T> {

   private static final Logger log = MyLogger.getLogger(ColumnDTO.class);
   private static final Map<Column, ColumnDTO> map = new HashMap<Column, ColumnDTO>();
   
   public static final ColumnDTO<String> URL = new ColumnDTO<String>(Column.URL, String.class, "");
   public static final ColumnDTO<String> Jira = new ColumnDTO<String>(Column.Jira, String.class, ""); 
   public static final ColumnDTO<String> Description= new ColumnDTO<String>(Column.Description, String.class, "");
   public static final ColumnDTO<String> FixVersion= new ColumnDTO<String>(Column.FixVersion, String.class, "");
   public static final ColumnDTO<String> Status= new ColumnDTO<String>(Column.JiraStatus, String.class, "");
   public static final ColumnDTO<String> Resolution= new ColumnDTO<String>(Column.Resolution, String.class, "");
   public static final ColumnDTO<String> BuildNo= new ColumnDTO<String>(Column.BuildNo, String.class, "");
   public static final ColumnDTO<String> Type= new ColumnDTO<String>(Column.Type, String.class, "");
   public static final ColumnDTO<String> Note= new ColumnDTO<String>(Column.Note, String.class, "");
   public static final ColumnDTO<String> Planned_Sprint= new ColumnDTO<String>(Column.Planned_Sprint, String.class, "");
   public static final ColumnDTO<String> Resolved_Sprint= new ColumnDTO<String>(Column.Resolved_Sprint, String.class, "");
   public static final ColumnDTO<String> Closed_Sprint= new ColumnDTO<String>(Column.Closed_Sprint, String.class, "");
   public static final ColumnDTO<String> Remaining_Progress= new ColumnDTO<String>(Column.isInProgress, String.class, "");
   
   public static final ColumnDTO<Float> Dev_Estimate= new ColumnDTO<Float>(Column.Dev_Estimate, Float.class, null);
   public static final ColumnDTO<Float> QA_Estimate= new ColumnDTO<Float>(Column.QA_Estimate, Float.class, null);
   public static final ColumnDTO<Float> Dev_Actual= new ColumnDTO<Float>(Column.Dev_Actual, Float.class, null);
   
   
   public static final ColumnDTO<Boolean> isOpen = new ColumnDTO<Boolean>(Column.isOpen, Boolean.class, Boolean.FALSE);
   public static final ColumnDTO<Boolean> isBug = new ColumnDTO<Boolean>(Column.isBug, Boolean.class, Boolean.FALSE);
   public static final ColumnDTO<Boolean> isResolved = new ColumnDTO<Boolean>(Column.isResolved, Boolean.class, Boolean.FALSE);
   public static final ColumnDTO<Boolean> isComplete = new ColumnDTO<Boolean>(Column.isComplete, Boolean.class, Boolean.FALSE);

   // Other Defaults
   public static final ColumnDTO<ColumnValue> isInPlan = new ColumnDTO<ColumnValue>(Column.isInPlan, ColumnValue.class, ColumnValue.NA);
   
   
   private final Column column;
   private final T defaultValue;
   private final Class<T> aClass;

   public ColumnDTO(Column column, Class<T> aClass, T defaultValue){
      this.column = column;
      this.aClass =  aClass;
      this.defaultValue = defaultValue;
      map.put(column, this);
   }
   
   public ColumnDTO(Column column, T defaultValue){
      this.column = column;
      this.aClass = (Class<T>) defaultValue.getClass();
      this.defaultValue = defaultValue;
      map.put(column, this);
   }
   
   public T getDefaultValue(){
      return defaultValue;
   }
   
   public Class<T> getDefaultClass(){
      return aClass;
   }

   public static ColumnDTO getColumnDTO(Column column) {
      ColumnDTO columnDTO = map.get(column);
      if(columnDTO == null){
         log.error("Does not contain " + column);
         System.err.println("Does not contain " + column);
      }
      return columnDTO;
   }
   
}
