<%@ page import="com.idearfly.decouple.vo.FileObject" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=750, user-scalable=yes">

    <title>Decouple Toolkit</title>
    <link rel="stylesheet" href="/css/base.css" type="text/css" />
    <script src="/js/base.js"></script>
    <link rel="stylesheet" href="/css/directory.css" type="text/css" />
    <script src="/js/jquery-3.3.1.min.js"></script>
    <script src="/js/directory.js"></script>
</head>
<body>
<div class="container">
    <ul class="directory">
        <% if (!request.getServletPath().equals("/manager")) { %>
        <li class="file" onclick="prev()">
            <img src="/images/directory.svg" class="icon">
            <span class="name">..</span>
        </li>
        <% } %>
    <% List<FileObject> fileObjectList = (List<FileObject>) request.getAttribute("listFiles");
        for (FileObject fileObject: fileObjectList) { %>
        <li class="file" onclick="entry('<%=fileObject.getName()%>')">
            <% if (fileObject.getType()) { %>
                <img src="/images/file.svg" class="icon" />
            <% } else { %>
                <img src="/images/directory.svg" class="icon" />
            <% } %>
            <span class="name"><%=fileObject.getName()%></span>
        </li>
    <% } %>
    </ul>
</div>
</body>
</html>