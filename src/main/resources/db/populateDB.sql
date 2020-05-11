INSERT INTO users (date_of_birth, email)
VALUES ('2000-01-01', 'email1@mail.ru');
INSERT INTO users (date_of_birth, email)
VALUES ('1980-01-01', 'email2@mail.ru');
INSERT INTO users (date_of_birth, email)
VALUES ('1910-01-01', 'email3@mail.ru');
INSERT INTO users (date_of_birth, email)
VALUES ('1984-01-01', 'email4@mail.ru');
INSERT INTO users (date_of_birth, email)
VALUES ('1956-01-01', 'email5@mail.ru');
INSERT INTO users (date_of_birth, email)
VALUES ('1446-01-01', 'email8@mail.ru');

INSERT INTO accounts (deposit_EUR, deposit_USD, user_id)
VALUES ('100', '2010', '1');
INSERT INTO accounts (deposit_EUR, deposit_USD, user_id)
VALUES ('200', '2040', '2');
INSERT INTO accounts (deposit_EUR, deposit_USD, user_id)
VALUES ('130', '260', '3');
INSERT INTO accounts (deposit_EUR, deposit_USD, user_id)
VALUES ('150', '800', '4');
INSERT INTO accounts (deposit_EUR, deposit_USD, user_id)
VALUES ('1100', '600', '5');
INSERT INTO accounts (deposit_EUR, deposit_USD, user_id)
VALUES ('1100', '500', '6');

INSERT INTO passports (address, name, passport_number, surname, user_id)
VALUES ('London', 'John', 'FA-32', 'Smith', '3');
INSERT INTO passports (address, name, passport_number, surname, user_id)
VALUES ('Kaliningrad', 'Boris', '2341po', 'Krasin', '2');
INSERT INTO passports (address, name, passport_number, surname, user_id)
VALUES ('Moscow', 'Semen', 'dqqwe321', 'Krotov', '1');
INSERT INTO passports (address, name, passport_number, surname, user_id)
VALUES ('Moscow', 'Semen', 'dsa1321', 'Krotov', '1');

INSERT INTO cars (mileage, model, production_date, registration_number)
VALUES ('200', 'Tesla', '2000-01-01', '23423');
INSERT INTO cars (mileage, model, production_date, registration_number)
VALUES ('33200', 'ZAZ', '2000-01-01', 'AB54353');

INSERT INTO bills (amount, date, car_id, user_id)
VALUES ('200', '2000-01-01', '1', '1');
INSERT INTO bills (amount, date, car_id, user_id)
VALUES ('12200', '1980-01-01', '2', '1');
INSERT INTO bills (amount, date, car_id, user_id)
VALUES ('23400', '1950-01-01', '1', '2');
INSERT INTO bills (amount, date, car_id, user_id)
VALUES ('205460', '2019-01-01', '2', '3');