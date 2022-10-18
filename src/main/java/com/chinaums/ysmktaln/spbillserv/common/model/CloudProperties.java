package com.chinaums.ysmktaln.spbillserv.common.model;

public class CloudProperties {
   private String authUrl;
   private String userName;
   private String password;
//   private String tenantName;
//   private String projectName;
   private String projectId;
   private String domainName;

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

//   public String getTenantName() {
//      return this.tenantName;
//   }
//
//   public void setTenantName(String tenantName) {
//      this.tenantName = tenantName;
//   }
//   public String getProjectName() {
//      return projectName;
//   }
//
//   public void setProjectName(String projectName) {
//      this.projectName = projectName;
//   }

   public String getProjectId() {
      return projectId;
   }

   public void setProjectId(String projectId) {
      this.projectId = projectId;
   }

   public String getDomainName() {
      return domainName;
   }

   public void setDomainName(String domainName) {
      this.domainName = domainName;
   }


   @Override
   public String toString() {
      return "CloudProperties{" +
              "authUrl='" + authUrl + '\'' +
              ", userName='" + userName + '\'' +
              ", password='" + password + '\'' +
              ", projectId='" + projectId + '\'' +
              ", domainName='" + domainName + '\'' +
              '}';
   }
}
