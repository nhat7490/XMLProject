<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<head>
    <title>Thông tin</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700" rel="stylesheet">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700" rel="stylesheet">

    <style>

        body {
            font-family: 'open sans';
            overflow-x: hidden;
        }

        img {
            max-width: 100%;
        }

        .preview {
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-orient: vertical;
            -webkit-box-direction: normal;
            -webkit-flex-direction: column;
            -ms-flex-direction: column;
            flex-direction: column;
        }

        @media screen and (max-width: 996px) {
            .preview {
                margin-bottom: 20px;
            }
        }

        .preview-pic {
            -webkit-box-flex: 1;
            -webkit-flex-grow: 1;
            -ms-flex-positive: 1;
            flex-grow: 1;
        }

        .preview-thumbnail.nav-tabs {
            border: none;
            margin-top: 15px;
        }

        .preview-thumbnail.nav-tabs li {
            width: 18%;
            margin-right: 2.5%;
        }

        .preview-thumbnail.nav-tabs li img {
            max-width: 100%;
            display: block;
        }

        .preview-thumbnail.nav-tabs li a {
            padding: 0;
            margin: 0;
        }

        .preview-thumbnail.nav-tabs li:last-of-type {
            margin-right: 0;
        }

        .tab-content {
            overflow: hidden;
        }

        .tab-content img {
            width: 100%;
            -webkit-animation-name: opacity;
            animation-name: opacity;
            -webkit-animation-duration: .3s;
            animation-duration: .3s;
        }

        .card {
            margin-top: 50px;
            background: #eee;
            padding: 3em;
            line-height: 1.5em;
        }

        @media screen and (min-width: 997px) {
            .wrapper {
                display: -webkit-box;
                display: -webkit-flex;
                display: -ms-flexbox;
                display: flex;
            }
        }

        .details {
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-orient: vertical;
            -webkit-box-direction: normal;
            -webkit-flex-direction: column;
            -ms-flex-direction: column;
            flex-direction: column;
        }

        .colors {
            -webkit-box-flex: 1;
            -webkit-flex-grow: 1;
            -ms-flex-positive: 1;
            flex-grow: 1;
        }

        .product-title, .price, .sizes, .colors {
            text-transform: UPPERCASE;
            font-weight: bold;
        }

        .checked, .price span {
            color: #ff9f1a;
        }

        .product-title, .rating, .product-description, .price, .vote, .sizes {
            margin-bottom: 15px;
        }

        .product-title {
            margin-top: 0;
        }

        .size {
            margin-right: 10px;
        }

        .size:first-of-type {
            margin-left: 40px;
        }

        .color {
            display: inline-block;
            vertical-align: middle;
            margin-right: 10px;
            height: 2em;
            width: 2em;
            border-radius: 2px;
        }

        .color:first-of-type {
            margin-left: 20px;
        }

        .add-to-cart, .like {
            background: #ff9f1a;
            padding: 1.2em 1.5em;
            border: none;
            text-transform: UPPERCASE;
            font-weight: bold;
            color: #fff;
            -webkit-transition: background .3s ease;
            transition: background .3s ease;
        }

        .add-to-cart:hover, .like:hover {
            background: #b36800;
            color: #fff;
        }

        .not-available {
            text-align: center;
            line-height: 2em;
        }

        .not-available:before {
            font-family: fontawesome;
            content: "\f00d";
            color: #fff;
        }

        .orange {
            background: #ff9f1a;
        }

        .green {
            background: #85ad00;
        }

        .blue {
            background: #0076ad;
        }

        .tooltip-inner {
            padding: 1.3em;
        }

        @-webkit-keyframes opacity {
            0% {
                opacity: 0;
                -webkit-transform: scale(3);
                transform: scale(3);
            }
            100% {
                opacity: 1;
                -webkit-transform: scale(1);
                transform: scale(1);
            }
        }

        @keyframes opacity {
            0% {
                opacity: 0;
                -webkit-transform: scale(3);
                transform: scale(3);
            }
            100% {
                opacity: 1;
                -webkit-transform: scale(1);
                transform: scale(1);
            }
        }

    </style>
</head>

<c:set value="${requestScope.ID}" var="id"/>
<body onload="loadXmlDoc(${id})">
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
<div class="container">
    <c:if test="${not empty user}">
        <div class="card">
            <div class="container-fliud">
                <div class="wrapper row">
                    <div class="preview col-md-6">

                        <div class="preview-pic tab-content">
                            <div class="tab-pane active" id="pic-1">
                                <img id="movieImage"/>
                            </div>
                        </div>

                    </div>
                    <div class="details col-md-6">
                        <h2 id="movieTitle" class="product-title">Ready Player One</h2>
                        <h5 id="movieDirector" class="price"> Đạo Diễn:
                        </h5>
                        <h5 id="movieActor" class="price"> Diễn Viên:
                        </h5>
                        <h5 id="movieYear" class="price"> Năm:
                        </h5>
                        <h5 id="vkoolRate" class="price"> Điểm Vkool:
                        </h5>
                        <h5 id="phimmoiRate" class="price"> Điểm Phimmoi:
                        </h5>
                        <div id="divLink" class="action text-center">
                            <a id="phimmoiLink" class="btn btn-info" role="button"><h5>Phimmoi</h5></a>
                            <a id="vkoolLink" class="btn btn-info" role="button"><h5>Vkool</h5></a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
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
                <span class="tm-current-year">2018</span> Your Company Name - Web Design:
                <a href="http://tooplate.com" class="tm-text-gray">Tooplate</a>
            </p>
        </div>
    </footer>
</div>

<script>

    function loadXmlDoc(id) {
        var url = 'http://localhost:8080/movie-detail/' + id;
        var xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function () {
            if (xhttp.readyState === 4 && xhttp.status === 200) {
                console.log(xhttp.responseText);

                // Clean xmlString
                var xmlString = xhttp.responseText;
                xmlString = xmlString.replace(new RegExp(' xmlns(:.*?)?=(".*?")'), '');
                console.log(xmlString);

                // Create parser
                var parser = new DOMParser();
                // Create xmlDoc
                var xmlDoc = parser.parseFromString(xmlString, "text/xml");

                // Get model NodeSet
                var movies = xmlDoc.evaluate('//movie', xmlDoc, null, XPathResult.ANY_TYPE, null);

                //Get single movie
                var movie = movies.iterateNext();

                //get poster
                var imgTag = document.getElementById("movieImage");
                var imgLink = xmlDoc.evaluate('poster_link', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(imgLink);
                imgTag.src = imgLink;

                //get Title
                var titleTag = document.getElementById("movieTitle");
                var title = xmlDoc.evaluate('title', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(title);
                titleTag.innerHTML = title;

                //get directors
                var directorTag = document.getElementById("movieDirector");
                var director = xmlDoc.evaluate('director', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(director);
                directorTag.append(director);

                //get actors
                var actorTag = document.getElementById("movieActor");
                var actor = xmlDoc.evaluate('actors', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(actor);
                actorTag.append(actor);

                //get year
                var yearTag = document.getElementById("movieYear");
                var year = xmlDoc.evaluate('year_public', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(year);
                yearTag.append(year);

                //get vkool rate
                var vkoolRateTag = document.getElementById("vkoolRate");
                var vkoolRate = xmlDoc.evaluate('vkool_rate', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(vkoolRate);
                vkoolRateTag.append(vkoolRate);

                //get phimmoi rate
                var phimmoiRateTag = document.getElementById("phimmoiRate");
                var phimmoiRate = xmlDoc.evaluate('bilu_rate', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(phimmoiRate);
                phimmoiRateTag.append(phimmoiRate);

                //get vkool link
                var vkoolLinkTag = document.getElementById("vkoolLink");
                var vkoolLink = xmlDoc.evaluate('vkool_link', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(vkoolRate);
                if (vkoolLink) {
                    vkoolLinkTag.href = vkoolLink;
                } else {
                    document.getElementById("divLink").removeChild(vkoolLinkTag);
                }

                //get phimmoi link
                var phmmoiLinkTag = document.getElementById("phimmoiLink");
                var phimmoiLink = xmlDoc.evaluate('bilu_link', movie, null, XPathResult.STRING_TYPE, null).stringValue;
                console.log(phimmoiLink);
                if (phimmoiLink) {
                    phmmoiLinkTag.href = phimmoiLink;
                } else {
                 document.getElementById("divLink").removeChild(phmmoiLinkTag);
                }

            }
        };

        xhttp.open("GET", url, true);
        xhttp.send();
    }
</script>
</body>