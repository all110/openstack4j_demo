package com.chinaums.ysmktaln.spbillserv.common.utils;

import com.chinaums.ysmktaln.spbillserv.common.Exception.Asserts;
import com.chinaums.ysmktaln.spbillserv.common.Exception.HandleCode;
import com.chinaums.ysmktaln.spbillserv.common.Exception.HandleException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Strings extends org.apache.commons.lang.StringUtils {
   private static Logger logger = LoggerFactory.getLogger(Strings.class);

   public static String getUUID() {
      UUID uuid = UUID.randomUUID();
      return uuid.toString().replaceAll("-", "");
   }

   public static byte[] str2bytes(String val) {
      logger.debug("【开始字符串转二进制数组】");

      byte[] var2;
      try {
         byte[] bytes = val.getBytes("utf-8");
         var2 = bytes;
      } catch (UnsupportedEncodingException var6) {
         var6.printStackTrace();
         logger.error("[str2bytes]:{}", var6);
         throw new HandleException(HandleCode.RSP_9999, "不支持该编码");
      } finally {
         logger.debug("【字符串转二进制数组完成】");
      }

      return var2;
   }

   public static String bytes2str(byte[] data) {
      logger.debug("【开始二进制数组转字符串】");

      String var1;
      try {
         var1 = new String(data, "utf-8");
      } catch (UnsupportedEncodingException var5) {
         var5.printStackTrace();
         logger.error("[bytes2str]:{}", var5);
         throw new HandleException(HandleCode.RSP_9999, "不支持该编码");
      } finally {
         logger.debug("【二进制数组转字符串完成】");
      }

      return var1;
   }

   public static String bytes2hex(byte[] bytearr) {
      if (bytearr == null) {
         return "";
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

   public static byte[] hex2bytes(String hex) {
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

   public static String str2hex(String str) {
      logger.debug("【开始字符串转HEX字符串】");
      byte[] bytearr = str2bytes(str);
      Asserts.notNull(bytearr, HandleCode.RSP_9999, "待转码二进制数据为空");
      StringBuffer sb = new StringBuffer();

      for(int k = 0; k < bytearr.length; ++k) {
         if ((bytearr[k] & 255) < 16) {
            sb.append("0");
         }

         sb.append(Integer.toString(bytearr[k] & 255, 16));
      }

      logger.debug("【字符串转HEX字符串完成】");
      return sb.toString();
   }

   public static String hex2str(String hex) {
      logger.debug("【开始HEX字符串转字符串】");
      String result = bytes2str(hex2bytes(hex));
      logger.debug("【HEX字符串转字符串完成】");
      return result;
   }

   private static byte toByte(char c) {
      byte b = (byte)"0123456789ABCDEF".indexOf(c);
      return b;
   }

   public static long bytes2long(byte[] b) {
      long temp = 0L;
      long res = 0L;

      for(int i = 0; i < b.length; ++i) {
         res <<= 8;
         temp = (long)(b[i] & 255);
         res |= temp;
      }

      return res;
   }

   public static String nvl(String str, String defaultVal) {
      return defaultIfEmpty(str, defaultVal);
   }

   public static String nvlAdd(String value, String defaultVal) {
      return isEmpty(value) ? defaultVal : '%' + value + '%';
   }

   public static String byteSubstring(String s, int length) {
      if (org.apache.commons.lang.StringUtils.isEmpty(s)) {
         return "";
      } else {
         Object var2 = null;

         try {
            byte[] bytes = s.getBytes("Unicode");
            int n = 0;

            int i;
            for(i = 2; i < bytes.length && n < length; ++i) {
               if (i % 2 == 1) {
                  ++n;
               } else if (bytes[i] != 0) {
                  ++n;
               }
            }

            if (i % 2 == 1) {
               if (bytes[i - 1] != 0) {
                  --i;
               } else {
                  ++i;
               }
            }

            return new String(bytes, 0, i, "Unicode");
         } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
            return "";
         }
      }
   }

   public static String joins(Collection<?> collection, String separator) {
      return collection == null ? null : joins(collection.iterator(), separator);
   }

   public static String joins(Iterator iterator, String separator) {
      if (iterator == null) {
         return null;
      } else if (!iterator.hasNext()) {
         return "";
      } else {
         Object first = iterator.next();
         if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
         } else {
            StringBuffer buf = new StringBuffer(256);
            if (first != null) {
               buf.append(first);
            }

            while(iterator.hasNext()) {
               if (separator != null) {
                  buf.append(separator);
               }

               Object obj = iterator.next();
               if (obj != null) {
                  buf.append(obj);
               }
            }

            return buf.toString();
         }
      }
   }

   public static String map(Map<String, ?> collection, String format, String sep) {
      List<String> param = new ArrayList();
      Iterator var4 = collection.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<String, ?> entry = (Entry)var4.next();
         param.add(String.format(format, entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString()));
      }

      return joins((Collection)param, sep);
   }

   public static String cardEnd(String cardNo) {
      Asserts.hasText(cardNo, HandleCode.RSP_9999, "卡号为空");
      Asserts.ge(cardNo.length(), 10, HandleCode.RSP_9999, "卡号长度不正确");
      String result = cardNo.substring(cardNo.length() - 4, cardNo.length());
      return result;
   }

   public static String change2star(String str, int prefix, int suffix, String star) {
      if (isBlank(str)) {
         return "";
      } else {
         Asserts.ge(prefix, 0, HandleCode.RSP_9996, "截取范围错误");
         Asserts.ge(suffix, 0, HandleCode.RSP_9996, "截取范围错误");
         Asserts.hasText(str, HandleCode.RSP_9996, "截取字符串为空");
         Asserts.ge(str.length(), prefix + suffix, HandleCode.RSP_9996, "截取内容不在截取范围内");
         int len = str.length() - prefix - suffix;
         if (len < 1) {
            len = 4;
            prefix = (str.length() - 4) / 2;
         }

         char[] chars = str.toCharArray();

         for(int i = 0; i < chars.length; ++i) {
            if (i >= prefix && i < prefix + len) {
               chars[i] = star.charAt(0);
            }
         }

         return new String(chars);
      }
   }
}
