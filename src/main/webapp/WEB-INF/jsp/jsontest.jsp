<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>JSON Test</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
    <jsp:include page="fragments/header.jsp"/>
</head>
<body>
<div class="json-table">
    <p>JSON Test</p>
    <div class="json-row">

    <form class="json-left-form" action="${pageContext.request.contextPath}/json/serialize">
        <input type="submit" value="serialize"/>
    </form>

    <form class="json-right-form" action="${pageContext.request.contextPath}/json/deserialize">
        <input type="submit" value="deserialize"/>
    </form>
    </div>

</div>


</body>
</html>
