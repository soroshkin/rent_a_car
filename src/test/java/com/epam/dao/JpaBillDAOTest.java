package com.epam.dao;

import com.epam.DatabaseSetupExtension;
import com.epam.ModelUtilityClass;
import com.epam.model.Bill;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.epam.ModelUtilityClass.*;

public class JpaBillDAOTest {
    private JpaBillDAO jpaBillDAO = new JpaBillDAO();
    private Bill bill;
    private User user;

    @RegisterExtension
    DatabaseSetupExtension databaseSetupExtension = new DatabaseSetupExtension();

    @BeforeEach
    public void setUp() {
        jpaBillDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        user = createUser();
        bill = createBill();
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
        Bill anotherBill = new Bill(LocalDate.now(), BigDecimal.valueOf(100), user);
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