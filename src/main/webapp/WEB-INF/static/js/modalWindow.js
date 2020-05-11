let openModal;
let closeModal;
let modalOverlay;
let openOverlay;
let closeOverlay;

function initModalWindow() {
    let modal = document.querySelector('.modal');
    modalOverlay = document.querySelector('.modal-overlay')
    let buttonClose = document.querySelector('.close-button');
    let modalMessage = document.querySelector('.modal-message');

    openModal = function (message) {
        modalMessage.innerText = message;
        modal.classList.value = ('modal');
        modalOverlay.classList.value = ('modal-overlay');
    }

    closeModal = function () {
        modal.classList.value = ('closed');
        modalOverlay.classList.value = ('closed');
    }

    openOverlay = function () {
        modalOverlay.classList.value = 'modal-overlay';
    }

    closeOverlay = function () {
        if (modal.classList.value === ('closed')) {
            modalOverlay.classList.value = 'closed';
        }
    }

    buttonClose.addEventListener('click', function () {
        closeModal();
    })

    modalOverlay.addEventListener('click', function () {
        closeModal();
    })

    modal.classList.value = ('closed');
    modalOverlay.classList.value = ('closed');
}


