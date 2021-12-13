<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2021/6/13
  Time: 22:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@include file="common/header.jsp" %>
<h1>主页面</h1>
<%@include file="common/footer.jsp" %>

<%--  jsp标签  --%>
<jsp:include page="/common/header.jsp"></jsp:include>
<h1>网页主题</h1>
<jsp:include page="/common/footer.jsp"/>
</body>
</html>
