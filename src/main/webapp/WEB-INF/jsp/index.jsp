<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title> RENT-A-CAR</title>
    <script src="${pageContext.request.contextPath}/resources/js/axios.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/utils.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/spinkit.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/normalize.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">

</head>
<body>
<div class="users-table">
    <a href="${pageContext.request.contextPath}/json">
        ------>JSON test<------
        <p></p>
    </a>
</div>


<%--<div class="full-screen-overlay"></div>--%>
<%--<div class="ffb-buttons-bar">--%>
<%--    <button class="ffb-create-record btn btn-primary">create</button>--%>
<%--    <button class="ffb-refresh-records btn btn-primary">refresh</button>--%>
<%--</div>--%>
<div class="users-table">
    <div class="users-table-td">users</div>
    <div class="ffb-table-header">
        <div class="user-id">id</div>
        <div class="users-table-td user-email">E-mail</div>
        <div class="users-table-td user-date">date of birth</div>
        <div class="users-table-td user-deposit">deposit, USD</div>
        <div class="ffb-table-th cube-action-buttons-column-name">actions</div>
    </div>
    <div class="ffb-table-content"></div>
</div>

<template id="template-record">
    <div class="ffb-table-content-record">
        <div class="users-table-td user-id"></div>
        <div class="users-table-td user-email"></div>
        <div class="users-table-td user-date"></div>
        <div class="users-table-td user-deposit"></div>
        <div class="ffb-table-td cube-action-buttons">
            <button class="fbb-user-edit-btn btn btn-warning">
                <em class="fa fa-pencil-square-o"></em>
            </button>
            <button class="fbb-cube-delete-btn btn btn-danger">
                <em class="fa fa-trash-o"></em>
            </button>
        </div>
    </div>
</template>

<div class="ffb-edit-record-wrapper">
    <div class="ffb-edit-record-inner-wrapper">
        <div class="ffb-edit-record">
            <div class="ffb-edit-field">
                <input class="ffb-edit-record-color">
            </div>
            <div class="ffb-edit-field ffb-edit-field-location" style="position: relative">
                <div class="ffb-edit-record-location-name"></div>
                <div class="ffb-locations-dropdown-wrapper">
                    <div class="ffb-locations-dropdown"></div>
                </div>
            </div>
            <div class="ffb-edit-field">
                <input type="number" class="ffb-edit-record-size">
            </div>
            <div class="ffb-edit-field ffb-edit-field-actions">
                <button class="ffb-edit-record-save-btn btn btn-warning">
                    <em class="fa fa-save"></em>
                </button>
                <button class="ffb-edit-record-cancel-btn btn btn-secondary">
                    <em class="fa fa-times"></em>
                </button>
            </div>
        </div>
    </div>
</div>


</body>
</html>