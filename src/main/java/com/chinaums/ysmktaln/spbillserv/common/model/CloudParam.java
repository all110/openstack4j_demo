package com.chinaums.ysmktaln.spbillserv.common.model;

public class CloudParam {
   private String request_id;
   private String create_type;
   private String cloud_type;
   private String service_id;
   private String volume_id;
   private String serve_name;
   private String image_id;
   private String flavor_id;
   private String status;

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getCreate_type() {
      return this.create_type;
   }

   public void setCreate_type(String create_type) {
      this.create_type = create_type;
   }

   public String getCloud_type() {
      return this.cloud_type;
   }

   public void setCloud_type(String cloud_type) {
      this.cloud_type = cloud_type;
   }

   public String getVolume_id() {
      return this.volume_id;
   }

   public void setVolume_id(String volume_id) {
      this.volume_id = volume_id;
   }

   public String getServe_name() {
      return this.serve_name;
   }

   public void setServe_name(String serve_name) {
      this.serve_name = serve_name;
   }

   public String getImage_id() {
      return this.image_id;
   }

   public void setImage_id(String image_id) {
      this.image_id = image_id;
   }

   public String getFlavor_id() {
      return this.flavor_id;
   }

   public void setFlavor_id(String flavor_id) {
      this.flavor_id = flavor_id;
   }

   public String getRequest_id() {
      return this.request_id;
   }

   public void setRequest_id(String request_id) {
      this.request_id = request_id;
   }

   public String getService_id() {
      return this.service_id;
   }

   public void setService_id(String service_id) {
      this.service_id = service_id;
   }
}
