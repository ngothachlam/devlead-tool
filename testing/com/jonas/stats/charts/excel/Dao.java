package com.jonas.stats.charts.excel;

import java.io.File;
import java.io.IOException;
import com.jonas.agile.devleadtool.burndown.ContentsDto;

public interface Dao {

   public ContentsDto loadContents(File xlsFile, String excelSheet) throws IOException;

}
