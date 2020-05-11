document.addEventListener("DOMContentLoaded", function () {
    // const modalWindow = window.modalOverlay();
    initModalWindow();
    const utils = window.ffbUtils;
    utils.init();
    utils.spinner.createSpinner();
    utils.spinner.hideSpinner();
    utils.userFunctions.refreshTable();
});
