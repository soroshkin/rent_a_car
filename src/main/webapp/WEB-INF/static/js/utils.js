(function () {
    if (window.ffbUtils !== undefined) {
        return;
    }

    const ffbUtils = {};
    ffbUtils.store = {
        locations: [],
        users: [],
        passports: [],
        userViews: [],
        editUser: null
    };
    ffbUtils.templates = {};

    ffbUtils.ajax = {
        baseUrl: '',
        get usersUrl() {
            return this.baseUrl + '/users';
        },
        get accountsUrl() {
            return this.baseUrl + '/accounts';
        },
    };

    ffbUtils.init = function () {
        ffbUtils.templates.record = document.querySelector('#template-record').content;
        const createRecordView = document.body.querySelector('.ffb-edit-record-wrapper');
        ffbUtils.store.createRecordView = createRecordView;
        createRecordView.style.display = 'none';

        createRecordView.querySelector('.ffb-edit-record-save-btn')
            .addEventListener('click', function () {
                // ffbUtils.spinner.fullScreenOverlay.style.display = 'none';
                closeOverlay();
                ffbUtils.store.createRecordView.style.display = 'none';
                const id = document.body.querySelector(".user-edit-record-userId").value;
                const email = document.body.querySelector('.user-edit-record-email').value;
                const dateOfBirth = document.body.querySelector('.user-edit-record-dateOfBirth').value;
                const depositUSD = document.body.querySelector('.users-edit-record-account-depositusd').value;
                const depositEUR = document.body.querySelector('.users-edit-record-account-depositeur').value;
                // modalOverlay.classList.toggle("closed");
                let user = {
                    id: id,
                    email: email,
                    dateOfBirth: dateOfBirth,
                };
                let account = {
                    id: id,
                    depositUSD: depositUSD,
                    depositEUR: depositEUR,
                    user: {
                        id: id
                    }
                };
                if (ffbUtils.store.editUser) {
                    const editUser = ffbUtils.store.editUser;
                    user.id = editUser.id;
                    ffbUtils.ajax
                        .updateUser(user)
                        .then(
                            (data) => {
                                ffbUtils.userFunctions.refreshTable();
                            },
                            (error) => console.log(error.response)
                        );
                    ffbUtils.ajax
                        .updateAccount(account)
                        .then(
                            (data) => {
                                ffbUtils.userFunctions.refreshTable();
                            },
                            (error) => console.log(error.response)
                        );
                } else {
                    user.id = null;
                    ffbUtils.ajax
                        .createUser(user)
                        .then(
                            (data) => {
                                account.id = data.data.id;
                                account.user.id = account.id;
                                ffbUtils.ajax
                                    .createAccount(account)
                                    .then(
                                        (data) => {
                                            ffbUtils.userFunctions.refreshTable();
                                        },
                                        (error) => console.error(error)
                                    );
                                ffbUtils.userFunctions.refreshTable();
                            },
                            (error) => console.error(error)
                        );
                }
            });

        modalOverlay.addEventListener('click', function () {
            closeModal();
            ffbUtils.store.createRecordView.style.display = 'none';
        })


        createRecordView.querySelector('.ffb-edit-record-cancel-btn')
            .addEventListener('click', () => {
                // ffbUtils.spinner.fullScreenOverlay.style.display = 'none';
                ffbUtils.store.createRecordView.style.display = 'none';
                // modalOverlay.classList.toggle('closed');
                closeOverlay();
            });

        document.body.querySelector('.user-create-record')
            .addEventListener('click', function () {
                ffbUtils.userFunctions.createNewRecord(false);
                openOverlay();
            })

        document.body.querySelector('.user-refresh-records')
            .addEventListener('click', function () {
                ffbUtils.userFunctions.refreshTable();
            })

        ffbUtils.store.userTable = document.querySelector('.ffb-table-content');
    };

    ffbUtils.ajax.getUsers = function () {
        ffbUtils.spinner.showSpinner();
        return window.axios
            .get(this.usersUrl)
            .then((response) => {
                ffbUtils.store.users = response.data;
                ffbUtils.spinner.hideSpinner();
            })
            .catch((err) => {
                console.error(err);
                ffbUtils.spinner.hideSpinner();
            });
    };


    ffbUtils.userFunctions = {};
    ffbUtils.userFunctions.refreshTable = function () {
        ffbUtils.ajax.getUsers().then(() => {
            ffbUtils.userFunctions.cleanTable();
            ffbUtils.userFunctions.fillTable();
        })
    };

    ffbUtils.errorHandler = {};

    //return promise
    ffbUtils.ajax.updateUser = function (user) {
        return window.axios.put(`${this.usersUrl}/${user.id}`, user)
            .catch(function (error) {
                if (error.response) {
                    ffbUtils.errorHandler.showModal(error.response.data, error.response.status)
                }
            });
    };

    ffbUtils.ajax.updateAccount = function (account) {
        return window.axios.put(`${this.accountsUrl}/${account.id}`, account)
            .catch(function (error) {
                if (error.response) {
                    ffbUtils.errorHandler.showModal(error.response.data, error.response.status)
                }
            });
    };

    ffbUtils.ajax.createUser = function (user) {
        return window.axios.post(`${this.usersUrl}`, user)
            .catch(function (error) {
                if (error.response) {
                    ffbUtils.errorHandler.showModal(error.response.data, error.response.status)
                }
            });
    };

    ffbUtils.ajax.createAccount = function (account) {
        return window.axios.post(`${this.accountsUrl}`, account)
            .catch(function (error) {
                if (error.response) {
                    ffbUtils.errorHandler.showModal(error.response.data, error.response.status)
                }
            });
    };

    ffbUtils.ajax.deleteUser = function (user) {
        return window.axios.delete(`${this.usersUrl}/${user.id}`)
            .catch(function (error) {
                if (error.response) {
                    ffbUtils.errorHandler.showModal(error.response.data, error.response.status);
                }
            });
    };

    ffbUtils.errorHandler.showModal = function (errorMessage, status) {
        openModal(errorMessage + ".\n Status: " + status);
    }

    ffbUtils.userFunctions.fillTable = function () {
        ffbUtils.store.users.forEach((c) => {
            ffbUtils.store.userTable.append(ffbUtils.userFunctions.createRecord(c));
        })
    };

    ffbUtils.userFunctions.cleanTable = function () {
        ffbUtils.store.userTable.innerHTML = '';
    };


    ffbUtils.userFunctions.createRecord = function (user) {
        const viewTemplate = ffbUtils.templates.record;
        viewTemplate.querySelector('.user-id').innerText = user.id;
        viewTemplate.querySelector('.user-email').innerText = user.email;
        viewTemplate.querySelector('.user-date').innerText = user.dateOfBirth;
        viewTemplate.querySelector('.user-deposit-usd').innerText = user.account.depositUSD;
        viewTemplate.querySelector('.user-deposit-eur').innerText = user.account.depositEUR;
        let view = document.importNode(viewTemplate, true);
        view.userId = user.id;

        //edit button
        view.querySelector('.fbb-user-edit-btn')
            .addEventListener('click', function () {
                ffbUtils.userFunctions.update(user);
                openOverlay();
            });

        view.querySelector('.fbb-user-delete-btn')
            .addEventListener('click', function () {
                ffbUtils.spinner.showSpinner();
                ffbUtils.ajax
                    .deleteUser(user)
                    .then(
                        (data) => {
                            ffbUtils.userFunctions.refreshTable();
                            ffbUtils.spinner.hideSpinner();
                        },
                        (error) => console.log(error.response)
                    )
                    .catch((err) => {
                        console.error(err);
                        ffbUtils.spinner.hideSpinner();
                    });
            });

        ffbUtils.store.userViews.push(view);

        return view;
    };

    ffbUtils.userFunctions.update = function (user) {
        ffbUtils.userFunctions.createNewRecord(true, user);
    }

    //edit entity user
    ffbUtils.userFunctions.createNewRecord = function (edit, user) {
        ffbUtils.store.createRecordView.style.display = '';
        openOverlay();
        const userId = document.body.querySelector('.user-edit-record-userId');
        const email = document.body.querySelector('.user-edit-record-email');
        const dateOfBirth = document.body.querySelector('.user-edit-record-dateOfBirth');
        const accountDepositUSD = document.body.querySelector('.users-edit-record-account-depositusd');
        const accountDepositEUR = document.body.querySelector('.users-edit-record-account-depositeur');
        if (edit) {
            ffbUtils.store.editUser = user;
            userId.innerText = user.id;
            userId.value = user.id;
            email.value = user.email;
            dateOfBirth.value = user.dateOfBirth;
            accountDepositUSD.value = user.account.depositUSD;
            accountDepositEUR.value = user.account.depositEUR;
        } else {
            userId.innerText = '';
            email.value = "new_email@epam.com";
            dateOfBirth.value = '0000-00-00';
            accountDepositUSD.value = '0';
            accountDepositEUR.value = '0';
            ffbUtils.store.editUser = null;
        }
    }


    ffbUtils.spinner = {};
    ffbUtils.spinner.spinnerContainerClassName = 'ffb-spinner-container';
    ffbUtils.spinner.spinnerClassName = 'ffb-spinner';

    ffbUtils.spinner.createSpinner = function () {
        if (document.body.querySelector(`.${this.spinnerContainerClassName}`)) {
            return;
        }

        const spinnerContainer = document.createElement('div');
        spinnerContainer.classList.add(this.spinnerContainerClassName);

        const spinner = document.createElement('div');
        spinner.classList.add(this.spinnerClassName);
        spinnerContainer.append(spinner);

        spinner.innerHTML = ` <div class="sk-grid">
                                   <div class="sk-grid-cube"></div>
                                   <div class="sk-grid-cube"></div>
                                   <div class="sk-grid-cube"></div>
                                   <div class="sk-grid-cube"></div>
                                   <div class="sk-grid-cube"></div>
                                   <div class="sk-grid-cube"></div>
                                   <div class="sk-grid-cube"></div>
                                   <div class="sk-grid-cube"></div>
                                   <div class="sk-grid-cube"></div>
                               </div>
                             `;
        document.body.prepend(spinnerContainer);
        ffbUtils.spinner.spinnerContainer = spinnerContainer;
    };

    ffbUtils.spinner.showSpinner = function () {
        openOverlay();
        this.spinnerContainer.style.display = '';
    };

    ffbUtils.spinner.hideSpinner = function () {
        closeOverlay();
        this.spinnerContainer.style.display = 'none';
    };

    window.ffbUtils = ffbUtils;
}());
