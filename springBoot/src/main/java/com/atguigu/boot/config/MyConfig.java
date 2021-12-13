package com.atguigu.boot.config;

import ch.qos.logback.core.db.DBHelper;
import com.atguigu.boot.bean.Car;
import com.atguigu.boot.bean.Person;
import com.atguigu.boot.bean.Person2;
import com.atguigu.boot.bean.Pet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * 1.配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单例的F
 * 2.配置类本身也是组件
 * 3.proxyBeanMethods 默认为true
 *      FULL（proxyBeanMethods = true）,
 *      Lite（proxyBeanMethods = false）
 *      解决组件依赖场景
 * 4.@Import({Person.class, DBHelper.class})
 *      给容器中自动创建出这两个类型的组件,默认组件的名字就是全类名
 *
 * 5. @ImportResource("classpath:beans.xml")//导入spring的配置文件
 */
@Import({Person.class, DBHelper.class})
@Configuration(proxyBeanMethods = true)//告诉springboot这是一个配置类 === 配置文件
//@ConditionalOnBean(name = "Tom")//容器中有Tom组件的时候才注册
@ConditionalOnMissingBean(name = "Tom")//容器中有Tom组件的时候才注册
@ImportResource("classpath:beans.xml")//导入spring的配置文件
@EnableConfigurationProperties({Car.class, Person2.class})
//1.开启car的属性配置功能
//2.把这个car组件自动注册到容器中
public class MyConfig {
    /**
     * 外部无论对配置类中的这个组件注册的方法调用多少次，获得的都是单例对象
     * @return
     */
//    @ConditionalOnBean(name = "Tom")//容器中有Tom组件的时候才注册
    @Bean//给容器中添加组件，以方法名作为组件的id,返回类型就是组件类型。返回的值，就是组件在容器中的实例。
    public Person user01(){
        Person zhangsan = new Person("张三", 18);
        //Person组件依赖了pet组件
        zhangsan.setPet(tomcatPet());
        return zhangsan;
    }

    @Bean("Tom2")
    public Pet tomcatPet(){
        return new Pet("tomcat");
    }
}
