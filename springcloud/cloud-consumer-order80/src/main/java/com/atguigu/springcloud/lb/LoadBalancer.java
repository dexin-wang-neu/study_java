package com.atguigu.springcloud.lb;


import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * 自定义的负载均衡类
 */
public interface LoadBalancer {
    /**
     * 获取注册的提供服务的对象
     * @param serviceInstances
     * @return
     */
    ServiceInstance instance(List<ServiceInstance> serviceInstances);

}
