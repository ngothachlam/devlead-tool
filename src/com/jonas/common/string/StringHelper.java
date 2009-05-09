package com.jonas.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

   public List<String> separate(String string, String delimiter) {
      List<String> result = new ArrayList<String>();
      StringTokenizer st = new StringTokenizer(string, delimiter);
      while (st.hasMoreTokens()) {
         result.add(st.nextToken());
      }
      return result;
   }

   public List<String> generateSeparateList(String string) {
      return generateSeparateList(string, " \t,");
   }

   protected List<String> generateSeparateList(String string, String delim) {
      List<String> list = separate(string, delim);
      List<String> result = new ArrayList<String>();

      for (String entry : list) {
         ResultDTO startAndEnd = getPrefixStartAndEndNo(entry, "<");
         if (startAndEnd.getStart() != null) {
            List<String> startAndEndList = getListFromStartAndEnd(startAndEnd);
            for (String startAndEndListEntry : startAndEndList) {
               result.add(startAndEndListEntry);
            }
         } else {
            result.add(startAndEnd.getPrefix());
         }
      }
      return result;
   }

   protected List<String> getListFromStartAndEnd(ResultDTO startAndEnd) {
      int start = Integer.parseInt(startAndEnd.getStart());
      int end = Integer.parseInt(startAndEnd.getEnd());
      List<String> result = new ArrayList<String>(end - start);
      for (int temp = start; temp <= end; temp++) {
         result.add(startAndEnd.getPrefix() + temp);
      }
      return result;
   }

   protected ResultDTO getPrefixStartAndEndNo(String string, String delim) {
      string = string.trim();
      Pattern pattern = Pattern.compile("^(.*)([0-9])+" + delim + "([0-9]+)");
      Matcher matcher = pattern.matcher(string);
      ResultDTO result = new ResultDTO(string, null, null);
      if (matcher.find()) {
         result.setPrefix(matcher.group(1));
         result.setStart(matcher.group(2));
         result.setEnd(matcher.group(3));
      }
      if (result.getPrefix().contains(delim)) {
         throw new ParseException("The string " + string + "cannot be parsed!");
      }
      return result;
   }

   public static boolean isDouble(Object string) {
      if (string == null) {
         return false;
      }
      try {
         Double.valueOf(string.toString());
         return true;
      } catch (NumberFormatException e) {
         return false;
      }
   }

   public static Double getDoubleOrNull(Object string) {
      if (string == null) {
         return null;
      }
      try {
         return Double.valueOf(string.toString());
      } catch (NumberFormatException e) {
         return null;
      }
   }

   public static double getDoubleOrZero(Object string) {
      if (string == null) {
         return 0d;
      }
      try {
         return Double.valueOf(string.toString());
      } catch (NumberFormatException e) {
         return 0d;
      }
   }

   public static String getNiceString(Set<String> strings) {
      StringBuffer sb = new StringBuffer("");
      int i = 0;
      for (String string : strings) {
         sb.append(string);
         if (++i < strings.size()) {
            sb.append(", ");
         }
      }
      return sb.toString();
   }

   public static boolean isEmpty(Object value) {
      if (value == null)
         return true;
      if (value.toString().trim().length() == 0)
         return true;
      return false;
   }
}

class ResultDTO {

   public ResultDTO() {
   }

   public ResultDTO(String prefix, String start, String end) {
      this.prefix = prefix;
      this.start = start;
      this.end = end;

   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((end == null) ? 0 : end.hashCode());
      result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
      result = prime * result + ((start == null) ? 0 : start.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ResultDTO other = (ResultDTO) obj;
      if (end == null) {
         if (other.end != null)
            return false;
      } else if (!end.equals(other.end))
         return false;
      if (prefix == null) {
         if (other.prefix != null)
            return false;
      } else if (!prefix.equals(other.prefix))
         return false;
      if (start == null) {
         if (other.start != null)
            return false;
      } else if (!start.equals(other.start))
         return false;
      return true;
   }

   public String getPrefix() {
      return prefix;
   }

   public String getEnd() {
      return end;
   }

   public String getStart() {
      return start;
   }

   private String prefix;
   private String end;
   private String start;

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public void setEnd(String end) {
      this.end = end;
   }

   public void setStart(String start) {
      this.start = start;
   }

}

class ParseException extends RuntimeException {

   public ParseException(String string) {
      // TODO Auto-generated constructor stub
   }

}