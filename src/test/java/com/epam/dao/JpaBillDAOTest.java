package com.epam.dao;

import com.epam.EntityManagerSetupExtension;
import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.epam.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaBillDAOTest {
    private JpaBillDAO jpaBillDAO = new JpaBillDAO();
    private JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private JpaCarDAO jpaCarDAO = new JpaCarDAO();
    private Bill bill;
    private User user;
    private Car car;

    @BeforeEach
    public void setUp() {
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
        assertThat(jpaBillDAO.get(bill.getId()).orElse(mockBill))
                .isEqualToIgnoringGivenFields(bill, "car", "user");
    }

    @Test
    public void getAll() {
        jpaBillDAO.save(bill);
        jpaCarDAO.save(car);
        Bill anotherBill = new Bill(LocalDate.now(), BigDecimal.valueOf(1.23), user, car);
        jpaBillDAO.save(anotherBill);
        List<Bill> bills = new ArrayList<>();
        bills.add(bill);
        bills.add(anotherBill);
        assertThat(jpaBillDAO.getAll()).usingElementComparatorIgnoringFields("car", "user").isEqualTo(bills);
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