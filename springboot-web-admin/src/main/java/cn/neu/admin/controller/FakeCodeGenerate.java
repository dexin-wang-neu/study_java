package cn.neu.admin.controller;

import cn.neu.admin.bean.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class FakeCodeGenerate {



    /**
     * 登录成功到主页
     * @param student
     * @param session
     * @return
     */
    @PostMapping("/generateFakeCodeInfo")
    public String main(Student student, HttpSession session, Model model){
        model.addAttribute("name",student.getUsername());
        model.addAttribute("code",student.getUsercode());
        model.addAttribute("gate",student.getGete());
        model.addAttribute("date",System.currentTimeMillis());

        //回到登录页
        return "e码通";
//        return "pass";
    }


}
