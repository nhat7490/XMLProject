<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Trang chủ</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" media="screen" href="css/index.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="../css/w3.css"/>
    <link href="https://unpkg.com/ionicons@4.2.4/dist/css/ionicons.min.css" rel="stylesheet">
</head>

<body>
<c:set value="${sessionScope.USER}" var="user"/>
<div class="wrapper">
    <div class="header">
        <div class="header-items">
            <c:if test="${user.role eq 1}">
                <div class="header-item">
                    <a href="/admin">
                        <i class="icon ion-md-code-download"></i>
                        <span>Crawler</span>
                    </a>
                </div>
            </c:if>
            <c:if test="${not empty user}">
                <div class="header-item">
                    <a href="/log-out">
                        <i class="icon ion-ios-log-out"></i>
                        <span>Đăng Xuất</span>
                    </a>
                </div>
                <div class="header-item">
                    <span>Xin Chào, ${user.firstname}</span>
                </div>
            </c:if>
            <c:if test="${empty user}">
                <div class="header-item">
                    <a href="/">
                        <i class="icon ion-ios-log-in"></i>
                        <span>Đăng Nhập</span>
                    </a>
                </div>
            </c:if>

        </div>
    </div>
    <div class="banner">
        <img src="../image/incredible.jpg"/>
        <div class="search">
            <form action="/tim-kiem">
                <input type="text" name="search" placeholder="Nhập tên phim bạn cần tìm ..."/>
                <input type="submit" value="Search">
            </form>
        </div>
    </div>
    <c:set var="movieList" value="${requestScope.RESULT}"/>
    <c:if test="${not empty user}">
        <div class="body">
            <div class="movie-list">

                <c:set var="xmlDoc" value="${requestScope.RESULT}"/>
                <c:import charEncoding="UTF-8" url="/xslt/movie-list.xsl" var="xsltDoc"/>
                <x:transform xml="${xmlDoc}" xslt="${xsltDoc}"/>


            </div>
        </div>
        <div class="footer">
            <div class="pagination">
                <c:set var="totalPage" value="${requestScope.PAGE}"/>
                <c:set var="currentPage" value="${requestScope.CURRENTPAGE}"/>

                    <%--current page greater than or equal 7--%>
                <c:if test="${currentPage lt 7}">
                    <div class="prev">
                        Trang Trước
                    </div>
                    <div class="pages">
                        <c:forEach var="i" begin="1" end="7">
                            <div class="page">
                                <a href="/trang-chu?page=${i}">${i}</a>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="next">
                        Trang Sau
                    </div>
                </c:if>

                    <%--current page greater than or equal 7--%>
                <c:if test="${currentPage ge 7}">

                    <div class="prev">
                        <a href="/trang-chu?page=${currentPage-1}">Trang Trước</a>
                    </div>
                    <div class="pages">
                        <c:forEach var="i" begin="${currentPage - 3}" end="${currentPage + 3}">
                            <div class="page">
                                <a href="/trang-chu?page=${i}">${i}</a>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="next">
                        <a href="/trang-chu?page=${currentPage+1}">Trang Sau</a>
                    </div>

                </c:if>

            </div>
        </div>
    </c:if>
    <c:if test="${empty user}">
        <div class="body w3-container w3-center">
            <div class="alert">
                <h3><strong>Bạn cần phải đăng nhập</strong></h3>
            </div>
        </div>
    </c:if>
</div>
</body>

</html>