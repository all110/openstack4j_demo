package com.chinaums.ysmktaln.spbillserv.common.model;

public class CloudProperties {
   private String authUrl;
   private String userName;
   private String password;
   private String tenantName;

   public String getAuthUrl() {
      return this.authUrl;
   }

   public void setAuthUrl(String authUrl) {
      this.authUrl = authUrl;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getTenantName() {
      return this.tenantName;
   }

   public void setTenantName(String tenantName) {
      this.tenantName = tenantName;
   }
}
