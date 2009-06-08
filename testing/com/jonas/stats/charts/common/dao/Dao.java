package com.jonas.stats.charts.common.dao;

import java.io.File;
import java.io.IOException;
import com.jonas.agile.devleadtool.burndown.ContentsDto;

public interface Dao {

   public ContentsDto loadContents() throws IOException;

}
