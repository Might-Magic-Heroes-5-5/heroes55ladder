<%--
  Created by IntelliJ IDEA.
  User: maria
  Date: 10/17/2019
  Time: 6:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Confirmation</title>
    <script src="https://code.jquery.com/jquery-1.10.2.js"
            type="text/javascript"></script>
</head>
<body>
    Please wait.
</body>
<script>
    $(document).ready(function() {
        $.post('LoginServelet', { "command": "confirm",
            "username": "<%=request.getParameter("username")%>",
            "identificator": "<%=request.getParameter("iden")%>"}, onConfirmResponse, 'json')
    });

    function onConfirmResponse(response) {
        if(response.success)
            window.location.replace("index.jsp");
    }
</script>

</html>
