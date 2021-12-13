package com.atguigu.boot.bean;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 使用yml文件配置属性
 */

//@Component
//@ConfigurationProperties(prefix = "pet2")


@Data
@ToString
public class Pet2 {
        private String name;
        private Double weight;
}
