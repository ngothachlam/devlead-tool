package com.jonas.agile.devleadtool.component.table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class ColumnDTO<T> {

   private static final Logger log = MyLogger.getLogger(ColumnDTO.class);
   private static final Map<ColumnDataType, ColumnDTO> map = new HashMap<ColumnDataType, ColumnDTO>();
   
   public static final ColumnDTO<String> URL = new ColumnDTO<String>(ColumnDataType.URL, String.class, "");
   public static final ColumnDTO<String> Jira = new ColumnDTO<String>(ColumnDataType.Jira, String.class, ""); 
   public static final ColumnDTO<String> Description= new ColumnDTO<String>(ColumnDataType.Description, String.class, "");
   public static final ColumnDTO<String> FixVersion= new ColumnDTO<String>(ColumnDataType.J_FixVersion, String.class, "");
   public static final ColumnDTO<String> Status= new ColumnDTO<String>(ColumnDataType.J_Status, String.class, "");
   public static final ColumnDTO<String> Resolution= new ColumnDTO<String>(ColumnDataType.J_Resolution, String.class, "");
   public static final ColumnDTO<String> BuildNo= new ColumnDTO<String>(ColumnDataType.J_BuildNo, String.class, "");
   public static final ColumnDTO<String> Type= new ColumnDTO<String>(ColumnDataType.J_Type, String.class, "");
   public static final ColumnDTO<String> Note= new ColumnDTO<String>(ColumnDataType.Note, String.class, "");
   public static final ColumnDTO<String> Planned_Sprint= new ColumnDTO<String>(ColumnDataType.Planned_Sprint, String.class, "");
   public static final ColumnDTO<String> Resolved_Sprint= new ColumnDTO<String>(ColumnDataType.Resolved_Sprint, String.class, "");
   public static final ColumnDTO<String> Closed_Sprint= new ColumnDTO<String>(ColumnDataType.Closed_Sprint, String.class, "");
   public static final ColumnDTO<String> Remaining_Progress= new ColumnDTO<String>(ColumnDataType.isInProgress, String.class, "");
   
   public static final ColumnDTO<Float> Dev_Estimate= new ColumnDTO<Float>(ColumnDataType.Dev_Estimate, Float.class, null);
   public static final ColumnDTO<Float> QA_Estimate= new ColumnDTO<Float>(ColumnDataType.QA_Estimate, Float.class, null);
   public static final ColumnDTO<Float> Dev_Actual= new ColumnDTO<Float>(ColumnDataType.Dev_Actual, Float.class, null);
   
   
   public static final ColumnDTO<Boolean> isOpen = new ColumnDTO<Boolean>(ColumnDataType.isOpen, Boolean.class, Boolean.FALSE);
   public static final ColumnDTO<Boolean> isBug = new ColumnDTO<Boolean>(ColumnDataType.isBug, Boolean.class, Boolean.FALSE);
   public static final ColumnDTO<Boolean> isResolved = new ColumnDTO<Boolean>(ColumnDataType.isResolved, Boolean.class, Boolean.FALSE);
   public static final ColumnDTO<Boolean> isComplete = new ColumnDTO<Boolean>(ColumnDataType.isComplete, Boolean.class, Boolean.FALSE);

   // Other Defaults
   public static final ColumnDTO<ColumnValue> isInPlan = new ColumnDTO<ColumnValue>(ColumnDataType.isInPlan, ColumnValue.class, ColumnValue.NA);
   
   
   private final ColumnDataType column;
   private final T defaultValue;
   private final Class<T> aClass;

   public ColumnDTO(ColumnDataType column, Class<T> aClass, T defaultValue){
      this.column = column;
      this.aClass =  aClass;
      this.defaultValue = defaultValue;
      map.put(column, this);
   }
   
   public ColumnDTO(ColumnDataType column, T defaultValue){
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

   public static ColumnDTO getColumnDTO(ColumnDataType column) {
      ColumnDTO columnDTO = map.get(column);
      if(columnDTO == null){
         log.error("Does not contain " + column);
         System.err.println("Does not contain " + column);
      }
      return columnDTO;
   }
   
}
