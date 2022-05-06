package com.atguigu.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashboardMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardMain9001.class,args);
        System.out.println("HystrixDashboard 可视化监控应用启动；地址   http://localhost:9001/hystrix ");
        //http://localhost:8001/hystrix.stream  对8001的监控地址

    }
}
