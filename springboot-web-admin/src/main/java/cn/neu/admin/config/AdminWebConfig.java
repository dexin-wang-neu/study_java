package cn.neu.admin.config;

import cn.neu.admin.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器使用：
 * 1.编写一个拦截器实现HandleInterceptor接口
 * 2.拦截器注册到容器中（实现WebMvcConfigurer的addInterceptors)
 * 3.指定拦截规则【如果拦截所有，静态资源也被拦截】
 */
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")     //默认拦截所有请求，这时静态资源也被拦截了
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**");//放行的请求
    }
}
