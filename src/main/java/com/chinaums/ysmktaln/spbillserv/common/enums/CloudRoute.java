package com.chinaums.ysmktaln.spbillserv.common.enums;

import com.chinaums.ysmktaln.spbillserv.common.Exception.HandleCode;
import com.chinaums.ysmktaln.spbillserv.common.Exception.HandleException;
import com.chinaums.ysmktaln.spbillserv.common.context.ApplicationContextProvider;
import com.chinaums.ysmktaln.spbillserv.common.interfaces.CloudManager;

public enum CloudRoute {
   PRIVATE("PRIVATE", "privateCloudManager"),
   PUBLIC("PUBLIC", "publicCloudManager");

   private String type;
   private String cloudBeanName;

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getCloudBeanName() {
      return this.cloudBeanName;
   }

   public void setCloudBeanName(String cloudBeanName) {
      this.cloudBeanName = cloudBeanName;
   }

   private CloudRoute(String type, String cloudBeanName) {
      this.type = type;
      this.cloudBeanName = cloudBeanName;
   }

   public static CloudManager getCloudManager(String type) {
      CloudRoute[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CloudRoute cloudRoute = var1[var3];
         if (cloudRoute.getType().equals(type)) {
            return (CloudManager)ApplicationContextProvider.getApplicationContext().getBean(cloudRoute.getCloudBeanName());
         }
      }

      throw new HandleException(HandleCode.RSP_1001, "无效类型");
   }
}
