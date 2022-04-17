package com.chinaums.ysmktaln.spbillserv;

import com.chinaums.ysmktaln.spbillserv.common.context.ApplicationContextProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpBillServApplication {
   public static void main(String[] args) {
      SpringApplication.run(SpBillServApplication.class, args);
      System.out.println("启动成功" + ApplicationContextProvider.getApplicationContext().getBean("threadPoolExecutor"));
   }
}
