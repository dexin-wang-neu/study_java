package cn.neu;

import cn.neu.pojo.Person;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class SessionDemo01 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //解决乱码问题
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");

        //得到Session
        HttpSession session = req.getSession();

        //给Session中存东西
        session.setAttribute("name", new Person("关晓彤", 24));

        //获取Session的ID
        String id = session.getId();

        //判断Session是不是新创建的
        if (session.isNew()) {
            resp.getWriter().write("session创建成功，ID:" + id);
        } else {
            resp.getWriter().write("session已经在服务器中了，ID:" + id);
        }

        //session创建的时候做了什么事情：
//        Cookie co = new Cookie("JSESSIONID", id);
//        resp.addCookie(co);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
