package cn.neu.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


//@MapperScan("cn.neu.admin.mapper")  //设置mapper包位置，这样在mapper文件里就不用写@Mapper注解
@ServletComponentScan(basePackages = "cn.neu")  //开启Servlet扫描，配置扫描的包
@SpringBootApplication
public class SpringbootWebAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebAdminApplication.class, args);
        System.out.println("运行");
    }

}
