package com.jonas.agile.devleadtool.poi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class TestColor {

   public static void main(String[] args) {
      HSSFWorkbook hwb = new HSSFWorkbook();
      HSSFSheet sheet = hwb.createSheet("new sheet");
      for (int i = 0; i < 1; i++) {
         HSSFRow row = sheet.createRow((short) 0 + i);
         HSSFCell cell = row.createCell((short) 0);
         cell.setCellValue(1);
         
         HSSFCellStyle style = hwb.createCellStyle();
         style.setFillForegroundColor(HSSFColor.GREEN.index);
         style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         cell.setCellStyle(style);
         
      }
      try {
         FileOutputStream fileOut = new FileOutputStream("C:\\fillsColor.xls");
         hwb.write(fileOut);
         fileOut.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      System.out.println("Your excel file has been generated");
   }

}
