<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Crawler</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" media="screen" href="css/login.css"/>
</head>

<body>
<div class="wrapper">
    <div class="overlay">

    </div>
    <div class="welcome">
        Welcome, Nhat
    </div>
    <div class="update">
        Cập nhật thông tin
    </div>
    <div class="operation">
        <a href="/crawl?option=run">
            <button style="background-color: #4CAF50" onclick="createLoading()">Run</button>
        </a>

        <a href="/crawl?option=pause">
            <button style="background-color: red" onclick="removeLoading()">Stop</button>
        </a>
    </div>
    <div class="homepage">
        <a href="/trang-chu">
            <button>Trang Chủ</button>
        </a>
    </div>
    <div class="loading-animation">
        <span id="loading" class="login100-form-title p-b-41">
        <div id="fountainG" style="visibility: hidden">
            <div id="fountainG_1" class="fountainG"></div>
            <div id="fountainG_2" class="fountainG"></div>
            <div id="fountainG_3" class="fountainG"></div>
            <div id="fountainG_4" class="fountainG"></div>
            <div id="fountainG_5" class="fountainG"></div>
            <div id="fountainG_6" class="fountainG"></div>
            <div id="fountainG_7" class="fountainG"></div>
            <div id="fountainG_8" class="fountainG"></div>
        </div>
    </span>
    </div>
</div>
<script>
    function createLoading() {
        var div = document.getElementById("fountainG");
        div.style.visibility = "visible";
    }

    function removeLoading() {
        var div = document.getElementById("fountainG");
        div.style.visibility = "hidden";
    }
</script>
</body>

</html>