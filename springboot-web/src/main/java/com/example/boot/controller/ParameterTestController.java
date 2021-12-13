package com.example.boot.controller;

import com.example.boot.bean.Person;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ParameterTestController {

    /**
     * 数据绑定：页面提交的请求数据（get,post）都可以和和对象属性绑定
     * @param person
     * @return
     */
    @PostMapping("/saveuser")
    public Person saveuser(Person person){
        return person;
    }

    @GetMapping("/car/{id}/owner/{username}")
    public Map<String,Object> getCar(@PathVariable("id") Integer id,
                                     @PathVariable("username") String name,
                                     @PathVariable Map<String,String> pv,
                                     @RequestHeader("User-Agent") String userAgent,
                                     @RequestHeader Map<String,String> header,
                                     @RequestParam("age") Integer age,
                                     @RequestParam("interests") List<String> interests,
                                     @RequestParam Map<String,String> params,
                                     @CookieValue("Idea-43867c75") String _ga,
                                     @CookieValue("Idea-43867c75") Cookie cookie
                                     ){
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("name",name);
        map.put("pv",pv);
        map.put("userAgent",userAgent);
        map.put("headers",header);
        map.put("age",age);
        map.put("interests",interests);
        map.put("params",params);
        map.put("_ga",_ga);
        System.out.println("cookieName="+cookie.getName()+", cookieValue:"+cookie.getValue());
        return map;
    }

    @PostMapping("/save")
    public Map postMethod(@RequestBody String content){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content",content);
        return map;
    }

    //1.语法  /cars/sell;low=34;brand=byd,audi,yd
    //2.  springboot默认是禁用矩阵变量的功能
    //       手动开启：原理。对于路径的处理使用UrlPathHelper进行解析
    //        removeSemicolonContent（移除分号内容）    支持矩阵变量
    @GetMapping("/cars/{path}")
    public Map carsSell(@MatrixVariable("low") Integer low,
                        @MatrixVariable("brand") List<String> brand,
                        @PathVariable("path") String path){
        HashMap<String, Object> map = new HashMap<>();
        map.put("low",low);
        map.put("brand",brand);
        map.put("path",path);
        return map;
    }


}
