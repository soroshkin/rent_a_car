package com.epam;

import com.epam.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ModelUtilityClass {
    private static final LocalDate PAST_DATE = LocalDate.now().minus(1, ChronoUnit.YEARS);
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);

    public static User createUser(){
        return new User("email@domain.com", PAST_DATE);
    }

    public static Passport createPassport(){
        return new Passport("AB12345", "some address", "Semen", "Krotov", createUser());
    }

    public static Bill createBill(){
        return new Bill(PAST_DATE, AMOUNT, createUser());
    }

    public static Car createCar(){
        return new Car("ZAZ", PAST_DATE, 100);
    }

    public static Account createAccount(){
        return new Account(createUser());
    }
}
