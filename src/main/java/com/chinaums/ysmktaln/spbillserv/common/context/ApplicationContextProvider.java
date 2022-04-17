package com.chinaums.ysmktaln.spbillserv.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
   private static ApplicationContext applicationContext;

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      ApplicationContextProvider.applicationContext = applicationContext;
   }

   public static ApplicationContext getApplicationContext() {
      return applicationContext;
   }

   public static Object getBean(String name) {
      return getApplicationContext().getBean(name);
   }
}
