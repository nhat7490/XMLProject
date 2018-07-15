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
        #fountainG {
            position: relative;
            width: 234px;
            height: 28px;
            margin: auto;
        }

        .fountainG {
            position: absolute;
            top: 0;
            background-color: white;
            width: 28px;
            height: 28px;
            animation-name: bounce_fountainG;
            -o-animation-name: bounce_fountainG;
            -ms-animation-name: bounce_fountainG;
            -webkit-animation-name: bounce_fountainG;
            -moz-animation-name: bounce_fountainG;
            animation-duration: 1.5s;
            -o-animation-duration: 1.5s;
            -ms-animation-duration: 1.5s;
            -webkit-animation-duration: 1.5s;
            -moz-animation-duration: 1.5s;
            animation-iteration-count: infinite;
            -o-animation-iteration-count: infinite;
            -ms-animation-iteration-count: infinite;
            -webkit-animation-iteration-count: infinite;
            -moz-animation-iteration-count: infinite;
            animation-direction: normal;
            -o-animation-direction: normal;
            -ms-animation-direction: normal;
            -webkit-animation-direction: normal;
            -moz-animation-direction: normal;
            transform: scale(.3);
            -o-transform: scale(.3);
            -ms-transform: scale(.3);
            -webkit-transform: scale(.3);
            -moz-transform: scale(.3);
            border-radius: 19px;
            -o-border-radius: 19px;
            -ms-border-radius: 19px;
            -webkit-border-radius: 19px;
            -moz-border-radius: 19px;
        }

        #fountainG_1 {
            left: 0;
            animation-delay: 0.6s;
            -o-animation-delay: 0.6s;
            -ms-animation-delay: 0.6s;
            -webkit-animation-delay: 0.6s;
            -moz-animation-delay: 0.6s;
        }

        #fountainG_2 {
            left: 29px;
            animation-delay: 0.75s;
            -o-animation-delay: 0.75s;
            -ms-animation-delay: 0.75s;
            -webkit-animation-delay: 0.75s;
            -moz-animation-delay: 0.75s;
        }

        #fountainG_3 {
            left: 58px;
            animation-delay: 0.9s;
            -o-animation-delay: 0.9s;
            -ms-animation-delay: 0.9s;
            -webkit-animation-delay: 0.9s;
            -moz-animation-delay: 0.9s;
        }

        #fountainG_4 {
            left: 88px;
            animation-delay: 1.05s;
            -o-animation-delay: 1.05s;
            -ms-animation-delay: 1.05s;
            -webkit-animation-delay: 1.05s;
            -moz-animation-delay: 1.05s;
        }

        #fountainG_5 {
            left: 117px;
            animation-delay: 1.2s;
            -o-animation-delay: 1.2s;
            -ms-animation-delay: 1.2s;
            -webkit-animation-delay: 1.2s;
            -moz-animation-delay: 1.2s;
        }

        #fountainG_6 {
            left: 146px;
            animation-delay: 1.35s;
            -o-animation-delay: 1.35s;
            -ms-animation-delay: 1.35s;
            -webkit-animation-delay: 1.35s;
            -moz-animation-delay: 1.35s;
        }

        #fountainG_7 {
            left: 175px;
            animation-delay: 1.5s;
            -o-animation-delay: 1.5s;
            -ms-animation-delay: 1.5s;
            -webkit-animation-delay: 1.5s;
            -moz-animation-delay: 1.5s;
        }

        #fountainG_8 {
            left: 205px;
            animation-delay: 1.64s;
            -o-animation-delay: 1.64s;
            -ms-animation-delay: 1.64s;
            -webkit-animation-delay: 1.64s;
            -moz-animation-delay: 1.64s;
        }

        @keyframes bounce_fountainG {
            0% {
                transform: scale(1);
                background-color: rgb(0, 0, 0);
            }

            100% {
                transform: scale(.3);
                background-color: rgb(255, 255, 255);
            }
        }

        @-o-keyframes bounce_fountainG {
            0% {
                -o-transform: scale(1);
                background-color: rgb(0, 0, 0);
            }

            100% {
                -o-transform: scale(.3);
                background-color: rgb(255, 255, 255);
            }
        }

        @-ms-keyframes bounce_fountainG {
            0% {
                -ms-transform: scale(1);
                background-color: rgb(0, 0, 0);
            }

            100% {
                -ms-transform: scale(.3);
                background-color: rgb(255, 255, 255);
            }
        }

        @-webkit-keyframes bounce_fountainG {
            0% {
                -webkit-transform: scale(1);
                background-color: rgb(0, 0, 0);
            }

            100% {
                -webkit-transform: scale(.3);
                background-color: rgb(255, 255, 255);
            }
        }

        @-moz-keyframes bounce_fountainG {
            0% {
                -moz-transform: scale(1);
                background-color: rgb(0, 0, 0);
            }

            100% {
                -moz-transform: scale(.3);
                background-color: rgb(255, 255, 255);
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
                    <h3 >
                        Bạn cần quyền Admin để truy cập trang này
                    </h3>
                    <a href="/">
                        <button type="button" class="btn-primary btn-lg">Login</button>
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

    var status = false;

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