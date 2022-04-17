package com.chinaums.ysmktaln.spbillserv.common.utils;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgInitUtils {
   private static Logger LOGGER = LoggerFactory.getLogger(MsgInitUtils.class);

   public static Map<String, Object> initRequestMap(HttpServletRequest request) {
      Map<String, Object> params = new HashMap();
      String jsonType = "application/json";
      String content_type = request.getContentType();
      LOGGER.info("content_type:{}", content_type);
      String key;
      if (content_type != null && content_type.contains(jsonType)) {
         StringBuffer sb = new StringBuffer();

         try {
            InputStream input = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            key = null;

            while((key = reader.readLine()) != null) {
               sb.append(key);
            }

            JSONObject jsonObject = JSONObject.parseObject(sb.toString());
            params.putAll(jsonObject);
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      } else {
         Map map = request.getParameterMap();
         Iterator iterator = map.entrySet().iterator();

         label33:
         while(true) {
            while(true) {
               if (!iterator.hasNext()) {
                  break label33;
               }

               Entry<String, Object[]> entry = (Entry)iterator.next();
               key = (String)entry.getKey();
               Object[] value = (Object[])entry.getValue();
               if (value != null && value.length > 0) {
                  params.put(key, value[0]);
               } else {
                  params.put(key, (Object)null);
               }
            }
         }
      }

      LOGGER.info("请求报文：{}", JSONObject.toJSONString(params));
      return params;
   }

   public static Map<String, Object> toResponseMap(Map<String, Object> requestMap) {
      Map<String, Object> response = msgHeader(requestMap);
      response.put("msg_flg", "1");
      response.put("msg_sender", "1");
      return response;
   }

   public static Map<String, Object> msgHeader(Map<String, Object> requestMap) {
      Map<String, Object> msgHeader = new HashMap();
      requestMap.entrySet().stream().filter((entry) -> {
         return org.apache.commons.lang.StringUtils.startsWith((String)entry.getKey(), "msg");
      }).forEach((entry) -> {
         msgHeader.put(entry.getKey(), entry.getValue());
      });
      return msgHeader;
   }
}
