package cn.neu.admin.controller;

import cn.neu.admin.bean.User;
import cn.neu.admin.exception.UserTooManyExceptionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class TableController {

    @GetMapping("/basic_table")
    public String basic_table(){
        int i = 10/0;  //自定义了500异常
        return "table/basic_table";
    }
    @GetMapping("/dynamic_table")
    public String dynamic_table(Model model){
        //表格内容的遍历
        List<User> users = Arrays.asList(new User("张三", "123456"),
                new User("lisi", "111111"),
                new User("haha", "aaaaaa"),
                new User("hehe", "dddddd"));
        if (users.size() > 3){
            throw new UserTooManyExceptionHandler();
        }
        model.addAttribute("users",users);
        return "table/dynamic_table";
    }
    @GetMapping("/editable_table")
    public String editable_table(){
        return "table/editable_table";
    }
    @GetMapping("/pricing_table")
    public String pricing_table(){
        return "table/pricing_table";
    }
    @GetMapping("/responsive_table")
    public String responsive_table(){
        return "table/responsive_table";
    }
}
