<%--
  Created by IntelliJ IDEA.
  User: maria
  Date: 10/16/2019
  Time: 1:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign up</title>
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
                            <h4>Sign up</h4>
                        </div>
                        <p>In order to sign up you must be part of our discord server. We use the discord server to authenticate users, create challenges with other users, as well as make sure nobody claims a win which he didn't do.
                        The server will also help you find people to play with, share strategies, and be up to date with the newest releases of Heroes 5.5.</p>
                        <a href="https://discord.gg/NZkUD6" target="_blank">Click here to join our Heroes 5.5 discord server</a>
                        <form style="margin-top:30px" method="post">
                            <div class="form-group">
                                <label>Discord tag</label>
                                <input type="text" class="form-control" id="inputDiscord" aria-describedby="emailHelp" placeholder="discordUser#1234">
                            </div>
                            <div class="form-group">
                                <label>Username</label>
                                <input type="text" class="form-control" id="inputUser" placeholder="Enter username">
                            </div>
                            <div class="form-group">
                                <label>Password</label>
                                <input type="password" class="form-control" id="inputPassword" placeholder="Password">
                            </div>
                            <div class="form-group">
                            <label>Confirm password</label>
                            <input type="password" class="form-control" id="inputConfirmPassword" placeholder="Password">
                            </div>
                            <div class="text-center">
                                <button class="btn btn-primary" method="submit" type="button" onclick="signUp()">Submit</button>
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
    function signUp()
    {
        var discord = $("#inputDiscord").val();
        var username = $("#inputUser").val();
        var password = $("#inputPassword").val();
        var confirmedPassword = $("#inputConfirmPassword").val();

        if(discord == null || discord == "") {
            $("#errorMessage").text("Please enter email");
            return;
        }

        if(discord.indexOf('#') == -1) {
            $("#errorMessage").text("The discord tag isn't up to the proper format");
            return
        }

        if(username == null || username == ""){
            $("#errorMessage").text("Please enter username");
            return;
        }

        if(password == null || password == "") {
            $("#errorMessage").text("Please enter password");
            return;
        }

        if(password != confirmedPassword) {
            $("#errorMessage").text("The password and the confirmed password do not match");
            return;
        }

        $.post("LoginServelet", {"command": "signUp", "discord": discord, "username": username, "password":password}, onSignUpResponse, 'json');
        return;
    }

    function onSignUpResponse(response)
    {
        if(response.success == false) {
            $("#errorMessage").text(response.errorMessage);
        }
        else {
            $("#errorMessage").text("The sign up completed succesfully, please check discord for the verification message.");
        }
    }
</script>
</html>
