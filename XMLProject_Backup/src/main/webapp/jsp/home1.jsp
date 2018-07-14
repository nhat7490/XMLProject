<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Trang chủ</title>
    <!-- load CSS -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:300,400">
    <!-- Google web font "Open Sans" -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <!-- https://getbootstrap.com/ -->
    <link rel="stylesheet" href="css/fontawesome-all.min.css">
    <!-- Font awesome -->
    <link rel="stylesheet" href="css/tooplate-style.css">
    <!-- Templatemo style -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <style type="text/css">
        .tm-welcome-section {
            background-image: url('../image/incredible.jpg');
            height: 560px;
        }

        .form-inline {
            background-color: white;
        }

        .tm-album-col {
            margin-bottom: 2%;
        }

        .text-uppercase {
            background-color: white;
            color: white;
        }

        .img-fluid {
            width: 260px;
            height: 390px;
        }

        .button {
            font: bold;
            font-size: 24px;
            background: transparent;
            border: none;
            font-family: Arial, Helvetica, sans-serif;
            transition: 0.3s
        }

        .button:hover {
            transform: scale(1.15);
        }

        h2 {
            font-size: 30px;
        }
    </style>

</head>

<body>
<c:set value="${sessionScope.USER}" var="user"/>

<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"></a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"></li>
            <li></li>
            <li></li>
        </ul>
        <ul class="nav navbar-nav">
            <li class="active"></li>
            <li></li>
            <li></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <c:if test="${user.role eq 1}">
                <li><a href="/admin"><span class="glyphicon glyphicon-circle-arrow-down"></span> Crawler</a></li>
            </c:if>
            <c:if test="${not empty user}">
                <li><a href="/log-out"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                <li><a href="#"><span></span> Welcome, ${user.firstname}</a></li>
            </c:if>
            <c:if test="${empty user}">
                <li><a href="/"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
            </c:if>
        </ul>
    </div>
</nav>
<div class="tm-main">

    <div class="tm-welcome-section">
        <div class="container tm-navbar-container">
        </div>
    </div>

    <div class="container text-center">
        <div class="tm-search-form-container">
            <form action="/tim-kiem" class="form-inline tm-search-form"
                  style="background-color: #1C689A">
                <div class="text-uppercase tm-new-release" style="background-color: #1C689A"></div>
                <div class="tm-search-box form-group">
                    <input type="text" name="search" class="form-control tm-search-input"
                           placeholder="Type your keyword ...">
                    <input type="submit" value="Search" class="form-control tm-search-submit">
                </div>
                <div class="form-group tm-advanced-box">

                </div>

            </form>
        </div>
        <c:set var="movieList" value="${requestScope.RESULT}"/>
        <c:if test="${not empty user}">
            <c:if test="${not empty movieList}">
                <div id="movieList">
                    <c:set var="xmlDoc" value="${requestScope.RESULT}"/>
                    <c:import charEncoding="UTF-8" url="/xslt/movie-list.xsl" var="xsltDoc"/>
                    <x:transform xml="${xmlDoc}" xslt="${xsltDoc}"/>

                </div>
                <ul class="pagination pagination-lg">
                    <c:set var="totalPage" value="${requestScope.PAGE}"/>
                    <c:set var="currentPage" value="${requestScope.CURRENTPAGE}"/>

                        <%--<c:out value="${currentPage}"/>--%>
                        <%--<c:out value="${totalPage}"/>--%>

                        <%--current page less than 7--%>
                    <c:if test="${currentPage lt 7}">
                        <li><a>Previous</a></li>
                        <c:forEach var="i" begin="1" end="7">
                            <li><a href="/trang-chu?page=${i}">${i}</a></li>
                        </c:forEach>
                        <li><a>Next</a></li>
                    </c:if>

                        <%--current page greater than or equal 7--%>
                    <c:if test="${currentPage ge 7}">
                        <li><a href="/trang-chu?page=${currentPage-1}">Previous</a></li>
                        <c:forEach var="i" begin="${currentPage - 3}" end="${currentPage + 3}">
                            <li><a href="/trang-chu?page=${i}">${i}</a></li>
                        </c:forEach>
                        <li><a href="/trang-chu?page=${currentPage+1}">Next</a></li>
                    </c:if>

                </ul>
            </c:if>
            <c:if test="${empty movieList}">
                <div class="col-12">
                    <div class="alert alert-danger text-center">
                        Không tìm thấy thông tin bạn cần
                    </div>
                </div>
            </c:if>
        </c:if>
        <c:if test="${empty user}">
            <div class="col-12">
                <div class="alert alert-danger text-center">
                    Bạn cần phải đăng nhập
                </div>
            </div>

        </c:if>

        <footer class="row">
            <div class="col-xl-12">
                <p class="text-center p-4">Copyright &copy;
                    <span class="tm-current-year">2018</span>
                </p>
            </div>
        </footer>
    </div>
    <!-- .container -->

</div>

</body>

</html>