<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title> RENT-A-CAR</title>
    <jsp:include page="fragments/header.jsp"/>
</head>
<body>
<div class="users-table" align="center">
    <a href="${pageContext.request.contextPath}/json">
        ------>JSON test<------
        <p></p>
    </a>
    <div class="ffb-buttons-bar">
        <button class="user-create-record btn btn-primary">create</button>
        <button class="user-refresh-records btn btn-primary">refresh</button>
    </div>
    <div class="users-table-td"><h1>users</h1><br><br><br></div>
</div>

<div class="ffb-edit-record-wrapper">
    <div class="ffb-edit-record-inner-wrapper">
        <div class="ffb-edit-record">
            <div class="user-edit-record-id-text">id</div>
            <div class="user-edit-record-userId"></div>
            <div class="user-edit-field">
                <input class="user-edit-record-email">
            </div>
            <div class="user-edit-field">
                <input type="date" class="user-edit-record-dateOfBirth">
            </div>
            <div class="user-edit-field" style="position: relative">
                <label class="currency" data-currency="usd">
                    <input type="number" class="users-edit-record-account-depositusd">
                </label>
            </div>
            <div class="user-edit-field" style="position: relative">
                <label class="currency" data-currency="eur">
                    <input type="number" class="users-edit-record-account-depositeur">
                </label>
            </div>

            <div class="user-edit-field ffb-edit-field-actions">
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



<div class="users-table">
    <div class="user-table-header">
        <div class="user-id">id</div>
        <div class="users-table-td user-email">E-mail</div>
        <div class="users-table-td user-date">date of birth</div>
        <div class="users-table-td user-deposit-usd">deposit, USD</div>
        <div class="users-table-td user-deposit-eur">deposit, EUR</div>
        <div class="ffb-table-th cube-action-buttons-column-name">actions</div>
    </div>
    <div class="ffb-table-content"></div>
</div>

<template id="template-record">
    <div class="ffb-table-content-record">
        <div class="users-table-td user-id"></div>
        <div class="users-table-td user-email"></div>
        <div class="users-table-td user-date"></div>
        <div class="users-table-td user-deposit-usd"></div>
        <div class="users-table-td user-deposit-eur"></div>
        <div class="ffb-table-td user-action-buttons">
            <button class="fbb-user-edit-btn btn btn-warning">
                <em class="fa fa-pencil-square-o"></em>
            </button>
            <button class="fbb-user-delete-btn btn btn-danger">
                <em class="fa fa-trash-o"></em>
            </button>
        </div>
    </div>
</template>



<div class="modal-overlay" id="modal-overlay"></div>

<div class="modal" id="modal" aria-hidden="true" aria-labelledby="modalTitle" aria-describedby="modalDescription"
     role="dialog">
    <button class="close-button" id="close-button">X</button>
    <div class="modal-body" role="document">
        <h1 align="center">Message</h1>
        <div class="modal-message"></div>
    </div>
</div>

</body>
</html>