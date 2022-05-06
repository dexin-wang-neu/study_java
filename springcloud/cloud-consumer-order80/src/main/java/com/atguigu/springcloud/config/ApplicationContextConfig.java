package com.atguigu.springcloud.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationContextConfig {

    /**
     * RestTemplate是spring提供的远程调用其他http服务接口的工具，是httpClient的封装
     * @return
     */
    @Bean
//    @LoadBalanced   //负载均衡机制，不加的话就不知道去找哪个服务，就会报错
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
