<%--
  Created by IntelliJ IDEA.
  User: maria
  Date: 10/18/2019
  Time: 12:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Confirm match</title>
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
                    <div id="confirmMatchDiv" style="display: none">
                        <div class="panel-default">
                            <div class="panel-heading">
                                <h4>Confirm match</h4>
                            </div>
                            <div style="text-align: center">
                                <p id="claimPar"></p>
                                <br/>
                                <p class="btn btn-success" style="width:40%" onclick="confirmLoss()">Confirm</p>
                                <p class="btn btn-success" style="width:40%" onclick="declineLoss()">Decline</p>
                            </div>
                            <div class="text-center" id="errorMessage"></div>
                        </div>
                    </div>
                    <div id="confirmUserDiv" style="display:none;">
                        <form style="margin-top:30px" method="post">
                            <div class="form-group">
                                <label>Please enter password so that we confirm its you</label>
                                <input type="password" class="form-control" id="inputPassword" placeholder="Password">
                            </div>
                            <div class="text-center">
                                <button class="btn btn-primary" method="submit" type="button" onclick="loginWithIdenAndPass()">Submit</button>
                            </div>
                            <div class="text-center" id="confrimUsererrorMessage"></div>
                        </form>
                    </div>
                    <div id="errorDiv" style="display:none;">
                        <div class="panel-default">
                            <div class="panel-heading">
                                <h4>Error</h4>
                            </div>
                        </div>
                        <div>
                            <p id="errorParagraph"></p>
                        </div>
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
    var report;
    var identificator = "<%=request.getParameter("iden")%>";
    $(document).ready(function() {
        var inputObject = { "command": "getReportAndIsLoggedByIden",
            "identificator": identificator};
        $.post('ReportMatchServelet', JSON.stringify(inputObject), onGetReportResponse, 'json');
    });

    function loginWithIdenAndPass() {
        var password = $("#inputPassword").val();

        if(password == null || password == "") {
            $("#confrimUsererrorMessage").text("Please enter password");
            return false;
        }

        var inputObject = { "command": "loginByIdenAndPassword",
            "identificator": identificator,
            "password": password}

        $.post('ReportMatchServelet', JSON.stringify(inputObject), onGetReportResponse, 'json');
    }

    function onGetReportResponse(response) {
        if(response.success == false) {
            $("#errorDiv").show();
            $("#errorParagraph").text(response.errorMsg);
            return;
        }
        report = response;

        if(response.isLoggedIn == false) {
            $("#confirmUserDiv").show();
            return;
        }

        $("#errorDiv").hide();
        $("#confirmUserDiv").hide();
        $("#confirmMatchDiv").show();
        $("#claimPar").text("The user " + report.reportingUsername + " claims that he won a match against you.");
    }

    function confirmLoss() {
        var inputObject = { "command": "confirmReport",
            "identificator": identificator};
        $.post('ReportMatchServelet', JSON.stringify(inputObject), onSendAnswerResponse, 'json');
    }

    function declineLoss() {
        var inputObject = { "command": "declineReport",
            "identificator": identificator};
        $.post('ReportMatchServelet', JSON.stringify(inputObject), onSendAnswerResponse, 'json');
    }

    function onSendAnswerResponse(response) {
        if(response.success) {
            $("#errorMessage").text(response.successMsg);
        }
        else {
            $("#errorMessage").text(response.errorMsg);
        }
    }
</script>
</html>
