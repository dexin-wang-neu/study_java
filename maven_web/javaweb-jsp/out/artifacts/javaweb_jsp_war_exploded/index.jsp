<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2021/6/13
  Time: 22:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>

<%--  JSP表达式
 作用：用来将程序的输出，输出到客户端
 <%= 变量或表达式%>
 --%>
<%= new java.util.Date()  %>
<hr>

<%--  jsp脚本片段 --%>
<%
    int sum = 0;
    for (int i = 0; i < 100; i++) {
        sum += i;
    }
    out.println("<h1>SUM=" + sum + "<h1/>");
%>
<%
    int x = 10;
    out.println(x);
%>
<p>这是一个JSP文档</p>
<%
    int y = 2;
    out.println(y);
%>
<hr>

<%-- 在代码中嵌入HTML元素 --%>
<%
    for (int i = 0; i < 5; i++) {
%>
<h1>Hello,World  <%=i%>
</h1>
<%
    }
%>
<%-- EL表达式 --%>
<% for (int i = 0; i < 5; i++) { %>
<h1> Hello,World ${i}</h1>
<% } %>

<%!
    static {
        System.out.println("Loading Servlet");
    }

    private int globalVar = 0;

    public void test() {
        System.out.println("进入了TEST方法");
    }
%>
</body>
</html>
