package com.atguigu.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 注意：新定义的负载均衡类，不能放到与主函数相同的包下（因为不能被ComponentScan扫描到，否则就会被所有的ribbon共享），所以新建了一个myrule包。
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule(){
        return new RandomRule();//定义为随机
    }
}
