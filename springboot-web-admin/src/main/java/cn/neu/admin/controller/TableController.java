package cn.neu.admin.controller;

import cn.neu.admin.bean.User;
import cn.neu.admin.bean.UserMybatis;
import cn.neu.admin.exception.UserTooManyExceptionHandler;
import cn.neu.admin.service.UserMybatisService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class TableController {

    @Autowired
    UserMybatisService userMybatisService;

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, @RequestParam(value = "pn",defaultValue = "1") String pn, RedirectAttributes re){
        userMybatisService.removeById(id);
        re.addAttribute("pn",pn);
        return "redirect:/dynamic_table";
    }

    @GetMapping("/basic_table")
    public String basic_table(){
        int i = 10/0;  //自定义了500异常
        return "table/basic_table";
    }

    /**
     * 改接口功能式测试自定义异常处理，为了测试mybatis-plus，现或略异常
     * @param model
     * @return
     */
//    @GetMapping("/dynamic_table")
//    public String dynamic_table(Model model){
//        //表格内容的遍历
//        List<User> users = Arrays.asList(new User("张三", "123456"),
//                new User("lisi", "111111"),
//                new User("haha", "aaaaaa"),
//                new User("hehe", "dddddd"));
//        if (users.size() > 3){
//            throw new UserTooManyExceptionHandler();
//        }
//        model.addAttribute("users",users);
//        return "table/dynamic_table";
//    }


    /**
     * 使用mybatis-plus 完成CRUD
     * @param model
     * @return
     */
    @GetMapping("/dynamic_table")
    public String dynamic_table(Model model, @RequestParam(value = "pn",defaultValue = "1") Integer pn){
        //从数据库种查出user_mybatis中的用户
        List<UserMybatis> list = userMybatisService.list();
        model.addAttribute("users",list);
        //分页查询数据
        Page<UserMybatis> page = new Page<>(pn,2);
        Page<UserMybatis> result = userMybatisService.page(page, null);
        model.addAttribute("page",result);
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
