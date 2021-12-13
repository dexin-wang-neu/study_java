package cn.neu.aclservice.security.config;

import cn.neu.aclservice.security.filter.TokenAuthenticationFilter;
import cn.neu.aclservice.security.filter.TokenLoginFilter;
import cn.neu.aclservice.security.security.DefaultPasswordEncode;
import cn.neu.aclservice.security.security.TokenLogoutHandler;
import cn.neu.aclservice.security.security.TokenManager;
import cn.neu.aclservice.security.security.UnAuthEntryPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@Data
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    private DefaultPasswordEncode defaultPasswordEncoder;
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.exceptionHandling()
                .authenticationEntryPoint(new UnAuthEntryPoint())   //没有权限时的处理类
                .and().csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/admin/acl/index/logout")
                .addLogoutHandler(new TokenLogoutHandler(tokenManager,redisTemplate)).and() //退出执行的处理器
                .addFilter(new TokenLoginFilter(tokenManager,redisTemplate,authenticationManager()))    //添加自定义的认证过滤器
                .addFilter(new TokenAuthenticationFilter(authenticationManager(),tokenManager,redisTemplate)).httpBasic();  //添加自定义的授权过滤器

    }

    //调用userDetailsService和密码处理
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    /**
     * 不进行认证的路径，可以直接访问
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/api/**");
    }
}
