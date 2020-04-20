package com.epam;

import com.epam.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class ConstraintsTest {

    public static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void checkPassportConstraintsViolation() {
        User user = new User("batman@mail.ru", LocalDate.of(1988, 1, 9));
        Set<ConstraintViolation<Passport>> violations = validator.validate(new Passport(null, null, null, null, null));
        assertThat(violations.size()).isEqualTo(9);
        violations = validator.validate(new Passport("", "", "", "", null));
        assertThat(violations.size()).isEqualTo(5);
        violations = validator.validate(new Passport("1", "1", "1", "1", user));
        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void checkUserConstraintsViolation() {
        Set<ConstraintViolation<User>> violations = validator.validate(new User(null, null));
        assertThat(violations.size()).isEqualTo(3);
        violations = validator.validate(new User("", LocalDate.now().plusWeeks(1)));
        assertThat(violations.size()).isEqualTo(2);
        violations = validator.validate(new User("name", LocalDate.now().minus(1, ChronoUnit.YEARS)));
        assertThat(violations.size()).isEqualTo(1);
        violations = validator.validate(new User("email@domain.com", LocalDate.now().minus(1, ChronoUnit.YEARS)));
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void checkCarConstraintsViolation() {
        Set<ConstraintViolation<Car>> violations = validator.validate(new Car(null, null, -1));
        assertThat(violations.size()).isEqualTo(4);
        violations = validator.validate(new Car("", LocalDate.now().plusWeeks(10), 10));
        assertThat(violations.size()).isEqualTo(2);
        violations = validator.validate(new Car("some model", LocalDate.now(), 10));
        assertThat(violations.size()).isEqualTo(1);
        violations = validator.validate(new Car("some model", LocalDate.now().minus(12, ChronoUnit.YEARS), 10));
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void checkAccountConstraintsViolation(){
        Set<ConstraintViolation<Account>> violations = validator.validate(new Account(null));
        assertThat(violations.size()).isEqualTo(1);
        User mockUser = Mockito.mock(User.class);
        Account account = new Account(mockUser);
        account.setDepositEUR(null);
        account.setDepositUSD(null);
        violations = validator.validate(account);
        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    public void checkBillConstraintsViolation(){
        User mockUser = Mockito.mock(User.class);
        Car mockCar = Mockito.mock(Car.class);
        LocalDate pastDate = LocalDate.now().minus(1, ChronoUnit.WEEKS);
        LocalDate futureDate = LocalDate.now().plusWeeks(1);
        Set<ConstraintViolation<Bill>> violations = validator.validate(new Bill(null, null, null, null));
        assertThat(violations.size()).isEqualTo(4);
        violations = validator.validate(new Bill(futureDate, BigDecimal.valueOf(-10), mockUser, mockCar));
        assertThat(violations.size()).isEqualTo(2);
        violations = validator.validate(new Bill(pastDate, BigDecimal.valueOf(0), mockUser, mockCar));
        assertThat(violations.size()).isEqualTo(0);
    }
}
