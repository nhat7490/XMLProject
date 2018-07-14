<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="css/util.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <!--===============================================================================================-->
</head>
<body>

<div class="limiter">
    <div class="container-login100" style="background-image: url('../image/cinema.jpg');">
        <div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
					Đăng Nhập
				</span>
            <form class="login100-form validate-form p-b-33 p-t-5" method="post">

                <div class="wrap-input100 validate-input" data-validate="Enter username">
                    <input class="input100" type="text" id="username" name="username" placeholder="User name" required>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Enter password">
                    <input class="input100" type="password" id="password" name="password" placeholder="Password"
                           required>
                </div>

                <div id="error" class="col-12">
                </div>

                <div class="container-login100-form-btn m-t-32">
                    <button type="button" class="login100-form-btn" onclick="login()">
                        Login
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>
<script>
    function login() {
        var error = document.getElementById("error");
        error.innerHTML = '';

        var username = document.getElementById('username').value;
        var password = document.getElementById('password').value;
        console.log(username);
        console.log(password);

        var xhttp = new XMLHttpRequest();
        var url = "/login";
        var params = 'username=' + username + '&password=' + password;
        xhttp.open("POST", url, true);

        xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

        xhttp.onreadystatechange = function () {
            if (xhttp.readyState === 4 && xhttp.status === 200) {

                if (xhttp.responseText === '1') {
                    location.replace("http://localhost:8080/admin");
                } else if (xhttp.responseText === '2') {
                    location.replace("http://localhost:8080/trang-chu");
                }
            } else if(xhttp.status === 400){

                error.innerHTML = '';

                var errorMessage = document.createElement('div');
                errorMessage.classList.add('text-center');
                errorMessage.innerHTML = "Sai mật khẩu hoặc tên đăng nhập";
                error.appendChild(errorMessage);
            }

        };

        xhttp.send(params);
    }
</script>

</body>
</html>