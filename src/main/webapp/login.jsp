<%--
  Created by IntelliJ IDEA.
  User: maria
  Date: 10/16/2019
  Time: 2:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <script src="https://code.jquery.com/jquery-1.10.2.js"
            type="text/javascript"></script>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="utilities.css">
    <link rel="stylesheet" href="Colors.css">
    <scipt src="js/utilities.js"></scipt>
</head>
<body>
<div class="container-fluid">
    <a href="" class="btn btn-success pull-right" style="float:right!important;visibility: hidden">Login</a>
    <a id="feedbackButton" href="feedback.jsp" class="btn btn-success pull-right" style="float:right!important;margin-right: 20px;display: block">Feedback</a>
    <div class="row row-offcanvas row-offcanvas-left">
        <div class="col-xs-12 col-sm-12" data-spy="scroll" data-taget="#sidebar-nav">
            <div class="row">
                <div class="col-xs-12 col-sm-6 col-lg-3">
                </div>
                <div class="col-xs-12 col-sm-6 col-lg-3">
                    <div class="panel-default">
                        <div class="panel-heading">
                            <h4>Login</h4>
                        </div>
                        <form style="margin-top:30px" method="post">
                            <div class="form-group">
                                <label>Username</label>
                                <input type="text" class="form-control" id="inputUser" placeholder="Enter username">
                            </div>
                            <div class="form-group">
                                <label>Password</label>
                                <input type="password" class="form-control" id="inputPassword" placeholder="Password">
                            </div>
                            <div class="text-center">
                                <button class="btn btn-primary" method="submit" type="button" onclick="tryLogin()">Submit</button>
                            </div>
                        </form>
                        <div class="text-center" id="errorMessage"></div>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-6 col-lg-6">
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    function tryLogin()
    {
        var username = $("#inputUser").val();
        var password = $("#inputPassword").val();

        if(username == null || username == "") {
            $("#errorMessage").text("Please enter username");
            return false;
        }
        if(password == null || password == "") {
            $("#errorMessage").text("Please enter password");
            return false;
        }

        $.post("LoginServelet", {"command": "login", "username": username, "password":password}, onLoginResponse, 'json');
        return false;
    }

    function onLoginResponse(response)
    {
        returnPage = '<%=request.getParameter("returnPage")%>';
        if(response.loggedIn == false) {
            $("#errorMessage").text("The username and password does not match");
        }
        else {
            $("#errorMessage").text("The login completed succesfully, please wait.");
            window.location.replace(returnPage);
        }
    }
</script>
</html>
