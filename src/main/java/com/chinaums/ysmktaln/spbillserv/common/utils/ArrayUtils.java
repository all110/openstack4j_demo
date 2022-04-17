package com.chinaums.ysmktaln.spbillserv.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ArrayUtils {
   public static boolean isEmpty(Collection<?> collection) {
      return collection == null || collection.isEmpty();
   }

   public static boolean isEmpty(Map<?, ?> map) {
      return map == null || map.isEmpty();
   }

   public static boolean isEmpty(Object[] array) {
      return array == null || array.length == 0;
   }

   public static boolean isEmpty(List<Object> list) {
      return list == null || list.size() == 0;
   }
}
