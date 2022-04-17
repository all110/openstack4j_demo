package com.chinaums.ysmktaln.spbillserv.common.model;

public class ThreadProperties {
   private int corePoolSize;
   private int maxPoolSize;
   private int queueCapacity;
   private int keepAliveSeconds;

   public int getCorePoolSize() {
      return this.corePoolSize;
   }

   public void setCorePoolSize(int corePoolSize) {
      this.corePoolSize = corePoolSize;
   }

   public int getMaxPoolSize() {
      return this.maxPoolSize;
   }

   public void setMaxPoolSize(int maxPoolSize) {
      this.maxPoolSize = maxPoolSize;
   }

   public int getQueueCapacity() {
      return this.queueCapacity;
   }

   public void setQueueCapacity(int queueCapacity) {
      this.queueCapacity = queueCapacity;
   }

   public int getKeepAliveSeconds() {
      return this.keepAliveSeconds;
   }

   public void setKeepAliveSeconds(int keepAliveSeconds) {
      this.keepAliveSeconds = keepAliveSeconds;
   }
}
