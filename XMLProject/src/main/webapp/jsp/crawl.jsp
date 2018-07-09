<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Crawler</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="css/util.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <!--===============================================================================================-->

    <style>
        .loader {
            border: 16px solid #f3f3f3;
            border-radius: 50%;
            border-top: 16px solid #3498db;
            width: 120px;
            height: 120px;
            -webkit-animation: spin 2s linear infinite; /* Safari */
            animation: spin 2s linear infinite;
        }

        /* Safari */
        @-webkit-keyframes spin {
            0% {
                -webkit-transform: rotate(0deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
            }
        }

        @keyframes spin {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(360deg);
            }
        }
    </style>
</head>

<body>

<div class="limiter">
    <div class="container-login100" style="background-image: url('../image/cinema.jpg');">
        <c:set value="${sessionScope.USER}" var="user"/>
        <c:if test="${user.role eq 1}">

            <div class="wrap-login100 p-t-30 p-b-50">
                <span class="login100-form-title p-b-41">
                    Welcome, ${user.firstname}
                </span>
                <span class="login100-form-title p-b-41">
                    Cập Nhật Thông Tin
                </span>
                <span class="login100-form-title p-b-41">
                    <a href="/crawl?option=run">
                        <button type="button" class="btn btn-success btn-lg" onclick="createLoading()">Run</button>
                    </a>
                    <a href="/crawl?option=pause">
                        <button type="button" class="btn btn-danger btn-lg" onclick="removeLoading()">Stop</button>
                    </a>
                </span>
                <span class="login100-form-title p-b-41">
                    <a href="/trang-chu">
                        <button type="button" class="btn btn-primary btn-lg">Trang Chủ</button>
                    </a>
                </span>
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

        </c:if>
        <c:if test="${empty user}">
            <div class="col-12">
                <div class="alert alert-danger text-center">
                    <h3>
                        Bạn cần quyền Admin để truy cập trang này
                    </h3>
                    <a href="/">
                        <button type="button" class="btn btn-primary btn-lg">Login</button>
                    </a>
                </div>
            </div>
        </c:if>
        <c:if test="${not empty user&&user.role ne 1}">
            <div class="col-12">
                <div class="alert alert-danger text-center">
                    <h3>
                        Bạn cần quyền Admin để truy cập trang này
                    </h3>
                    <a href="/trang-chu">
                        <button type="button" class="btn btn-primary btn-lg">Back</button>
                    </a>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script>

    function createLoading() {
        var div = document.getElementById("fountainG");
        div.style.visibility="visible";
    }

    function removeLoading() {
        var div = document.getElementById("fountainG");
        div.style.visibility="hidden";
    }
</script>

</body>

</html>