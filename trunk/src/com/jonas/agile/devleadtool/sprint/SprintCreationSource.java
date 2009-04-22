package com.jonas.agile.devleadtool.sprint;

import java.util.Date;

public interface SprintCreationSource {

   public String getName();
   public Date getStart();
   public Date getEnd();
   public Integer getLength();
   public void clear();

}
