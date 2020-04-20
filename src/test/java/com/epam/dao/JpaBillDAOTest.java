package com.epam.dao;

import com.epam.DatabaseSetupExtension;
import com.epam.ModelUtilityClass;
import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.epam.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.entry;

public class JpaBillDAOTest {
    private JpaBillDAO jpaBillDAO = new JpaBillDAO();
    private JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private JpaCarDAO jpaCarDAO = new JpaCarDAO();
    private Bill bill;
    private User user;
    private Car car;

    @RegisterExtension
    DatabaseSetupExtension databaseSetupExtension = new DatabaseSetupExtension();

    @BeforeEach
    public void setUp() {
        jpaBillDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        jpaUserDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        jpaCarDAO.setEntityManager(databaseSetupExtension.getEntityManager());

        user = createUser();
        jpaUserDAO.save(user);
        car = createCar();
        jpaCarDAO.save(car);
        bill = createBill(user, car);
    }

    @Test
    public void get() {
        jpaBillDAO.save(bill);
        Bill mockBill = Mockito.mock(Bill.class);
        assertThat(jpaBillDAO.get(bill.getId())).isPresent();
        assertThat(jpaBillDAO.get(bill.getId()).orElse(mockBill)).isEqualTo(bill);
    }

    @Test
    public void getAll() {
        jpaBillDAO.save(bill);
        jpaCarDAO.save(car);
        Bill anotherBill = new Bill(LocalDate.now(), BigDecimal.valueOf(100), user, car);
        jpaBillDAO.save(anotherBill);
        List<Bill> bills = new ArrayList<>();
        bills.add(bill);
        bills.add(anotherBill);
        assertThat(jpaBillDAO.getAll()).isEqualTo(bills);
    }

    @Test
    public void save() {
        assertThat(jpaBillDAO.save(bill)).isSameAs(bill);
    }

    @Test
    public void delete() {
        jpaBillDAO.save(bill);
        assertThat(jpaBillDAO.delete(bill.getId())).isTrue();
    }
}