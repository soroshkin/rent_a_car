<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>JSON Test</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
</head>
<body>
<div class="json-table">
    <p>JSON Test</p>

    <form class="json-left-form" action="${pageContext.request.contextPath}/json/serialize">
        <input type="submit" value="serialize"/>
    </form>

    <form class="json-right-form" action="${pageContext.request.contextPath}/json/deserialize">
        <input type="submit" value="deserialize"/>
    </form>

</div>


</body>
</html>
