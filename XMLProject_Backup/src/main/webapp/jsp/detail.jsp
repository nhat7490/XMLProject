<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Thông tin</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" media="screen" href="../css/detail.css"/>
    <link href="https://unpkg.com/ionicons@4.2.4/dist/css/ionicons.min.css" rel="stylesheet">
</head>

<c:set value="${requestScope.ID}" var="id"/>
<body onload="loadXmlDoc(${id})">
<c:set value="${sessionScope.USER}" var="user"/>

<div class="wrapper">
    <div class="header">
        <div class="header-items">
            <div class="header-item">
                <i class="icon ion-md-code-download"></i>
                <span>Crawler</span>
            </div>
            <div class="header-item">
                <i class="icon ion-ios-log-out"></i>
                <span>Logout</span>
            </div>
            <div class="header-item">
                <span>Welcome, Nhat</span>
            </div>
        </div>
    </div>
    <div class="body">
        <div class="content">
            <div class="movie-image" id="movieImage">
            </div>
            <div class="movie-description">
                <span id="movieTitle"></span>
                <span id="movieDirector">Đạo Diễn: </span>
                <span id="movieActor">Diễn Viên: </span>
                <span id="movieYear">Năm: </span>
                <span id="vkoolRate">Điểm Vkool: </span>
                <span id="phimmoiRate">Điểm Phimmoi: </span>
                <div id="divLink">
                    <a id="phimmoiLink">
                        <button style="padding: 8%;">Phimmoi</button>
                    </a>
                    <a id="vkoolLink">
                        <button style="padding: 8%;">Vkool</button>
                    </a>

                </div>
            </div>
        </div>
    </div>
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

                // Create parser
                var parser = new DOMParser();
                // Create xmlDoc
                var xmlDoc = parser.parseFromString(xmlString, "application/xml");
                console.log(xmlDoc);

                //get poster
                var imgTag = document.getElementById("movieImage");
                var imgLink = xmlDoc.getElementsByTagName("poster_link")[0].textContent;
                console.log(imgLink);
                imgTag.style.background = "url('"+imgLink+"') no-repeat center";
                imgTag.style.backgroundSize = 'contain';

                //get Title
                var titleTag = document.getElementById("movieTitle");
                var title = xmlDoc.getElementsByTagName("title")[0].textContent;
                console.log(title);
                titleTag.innerHTML = title;

                //get directors
                var directorTag = document.getElementById("movieDirector");
                var director = xmlDoc.getElementsByTagName("director")[0].textContent;
                console.log(director);
                directorTag.append(director);

                //get actors
                var actorTag = document.getElementById("movieActor");
                var actor = xmlDoc.getElementsByTagName("actors")[0].textContent;
                console.log(actor);
                actorTag.append(actor);

                //get year
                var yearTag = document.getElementById("movieYear");
                var year = xmlDoc.getElementsByTagName("year_public")[0].textContent;
                console.log(year);
                yearTag.append(year);

                //get vkool rate
                var vkoolRateTag = document.getElementById("vkoolRate");
                var vkoolRate = xmlDoc.getElementsByTagName("vkool_rate")[0].textContent;
                console.log(vkoolRate);
                vkoolRateTag.append(vkoolRate);

                //get phimmoi rate
                var phimmoiRateTag = document.getElementById("phimmoiRate");
                var phimmoiRate = xmlDoc.getElementsByTagName("bilu_rate")[0].textContent;
                console.log(phimmoiRate);
                phimmoiRateTag.append(phimmoiRate);

                //get phimmoi link
                var phmmoiLinkTag = document.getElementById("phimmoiLink");
                var phimmoiLink = xmlDoc.getElementsByTagName("bilu_link")[0];
                console.log(phimmoiLink);
                if (phimmoiLink) {
                    phmmoiLinkTag.href = phimmoiLink.textContent;
                } else {
                    document.getElementById("divLink").removeChild(phmmoiLinkTag);
                }

                //get vkool link
                var vkoolLinkTag = document.getElementById("vkoolLink");
                var vkoolLink = xmlDoc.getElementsByTagName("vkool_link")[0];
                if (vkoolLink) {
                    vkoolLinkTag.href = vkoolLink.textContent;
                } else {
                    document.getElementById("divLink").removeChild(vkoolLinkTag);
                }

            }
        };

        xhttp.open("GET", url, true);
        xhttp.send();
    }
</script>
</body>

</html>