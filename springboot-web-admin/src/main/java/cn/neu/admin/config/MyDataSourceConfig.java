package cn.neu.admin.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 通过druid-spring-boot-starter 可以简化操作，这个配置类就不用自己写了
 */
@Deprecated
//@Configuration
public class MyDataSourceConfig {

    //@ConditionalOnMissingBean(DataSource.class) 默认的自动配置是判断容器中没有才会配置数据源
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        //配置数据源信息
//        druidDataSource.setUrl();
//        druidDataSource.setPassword();
//        druidDataSource.setUsername();
        //可以在配置文件里配置
//        druidDataSource.setFilters("stat,wall");//加入监控功能,防火墙功能

        return druidDataSource;
    }

    /**
     * webStatFilter  用于采集web-jdbc管理监控的数据
     */
    @Bean
    public FilterRegistrationBean  webStatFilter(){
        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(webStatFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));     //拦截的路径
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"); //排除一些路径
        return filterRegistrationBean;
    }

    /**
     * 配置Drudi的监控页
     * statViewServlet : 名字是Druid官方默认这么写 的
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        StatViewServlet statViewServlet = new StatViewServlet();

        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(statViewServlet, "/druid/*");
        //添加监控页的账号密码
        registrationBean.addInitParameter("loginUsername","admin");
        registrationBean.addInitParameter("loginPassword","123456");


        return registrationBean;
    }
}
