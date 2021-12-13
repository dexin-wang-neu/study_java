package com.atguigu.boot.controller;

import com.atguigu.boot.bean.Car;
import com.atguigu.boot.bean.Person2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@ResponseBody//说明是直接返回给浏览器的
//@Controller

@RestController
public class HelloController {
    @Autowired
    Car car;
    @Autowired
    Person2 person2;


    @RequestMapping("/car")
    public Car car(){
        return car;
    }
//    @ResponseBody
    @RequestMapping("/hello")
    public String handle01(){
        return "Hello SpringBoot 2";
    }

    @RequestMapping("/person2")
    public Person2 person2(){
        return person2;
    }
}
