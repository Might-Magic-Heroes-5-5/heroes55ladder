<%--
  Created by IntelliJ IDEA.
  User: maria
  Date: 10/17/2019
  Time: 10:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>parameter test</title>
    <script src="https://code.jquery.com/jquery-1.10.2.js" type="text/javascript"></script>
</head>
<body>
    <div>Parameter = <%=request.getParameter("parName")%></div>
    <br>
    <div id="placeForParameter">

    </div>
</body>
<script>
    var parameter = "<%=request.getParameter("parName")%>";

    $(document).ready(function() {
       $("#placeForParameter").text("Second parameter = " + parameter);
    });
</script>
</html>
