<%--
  Created by IntelliJ IDEA.
  User: Bulya
  Date: 23/10/2019
  Time: 22:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Feedback</title>
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
    <div class="row row-offcanvas row-offcanvas-left">
        <div class="col-xs-12 col-sm-12" data-spy="scroll" data-taget="#sidebar-nav">
            <div class="row">
                <div class="col-xs-12 col-sm-6 col-lg-3">
                </div>
                <div class="col-xs-12 col-sm-6 col-lg-3">
                    <div class="panel-default">
                        <div class="panel-heading">
                            <h4>Give us feedback</h4>
                        </div>
                        <span>Let us know of any problems we have in the site, any features you'd like to be developed, or any kind words you'd like to give.</span>
                        <form style="margin-top:30px" method="post">
                            <label for="feebackArea">Tell us your feedback</label>
                            <textarea class="form-control rounded-0" id="feebackArea" rows="10"></textarea>
                            <div class="text-center">
                                <button id="submitButton" class="btn btn-primary" method="submit" type="button" onclick="sendFeedback()">Submit</button>
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
    function sendFeedback() {
        var message = $("#feebackArea").val();

        if(message == null || message == "")
            $("#errorMessage").text("Please enter a message first");

        $("#submitButton").button('loading');
        $.post("FeedbackServlet", JSON.stringify({"message": message}), onFeedbackResponse, 'json');
    }

    function onFeedbackResponse(response) {
        $("#submitButton").button('reset');
        if(response.success) {
            $("#errorMessage").text("Thank you");
        }
        else {
            $("#errorMessage").text(response.errorMsg);
        }
    }
</script>
</html>
