package cn.neu.admin.config;

import cn.neu.admin.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器使用：
 * 1.编写一个拦截器实现HandleInterceptor接口
 * 2.拦截器注册到容器中（实现WebMvcConfigurer的addInterceptors)
 * 3.指定拦截规则【如果拦截所有，静态资源也被拦截】
 *
 *
 * @EnableWebMvc  : 全面接管 springmvc
 *      1.静态资源，视图解析器，欢迎页，。。。。。，全部失效
 */
//@EnableWebMvc       //这个注解最后自定义的时候才使用，前面要看其他功能时禁用
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    /**
     * 定义静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 访问aa下的所有请求，都去classpath:/static/  下面进行匹配
         */
        registry.addResourceHandler("/aa/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")     //默认拦截所有请求，这时静态资源也被拦截了
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**","/aa/**","/sql");//放行的请求
    }

}
