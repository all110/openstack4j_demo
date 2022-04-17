package com.chinaums.ysmktaln.spbillserv.common.Exception;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class Asserts {
   private static final Logger logger = LoggerFactory.getLogger(Asserts.class);

   public static <T extends Comparable<T>> void contains(T[] t1, T t2, HandleCode code, String message) {
      Comparable[] var4 = t1;
      int var5 = t1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         T t = (T)var4[var6];
         if (t.equals(t2)) {
            return;
         }
      }

      throw new HandleException(code, message);
   }

   public static <T extends Comparable<T>> void lt(T t1, T t2, HandleCode code, String message) {
      if (t1.compareTo(t2) >= 0) {
         logger.info("lt : t1 {} greater then or equals t2 {}", t1, t2);
         throw new HandleException(code, message);
      }
   }

   public static <T extends Comparable<T>> void le(T t1, T t2, HandleCode code, String message) {
      if (t1.compareTo(t2) > 0) {
         logger.info("le : t1 {} greater then t2 {}", t1, t2);
         throw new HandleException(code, message);
      }
   }

   public static <T extends Comparable<T>> void ge(T t1, T t2, HandleCode code, String message) {
      if (t1.compareTo(t2) < 0) {
         logger.info("ge : t1 {} less then t2 {}", t1, t2);
         throw new HandleException(code, message);
      }
   }

   public static <T extends Comparable<T>> void gt(T t1, T t2, HandleCode code, String message) {
      if (t1.compareTo(t2) <= 0) {
         logger.info("gt : t1 {} less then or equal t2 {}", t1, t2);
         throw new HandleException(code, message);
      }
   }

   public static <T extends Comparable<T>> void nq(T t1, T t2, HandleCode code, String message) {
      if (t1.equals(t2)) {
         logger.info("nq : t1 {} equal t2 {}", t1, t2);
         throw new HandleException(code, message);
      }
   }

   public static <T extends Comparable<T>> void eq(T t1, T t2, HandleCode code, String message) {
      if (!t1.equals(t2)) {
         logger.info("eq : t1 {} isn't equal t2 {}", t1, t2);
         throw new HandleException(code, message);
      }
   }

   public static void isTrue(boolean expression, HandleCode code, String message) {
      if (!expression) {
         logger.info("isTrue : {}", expression);
         throw new HandleException(code, message);
      }
   }

   public static void matches(String target, String pattern, HandleCode code, String message) {
      if (!target.matches(pattern)) {
         logger.info("matches : {} - {}", target, pattern);
         throw new HandleException(code, message);
      }
   }

   public static void isNull(Object object, HandleCode code, String message) {
      if (object != null) {
         logger.info("isNull : the object is not null {}", object);
         throw new HandleException(code, message);
      }
   }

   public static void notNull(Object object, HandleCode code, String message) {
      if (object == null) {
         logger.info("notNull : object is null");
         throw new HandleException(code, message);
      }
   }

   public static void hasLength(String text, HandleCode code, String message) {
      if (!StringUtils.hasLength(text)) {
         logger.info("hasLength : length is 0", message);
         throw new HandleException(code, message);
      }
   }

   public static void hasText(String text, HandleCode code, String message) {
      if (!StringUtils.hasText(text)) {
         logger.info("hasText : {}", message);
         throw new HandleException(code, message);
      }
   }

   public static void doesNotContain(String textToSearch, String substring, HandleCode code, String message) {
      if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.indexOf(substring) != -1) {
         logger.info("doesNotContain : search str {}, target str {}", substring, textToSearch);
         throw new HandleException(code, message);
      }
   }

   public static void notEmpty(Object[] array, HandleCode code, String message) {
      if (ObjectUtils.isEmpty(array)) {
         logger.info("notEmpty : array is empty");
         throw new HandleException(code, message);
      }
   }

   public static void noNullElements(Object[] array, HandleCode code, String message) {
      if (array != null) {
         for(int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
               logger.info("noNullElements : index[{}] is null", i);
               throw new HandleException(code, message);
            }
         }
      }

   }

   public static void notEmpty(Collection<?> collection, HandleCode code, String message) {
      if (CollectionUtils.isEmpty(collection)) {
         logger.info("notEmpty : collection is empty");
         throw new HandleException(code, message);
      }
   }

   public static void notEmpty(Map<?, ?> map, HandleCode code, String message) {
      if (CollectionUtils.isEmpty(map)) {
         logger.info("notEmpty : target args is empty");
         throw new HandleException(code, message);
      }
   }

   public static void notOverByteLength(String paramValue, int length, HandleCode code, String message) {
      Charset gbk = Charset.forName("GBK");
      int paramLength = paramValue.getBytes(gbk).length;
      if (paramLength > length) {
         throw new HandleException(code, message);
      }
   }
}
