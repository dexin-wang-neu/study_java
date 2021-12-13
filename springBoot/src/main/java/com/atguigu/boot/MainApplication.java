package com.atguigu.boot;
import ch.qos.logback.core.db.DBHelper;
import com.atguigu.boot.bean.Person;
import com.atguigu.boot.bean.Pet;
import com.atguigu.boot.config.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * 主程序类
 * @SpringBootApplication：这是一个springboot应用
 */
//@SpringBootApplication
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("com.atguigu.boot")
public class MainApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
        String[] names = run.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }
//
//        Pet tom01 = run.getBean("Tom", Pet.class);
//        Pet tom02 = run.getBean("Tom", Pet.class);
//        System.out.println("组件："+(tom01 == tom02));
//
//        //如果@Configuration(proxyBeanMethods = true)，代理对象调用方法。springboot总会检查这个组件是否在容器中
//        //com.atguigu.boot.config.MyConfig$$EnhancerBySpringCGLIB$$3783a74d@6d64b553
//        MyConfig bean = run.getBean(MyConfig.class);//获取到的本身就是代理对象
//        System.out.println(bean);
//
//        Person user01 = run.getBean("user01", Person.class);
//        Pet tom = run.getBean("Tom", Pet.class);
//        System.out.println("用户的宠物："+(user01.getPet()==tom));
//
//
//        String[] beanNamesForType = run.getBeanNamesForType(Person.class);
//        System.out.println("=========");
//        for (String s : beanNamesForType) {
//            System.out.println(s);
//        }
//
//        String[] beanNamesForType1 = run.getBeanNamesForType(DBHelper.class);
//        for (String s : beanNamesForType1) {
//            System.out.println(s);
//        }

        boolean tom = run.containsBean("Tom");
        System.out.println("容器中Tom组件："+tom);

        boolean user011 = run.containsBean("user01");
        System.out.println("容器中USER01组件："+user011);

        boolean haha = run.containsBean("haha");
        System.out.println("haha:"+haha);
        boolean hehe = run.containsBean("hehe");
        System.out.println("hehe:"+hehe);

    }
}
