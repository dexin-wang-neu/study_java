package com.atguigu.springcloud;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@Slf4j
@EnableFeignClients
@EnableHystrix
public class ConsumerFeignHystrixOrder80 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeignHystrixOrder80.class,args);
        log.info("使用Feign进行服务调用，使用Hystrix进行服务熔断降级的应用 80 启动");
    }
}
