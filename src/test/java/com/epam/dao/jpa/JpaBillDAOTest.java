package com.epam.dao.jpa;

import com.epam.EntityManagerSetupExtension;
import com.epam.dao.BillDAO;
import com.epam.dao.CarDAO;
import com.epam.dao.UserDAO;
import com.epam.dao.jpa.JpaBillDAOImpl;
import com.epam.dao.jpa.JpaCarDAOImpl;
import com.epam.dao.jpa.JpaUserDAOImpl;
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
    private BillDAO billDAO = new JpaBillDAOImpl();
    private UserDAO userDAO = new JpaUserDAOImpl();
    private CarDAO carDAO = new JpaCarDAOImpl();
    private Bill bill;
    private User user;
    private Car car;

    @BeforeEach
    public void setUp() {
        user = createUser();
        userDAO.save(user);
        car = createCar();
        carDAO.save(car);
        bill = createBill(user, car);
    }

    @Test
    public void get() {
        billDAO.save(bill);
        Bill mockBill = Mockito.mock(Bill.class);
        assertThat(billDAO.get(bill.getId())).isPresent();
        assertThat(billDAO.get(bill.getId()).orElse(mockBill))
                .isEqualToIgnoringGivenFields(bill, "car", "user");
    }

    @Test
    public void getAll() {
        billDAO.save(bill);
        carDAO.save(car);
        Bill anotherBill = new Bill(LocalDate.now(), BigDecimal.valueOf(1.23), user, car);
        billDAO.save(anotherBill);
        List<Bill> bills = new ArrayList<>();
        bills.add(bill);
        bills.add(anotherBill);
        assertThat(billDAO.getAll()).usingElementComparatorIgnoringFields("car", "user").isEqualTo(bills);
    }

    @Test
    public void save() {
        assertThat(billDAO.save(bill)).isSameAs(bill);
    }

    @Test
    public void delete() {
        billDAO.save(bill);
        assertThat(billDAO.delete(bill.getId())).isTrue();
    }
}