<!DOCTYPE html>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=750, user-scalable=yes">

    <title>Decouple Toolkit</title>
    <link rel="stylesheet" href="/css/base.css" type="text/css" />
    <script src="/js/base.js"></script>
    <link rel="stylesheet" href="/css/manager.css" type="text/css" />
    <script src="/js/jquery-3.3.1.min.js"></script>
    <script src="/js/manager.js"></script>
    <link rel="stylesheet" href="/css/jsonkit.css" type="text/css" />
    <script src="/js/jsonkit.js"></script>
</head>
<body>
<div class="container">
    <p class="top">
        API：<a id="goto" target="_blank" href=""></a>
    </p>
    <div class="fileManager">
        <input class="file" type="file" />
    </div>
    <div class="form">
        <p class="message">
            <span class="status">状态：<span id="status">新建</span></span>
            <span id="error"></span>
        </p>
        <div id="data" class="data Canvas" name="data"
             contenteditable="true"
             placeholder="填写或选择文件json内容提交，用以保存">
        </div>
        <input class="submit" type="button" value="提交" />
    </div>
</div>
<script>
    loadData('<%=request.getAttribute("data")==null?"":request.getAttribute("data").toString().trim()%>');
</script>
</body>
</html>