<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
<h1>Login</h1>

<div style="text-align: center">
    <%--  这里表单表示的意思：以post方式提交表单，到login请求  --%>
    <form action="${pageContext.request.contextPath}/login" method="post">
        username:<input type="text" name="username"><br>
        password:<input type="password" name="password"><br>
        <input type="checkbox" name="hobbys" value="girl">girl
        <input type="checkbox" name="hobbys" value="code">code
        <input type="checkbox" name="hobbys" value="sing">sing
        <input type="checkbox" name="hobbys" value="move">move
        <br>
        <input type="submit">
    </form>
</div>
</body>
</html>
