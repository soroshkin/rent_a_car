(function () {
    if (window.ffbUtils !== undefined) {
        return;
    }

    const ffbUtils = {};

    ffbUtils.templates = {};
    ffbUtils.init = function () {
        ffbUtils.templates.record = document.querySelector('#template-record').content;
        const createRecordView = document.body.querySelector('.ffb-edit-record-wrapper');
        ffbUtils.store.createRecordView = createRecordView;
        createRecordView.style.display = 'none';

        const dropDown = createRecordView.querySelector('.ffb-locations-dropdown-wrapper');
        dropDown.addEventListener('click', (event) => {
            //TODO: move to function
            const locationField = document.body.querySelector('.ffb-edit-record-location-name');
            locationField.innerText = event.target.innerText;
            dropDown.classList.add('display-none');
            event.preventDefault();
            event.stopImmediatePropagation();
        });

        document.body.addEventListener('click', () => {
            dropDown.classList.add('display-none');
        });

        dropDown.classList.add('display-none');

        createRecordView.querySelector('.ffb-edit-field-location')
            .addEventListener('click', function (event) {
                //TODO: move to function
                const dropDown = document.body.querySelector('.ffb-locations-dropdown-wrapper');
                dropDown.classList.remove('display-none');
                const locations = document.body.querySelector('.ffb-locations-dropdown');
                locations.innerHTML = '';
                ffbUtils.store.locations.forEach((l) => {
                    locations.append(ffbUtils.location.createDropDownElement(l.name));
                });
                event.preventDefault();
                event.stopImmediatePropagation();
            });

        createRecordView.querySelector('.ffb-edit-record-cancel-btn')
            .addEventListener('click', () => {
                // ffbUtils.spinner.fullScreenOverlay.style.display = 'none';
                ffbUtils.store.createRecordView.style.display = 'none';
            });

        ffbUtils.store.cubeTable = document.querySelector('.ffb-table-content');

    };

    ffbUtils.store = {
        locations: [],
        users: [],
        passports: [],
        cubeViews: [],
        editCube: null
    };

    ffbUtils.ajax = {
        baseUrl: '',
        get productionLocationsUrl() {
            return this.baseUrl + '/production-locations';
        },
        get usersUrl() {
            return this.baseUrl + '/users';
        },
        get passportsUrl() {
            return this.baseUrl + '/passports';
        }
    };
    ffbUtils.ajax.getProductionLocations = function () {
        // ffbUtils.spinner.showSpinner();
        return window.axios
            .get(this.productionLocationsUrl)
            .then((response) => {
                ffbUtils.store.locations = response.data;
                // ffbUtils.spinner.hideSpinner();
            })
            .catch((err) => {
                console.error(err);
                // ffbUtils.spinner.hideSpinner();
            });
    };

    ffbUtils.ajax.getUsers = function () {
        // ffbUtils.spinner.showSpinner();
        return window.axios
            .get(this.usersUrl)
            .then((response) => {
                ffbUtils.store.users = response.data;
                // ffbUtils.spinner.hideSpinner();
            })
            .catch((err) => {
                console.error(err);
                // ffbUtils.spinner.hideSpinner();
            });
    };


    ffbUtils.user = {};
    ffbUtils.user.refreshTable = function () {
        ffbUtils.ajax.getUsers().then(() => {
            ffbUtils.user.cleanTable();
            ffbUtils.user.fillTable();
        })

    };

    ffbUtils.user.fillTable = function () {
        ffbUtils.store.users.forEach((c) => {
            ffbUtils.store.cubeTable.append(ffbUtils.user.createRecord(c));
        })
    };

    ffbUtils.user.cleanTable = function () {
        ffbUtils.store.cubeTable.innerHTML = '';
    };

    ffbUtils.user.createRecord = function (user) {
        const viewTemplate = ffbUtils.templates.record;
        // const colorCell = viewTemplate.querySelector('.user-color');
        // colorCell.innerHTML = `
        //     <div class="ffb-cube-color-sample" style="background-color: ${user.color}"></div>
        //     ${user.color}
        //     `;
        // viewTemplate.querySelector('.user-production-location').innerText = user.productionLocation.name;
        viewTemplate.querySelector('.user-date').innerText = user.dateOfBirth;
        viewTemplate.querySelector('.user-id').innerText = user.id;
        viewTemplate.querySelector('.user-email').innerText = user.email;
        viewTemplate.querySelector('.user-deposit').innerText = user.account.depositUSD;
        // viewTemplate.querySelector('.user-passports').innerText = user.email;
        let view = document.importNode(viewTemplate, true);
        view.cubeId = user.id;
        view.querySelector('.fbb-user-edit-btn')
            .addEventListener('click', function (event) {
                ffbUtils.user.update(user)
            });
        // view.querySelector('.fbb-user-delete-btn')
        //     .addEventListener('click', function () {
        //         ffbUtils.user.delete(user);
        //     });

        ffbUtils.store.cubeViews.push(view);

        return view;
    };

    ffbUtils.user.update = function (user) {
        ffbUtils.user.createNew(true, user);
    }

    ffbUtils.user.createNew = function (edit, user) {
        ffbUtils.store.createRecordView.style.display = '';
        // ffbUtils.spinner.fullScreenOverlay.style.display = '';
        const colorField = document.body.querySelector('.ffb-edit-record-color');
        const sizeField = document.body.querySelector('.ffb-edit-record-size');
        const locationField = document.body.querySelector('.ffb-edit-record-location-name');
        if (edit) {
            ffbUtils.store.editUser = user;
            //
            // colorField.value = cube.color;
            // sizeField.value = cube.size;
            locationField.innerText = user.account.depositUSD;
        } else {
            colorField.value = '';
            locationField.innerText = '';
            sizeField.value = '';
            ffbUtils.store.editUser = null;
        }
    }

    window.ffbUtils = ffbUtils;
}());
