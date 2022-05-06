package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.IMessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SendMessageController {
    @Resource
    private IMessageProvider messageProvider;

    /**
     * 每调用一次就像消息中间件发送一个消息
     * @return
     */
    @GetMapping("/sendMessage")
    public String sendMessage(){
        return messageProvider.send();
    }
}
