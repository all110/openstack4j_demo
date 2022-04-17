package com.chinaums.ysmktaln.spbillserv.common.config;

import com.chinaums.ysmktaln.spbillserv.common.interfaces.CloudManager;
import com.chinaums.ysmktaln.spbillserv.common.interfaces.impl.PrivateCloudManagerImpl;
import com.chinaums.ysmktaln.spbillserv.common.interfaces.impl.PublicCloudManagerImpl;
import com.chinaums.ysmktaln.spbillserv.common.model.CloudProperties;
import com.chinaums.ysmktaln.spbillserv.common.model.ThreadProperties;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource({"classpath:openstack.properties"})
public class CloudManagerConfig {
   @Bean(
      name = {"publicProperties"}
   )
   @ConfigurationProperties(
      prefix = "public.openstack"
   )
   public CloudProperties cloudProperties1() {
      return new CloudProperties();
   }

   @Bean(
      name = {"privateProperties"}
   )
   @ConfigurationProperties(
      prefix = "private.openstack"
   )
   public CloudProperties cloudProperties2() {
      return new CloudProperties();
   }

   @Bean(
      name = {"privateCloudManager"}
   )
   @Scope("prototype")
   public CloudManager privateCloud(CloudProperties privateProperties) {
      PrivateCloudManagerImpl privateCloudManager = new PrivateCloudManagerImpl(privateProperties);
      return privateCloudManager;
   }

   @Bean(
      name = {"publicCloudManager"}
   )
   @Scope("prototype")
   public CloudManager publicCloud(CloudProperties publicProperties) {
      PublicCloudManagerImpl publicCloudManager = new PublicCloudManagerImpl(publicProperties);
      return publicCloudManager;
   }

   @Bean(
      name = {"threadProperties"}
   )
   @ConfigurationProperties(
      prefix = "threadpool"
   )
   public ThreadProperties threadConfig() {
      return new ThreadProperties();
   }

   @Bean(
      name = {"threadPoolExecutor"}
   )
   public ThreadPoolExecutor threadPoolExecutor(ThreadProperties threadConfig) {
      return new ThreadPoolExecutor(threadConfig.getCorePoolSize(), threadConfig.getMaxPoolSize(), (long)threadConfig.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingDeque(threadConfig.getQueueCapacity()), new CallerRunsPolicy());
   }
}
