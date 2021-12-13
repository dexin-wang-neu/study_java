package com.example.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewTestController {
    @GetMapping("/atguigu")
    public String atguigu(Model model){
        //model中的数据，会被放到请求域中，request.setAttribute("a",aa)
        model.addAttribute("msg","你好 neu");
        model.addAttribute("link","http://www.baidu.com");
        return "success";
    }
}
