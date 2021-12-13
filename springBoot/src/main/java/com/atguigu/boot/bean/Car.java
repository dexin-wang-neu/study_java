package com.atguigu.boot.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 只有在容器中的组件，才会有Springboot提供的功能
 * @EnableConfigurationProperties(Car.class)在myconfig配置文件中指定了Car的属性绑定就可以去掉Componnet
 */
//@Component
@ConfigurationProperties(prefix = "mycar")//绑定application.properties配置文件以mycar前缀开头的属性
public class Car {
    private String brand;
    private Integer price;


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", price=" + price +
                '}';
    }
}
