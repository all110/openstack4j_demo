package com.chinaums.ysmktaln.spbillserv.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils {
   public static final int BEFORE = 1;
   public static final int AFTER = 2;
   public static final String DEFAULT_PATH_SEPARATOR = ",";
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

   public static String formatAmount(String amount) {
      return DECIMAL_FORMAT.format(Double.valueOf(amount));
   }

   public static String formatAmount(String amount, int argVal) {
      return DECIMAL_FORMAT.format(Double.valueOf(amount) / (double)argVal);
   }

   public static String str2HexStr(String str) throws UnsupportedEncodingException {
      byte[] bytearr = str.getBytes("UTF-8");
      if (bytearr == null) {
         return null;
      } else {
         StringBuffer sb = new StringBuffer();

         for(int k = 0; k < bytearr.length; ++k) {
            if ((bytearr[k] & 255) < 16) {
               sb.append("0");
            }

            sb.append(Integer.toString(bytearr[k] & 255, 16));
         }

         return sb.toString();
      }
   }

   public static String byteArr2HexString(byte[] bytearr) {
      if (bytearr == null) {
         return "null";
      } else {
         StringBuffer sb = new StringBuffer();

         for(int k = 0; k < bytearr.length; ++k) {
            if ((bytearr[k] & 255) < 16) {
               sb.append("0");
            }

            sb.append(Integer.toString(bytearr[k] & 255, 16));
         }

         return sb.toString();
      }
   }

   public static ArrayList<String> strToArrayList(String str) {
      return strToArrayListManager(str, ",");
   }

   public static ArrayList<String> strToArrayList(String str, String separator) {
      return strToArrayListManager(str, separator);
   }

   private static ArrayList<String> strToArrayListManager(String str, String separator) {
      StringTokenizer strTokens = new StringTokenizer(str, separator);
      ArrayList list = new ArrayList();

      while(strTokens.hasMoreTokens()) {
         list.add(strTokens.nextToken().trim());
      }

      return list;
   }

   public static String[] strToStrArray(String str) {
      return strToStrArrayManager(str, ",");
   }

   public static String[] strToStrArray(String str, String separator) {
      return strToStrArrayManager(str, separator);
   }

   private static String[] strToStrArrayManager(String str, String separator) {
      StringTokenizer strTokens = new StringTokenizer(str, separator);
      String[] strArray = new String[strTokens.countTokens()];

      for(int i = 0; strTokens.hasMoreTokens(); ++i) {
         strArray[i] = strTokens.nextToken().trim();
      }

      return strArray;
   }

   public static Set<String> strToSet(String str) {
      return strToSet(str, ",");
   }

   public static Set<String> strToSet(String str, String separator) {
      String[] values = strToStrArray(str, separator);
      Set<String> result = new LinkedHashSet();

      for(int i = 0; i < values.length; ++i) {
         result.add(values[i]);
      }

      return result;
   }

   public static String ArrayToStr(Object[] array) {
      if (array != null && array.length >= 0) {
         String str = "";
         int _step = 0;

         for(int i = 0; i < array.length; ++i) {
            if (_step > 0) {
               str = str + ",";
            }

            str = str + (String)array[i];
            ++_step;
         }

         return str;
      } else {
         return null;
      }
   }

   public static String CollectionToStr(Collection<String> coll) {
      StringBuffer sb = new StringBuffer();
      int i = 0;
      Iterator var3 = coll.iterator();

      while(var3.hasNext()) {
         String string = (String)var3.next();
         if (i > 0) {
            sb.append(",");
         }

         ++i;
         sb.append(string);
      }

      return sb.toString();
   }

   public static String encodingTransfer(String inputString, String inencoding, String outencoding) {
      if (inputString != null && inputString.length() != 0) {
         try {
            return new String(inputString.getBytes(outencoding), inencoding);
         } catch (Exception var4) {
            return inputString;
         }
      } else {
         return inputString;
      }
   }

   public static String delString(String inputString, String delStrs) {
      if (inputString != null && inputString.length() != 0) {
         String[] strs = strToStrArray(delStrs);

         for(int i = 0; i < strs.length; ++i) {
            if (strs[i].equals("")) {
               inputString = inputString.replaceAll(" ", "");
            } else if (strs[i].compareTo("a") >= 0) {
               inputString = replace(inputString, strs[i], "");
            } else {
               inputString = inputString.replaceAll("\\" + strs[i], "");
            }
         }

         if (subCount(delStrs, ",") > strs.length) {
            inputString = inputString.replaceAll("\\,", "");
         }

         return inputString;
      } else {
         return inputString;
      }
   }

   public static String trimLeft(String value) {
      String result = value;
      if (value == null) {
         return value;
      } else {
         char[] ch = value.toCharArray();
         int index = -1;

         for(int i = 0; i < ch.length && Character.isWhitespace(ch[i]); index = i++) {
         }

         if (index != -1) {
            result = value.substring(index + 1);
         }

         return result;
      }
   }

   public static String trimRight(String value) {
      String result = value;
      if (value == null) {
         return value;
      } else {
         char[] ch = value.toCharArray();
         int endIndex = -1;

         for(int i = ch.length - 1; i > -1 && Character.isWhitespace(ch[i]); endIndex = i--) {
         }

         if (endIndex != -1) {
            result = value.substring(0, endIndex);
         }

         return result;
      }
   }

   public static boolean isInclude(String parentStr, String subStr) {
      return parentStr.indexOf(subStr) >= 0;
   }

   public static boolean isIncludes(String parentStr, String subStrs) {
      String[] subStrArray = strToStrArray(subStrs);

      for(int j = 0; j < subStrArray.length; ++j) {
         String subStr = subStrArray[j];
         if (isInclude(parentStr, subStr)) {
            return true;
         }
      }

      return false;
   }

   public static int subCount(String parentStr, String subStr) {
      int count = 0;

      for(int i = 0; i < parentStr.length() - subStr.length(); ++i) {
         String tempString = parentStr.substring(i, i + subStr.length());
         if (subStr.equals(tempString)) {
            ++count;
         }
      }

      return count;
   }

   public static String subString(String parentStr, String startStr, String endStr) {
      return parentStr.substring(parentStr.indexOf(startStr) + startStr.length(), parentStr.indexOf(endStr));
   }

   public static List<String> subStringList(String parentStr, String startStr, String endStr) {
      List<String> result = new ArrayList();
      List<String> elements = dividToList(parentStr, startStr, endStr);
      Iterator var5 = elements.iterator();

      while(var5.hasNext()) {
         String element = (String)var5.next();
         if (isIncludes(element, startStr) && isIncludes(element, endStr)) {
            result.add(subString(element, startStr, endStr));
         }
      }

      return result;
   }

   public static String getUnicodeStr(String inStr) {
      char[] myBuffer = inStr.toCharArray();
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < inStr.length(); ++i) {
         short s = (short)myBuffer[i];
         String hexS = Integer.toHexString(s);
         sb.append(" \\u");
         sb.append(fillStr(hexS, "0", 4, 2));
      }

      return sb.toString();
   }

   public static boolean isJavaIdentifier(String s) {
      if (s.length() != 0 && !Character.isJavaIdentifierStart(s.charAt(0))) {
         for(int i = 0; i < s.length(); ++i) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static String replaceAll(String str, char oldchar, char newchar) {
      char[] chars = str.toCharArray();

      for(int i = 0; i < chars.length; ++i) {
         if (chars[i] == oldchar) {
            chars[i] = newchar;
         }
      }

      return new String(chars);
   }

   public static String fillStr(String inStr, String fill, int length, int direction) {
      if (inStr != null && inStr.length() <= length && inStr.length() != 0) {
         StringBuffer tempStr = new StringBuffer("");

         for(int i = 0; i < length - inStr.length(); ++i) {
            tempStr.append(fill);
         }

         return direction == 2 ? new String(tempStr.toString() + inStr) : new String(inStr + tempStr.toString());
      } else {
         return inStr;
      }
   }

   public static String replace(String str, String pattern, String replace) {
      int s = 0;
//      int e = false;

      StringBuffer result;
      int e;
      for(result = new StringBuffer(); (e = str.indexOf(pattern, s)) >= 0; s = e + pattern.length()) {
         result.append(str.substring(s, e));
         result.append(replace);
      }

      result.append(str.substring(s));
      return result.toString();
   }

   public static List<String> dividToList(String str, String start, String end) {
      if (str != null && str.length() != 0) {
         int s = 0;
//         int e = false;
         List<String> result = new ArrayList();
         if (isInclude(str, start) && isInclude(str, end)) {
            int e;
            while((e = str.indexOf(start, s)) >= 0) {
               result.add(str.substring(s, e));
               s = str.indexOf(end, e) + end.length();
               result.add(str.substring(e, s));
            }

            if (s < str.length()) {
               result.add(str.substring(s));
            }

            if (s == 0) {
               result.add(str);
            }
         } else {
            result.add(str);
         }

         return result;
      } else {
         return null;
      }
   }

   public static String upperFrist(String string) {
      if (string == null) {
         return null;
      } else {
         String upper = string.toUpperCase();
         return upper.substring(0, 1) + string.substring(1);
      }
   }

   public static String lowerFrist(String string) {
      if (string == null) {
         return null;
      } else {
         String lower = string.toLowerCase();
         return lower.substring(0, 1) + string.substring(1);
      }
   }

   public static String URLEncode(String string, String encode) {
      try {
         return URLEncoder.encode(string, encode);
      } catch (UnsupportedEncodingException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static String DateToStr(Date date, String format) {
      SimpleDateFormat formatter = new SimpleDateFormat(format);
      return formatter.format(date);
   }

   public static String escapeHtml(String string) {
      if (string != null && string.length() != 0) {
         char c = 0;
         int len = string.length();
         StringBuffer sb = new StringBuffer(len + 4);

         for(int i = 0; i < len; ++i) {
            char b = c;
            c = string.charAt(i);
            switch(c) {
            case '\b':
               sb.append("\\b");
               continue;
            case '\t':
               sb.append("\\t");
               continue;
            case '\n':
               sb.append("\\n");
               continue;
            case '\f':
               sb.append("\\f");
               continue;
            case '\r':
               sb.append("\\r");
               continue;
            case '"':
            case '\\':
               sb.append('\\');
               sb.append(c);
               continue;
            case '/':
               if (b == '<') {
                  sb.append('\\');
               }

               sb.append(c);
               continue;
            }

            if (c >= ' ' && (c < 128 || c >= 160) && (c < 8192 || c >= 8448)) {
               sb.append(c);
            } else {
               String t = "000" + Integer.toHexString(c);
               sb.append("\\u" + t.substring(t.length() - 4));
            }
         }

         return sb.toString();
      } else {
         return "";
      }
   }

   public static String randomString(int length) {
      if (length < 1) {
         return null;
      } else {
         Random randGen = new Random();
         char[] numbersAndLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
         char[] randBuffer = new char[length];

         for(int i = 0; i < randBuffer.length; ++i) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(51)];
         }

         return new String(randBuffer);
      }
   }

   public static String firstCharUpper(String s) {
      String result = s.substring(0, 1).toUpperCase() + s.substring(1);
      return result;
   }

   public static String firstCharLower(String s) {
      String result = s.substring(0, 1).toLowerCase() + s.substring(1);
      return result;
   }

   public static String genRandom(int length) {
      StringBuffer buffer = new StringBuffer();
      Random r = new Random();
      int i = 0;

      while(true) {
         int c;
         do {
            if (i >= length) {
               return buffer.toString();
            }

            c = r.nextInt(122);
         } while((48 > c || c > 57) && (65 > c || c > 90) && (97 > c || c > 122));

         buffer.append((char)c);
         ++i;
      }
   }

   public static String fillLeft(String orgStr, char fillWith, int fixLen) {
      return fillStr(orgStr, fillWith, fixLen, true);
   }

   public static String fillRight(String orgStr, char fillWith, int fixLen) {
      return fillStr(orgStr, fillWith, fixLen, false);
   }

   private static String fillStr(String orgStr, char fillWith, int fixLen, boolean isLeft) {
      int toFill = fixLen - orgStr.length();
      if (toFill <= 0) {
         return orgStr;
      } else {
         StringBuilder sb;
         for(sb = new StringBuilder(orgStr); toFill > 0; --toFill) {
            if (isLeft) {
               sb.insert(0, fillWith);
            } else {
               sb.append(fillWith);
            }
         }

         return sb.toString();
      }
   }

   public static String toTrim(String str) {
      return str == null ? "" : str.trim();
   }

   public static String convertToString(int length, int value) {
      StringBuffer buffer = new StringBuffer();

      for(int i = 0; i < length - ("" + value).length(); ++i) {
         buffer.append("0");
      }

      buffer.append(value);
      return buffer.toString();
   }

   public static String arrayToString(Object[] array, String split) {
      StringBuffer buffer = new StringBuffer();

      for(int i = 0; i < array.length; ++i) {
         buffer.append(array[i].toString()).append(split);
      }

      return buffer.length() != 0 ? buffer.substring(0, buffer.length() - split.length()) : "";
   }

   public static String arrayToString(Set<?> set, String split) {
      StringBuffer buffer = new StringBuffer();
      Iterator i = set.iterator();

      while(i.hasNext()) {
         buffer.append(i.next().toString()).append(split);
      }

      return buffer.length() != 0 ? buffer.substring(0, buffer.length() - split.length()) : "";
   }

   public static String toHexString(String src) {
      byte[] b = src.getBytes();
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < b.length; ++i) {
         if ((255 & b[i]) < 15) {
            sb.append('0');
         }

         sb.append(Integer.toHexString(255 & b[i]));
      }

      return sb.toString();
   }

   public static String toHexString(String src, String charsetName) throws Exception {
      byte[] b = src.getBytes(charsetName);
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < b.length; ++i) {
         if ((255 & b[i]) < 15) {
            sb.append('0');
         }

         sb.append(Integer.toHexString(255 & b[i]));
      }

      return sb.toString();
   }

   public static String hexStringToString(String hex) {
      return new String(hexStringToByte(hex));
   }

   public static String hexStringToString(String hex, String charsetName) throws Exception {
      return new String(hexStringToByte(hex), charsetName);
   }

   public static byte[] hexStringToByte(String hex) {
      if (hex.length() == 1) {
         hex = "0" + hex;
      }

      hex = hex.toUpperCase();
      int len = hex.length() / 2;
      byte[] result = new byte[len];
      char[] achar = hex.toCharArray();

      for(int i = 0; i < len; ++i) {
         int pos = i * 2;
         result[i] = (byte)(toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
      }

      return result;
   }

   public static byte[] int2byteArray(int length) {
      byte[] result = new byte[]{(byte)(length >> 24 & 255), (byte)(length >> 16 & 255), (byte)(length >> 8 & 255), (byte)(length & 255)};
      return result;
   }

   public static int byteArrayToInt(byte[] bytes) {
      int value = 0;

      for(int i = 0; i < 4; ++i) {
         int shift = (3 - i) * 8;
         value += (bytes[i] & 255) << shift;
      }

      return value;
   }

   public static byte[] addBytes(byte[] data1, byte[] data2) {
      byte[] data3 = new byte[data1.length + data2.length];
      System.arraycopy(data1, 0, data3, 0, data1.length);
      System.arraycopy(data2, 0, data3, data1.length, data2.length);
      return data3;
   }

   private static byte toByte(char c) {
      byte b = (byte)"0123456789ABCDEF".indexOf(c);
      return b;
   }

   public static String repNull(Object o) {
      return o == null ? "" : o.toString().trim();
   }

   public static String generateInitPwd() {
      return generateRandomString(6);
   }

   public static String generateRandomString(int len) {
      char[] mm = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
      StringBuffer sb = new StringBuffer();
      Random random = new Random();

      for(int i = 0; i < len; ++i) {
         sb.append(mm[random.nextInt(mm.length)]);
      }

      return sb.toString();
   }

   public static String dateFormat(String source) {
      String[] lst = source.split("-");
      StringBuffer sb = new StringBuffer();
      if (lst.length <= 1) {
         return source;
      } else {
         String[] var3 = lst;
         int var4 = lst.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String item = var3[var5];
            sb.append(item);
         }

         return sb.toString();
      }
   }

   public static String formatDate(String orderdate) {
      if (orderdate == null) {
         return "";
      } else if (orderdate.length() == 8) {
         return orderdate.substring(0, 4) + "-" + orderdate.substring(4, 6) + "-" + orderdate.substring(6);
      } else {
         return orderdate.length() == 6 ? orderdate.substring(0, 4) + "-" + orderdate.substring(4, 6) : orderdate;
      }
   }

   public static String getUUID() {
      UUID uuid = UUID.randomUUID();
      return uuid.toString().replaceAll("-", "");
   }

   public static boolean isEmpty(String str) {
      return str == null || str.trim().length() == 0;
   }

   public static String nvl(String str, String defValue) {
      return isEmpty(str) ? defValue : str;
   }

   public static String getSubstring(String str, int startPos, int endPos) {
      String ret = null;
      if (str != null && str.length() >= endPos) {
         ret = str.substring(startPos - 1, endPos - startPos + 1);
      }

      return ret;
   }

   public static String regMatch(String str, String reg, String def) {
      Pattern regex = Pattern.compile(reg);
      Matcher regexMatcher = regex.matcher(str);
      if (regexMatcher.find()) {
         String resultString = regexMatcher.group();
         return resultString;
      } else {
         return def;
      }
   }

   public static String getStackTrace(Throwable t) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);

      String var3;
      try {
         t.printStackTrace(pw);
         var3 = sw.toString();
      } finally {
         pw.close();
      }

      return var3;
   }

   public static String replaceToXml(String source) throws Exception {
      return isBlank(source) ? source : source.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
   }

   public static final String convertNullToEmpty(String source) {
      try {
         return source == null ? "" : source.trim();
      } catch (Exception var2) {
         return "";
      }
   }

   public static String change2star(String str, int prefix, int suffix, String star) {
      String pattern = "";

      for(int i = 0; i < str.length() - (prefix + suffix); ++i) {
         pattern = pattern + star;
      }

      String dcardno = str.replaceAll(str.substring(prefix, str.length() - suffix), pattern);
      return dcardno;
   }
}
