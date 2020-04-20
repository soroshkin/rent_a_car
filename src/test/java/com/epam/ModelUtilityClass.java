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

    public static Passport createPassport(User user){
        return new Passport("AB12345", "some address", "Semen", "Krotov", user);
    }

    public static Bill createBill(User user, Car car){
        return new Bill(PAST_DATE, AMOUNT, user, car);
    }

    public static Car createCar(){
        return new Car("ZAZ", PAST_DATE, 100);
    }

    public static Account createAccount(User user){
        return new Account(user);
    }
}
