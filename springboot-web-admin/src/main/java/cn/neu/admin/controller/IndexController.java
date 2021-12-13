package cn.neu.admin.controller;

import cn.neu.admin.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    /**
     * 到登录页
     * @return
     */
    @GetMapping(value = {"/","/login"})
    public String loginPage(){
        return "login";
    }

    /**
     * 登录成功到主页
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String main(User user, HttpSession session, Model model){

        if (user.getUsername() != null && "123456".equals(user.getPassword())){
            session.setAttribute("loginUser",user);
            //登录成功重定向到html页面；防止表单重复提交
            return "redirect:/main.html";
        }else {
            model.addAttribute("msg","账号密码错误");
            //回到登录页
            return "login";
        }

    }

    /**
     * 去index页面
     * @return
     */
    @GetMapping("/main.html")
    public String mainPage(HttpSession session,Model model){
        //判断是否登录。拦截器，过滤器
        Object user = session.getAttribute("loginUser");
        if(user != null){
            return "main";
        }else {
            //回到登录页
            model.addAttribute("msg","请重新登录");
            return "login";
        }
    }
}
