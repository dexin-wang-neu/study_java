package com.atguigu.boot.bean;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 使用ymal配置文件绑定属性
 *
 */
@Component
@ConfigurationProperties(prefix = "person2")
@Data//lombok注解
@ToString
public class Person2 {
    private String userName;
    private Boolean boss;
    private Date birth;
    private Integer age;
    private Pet2 pet;
    private String[] interests;
    private List<String> animal;
    private Map<String, Object> score;
    private Set<Double> salarys;
    private Map<String, List<Pet>> allPets;
}
