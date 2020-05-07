package com.epam.repository.datajpa;

import com.epam.config.WebConfig;
import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
import com.epam.repository.CarRepository;
import com.epam.repository.UserRepository;
import com.epam.util.ModelUtilityClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
@SqlGroup(@Sql(scripts = "classpath:db/clearDB.sql"))
public class DataJpaBillRepositoryTest {
    private Bill bill;
    private User user;
    private Car car;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    UserRepository userRepository;

    public DataJpaBillRepositoryTest() {
    }

    @BeforeEach
    void setUp() {
        user = userRepository.save(ModelUtilityClass.createUser());
        car = carRepository.save(ModelUtilityClass.createCar());
        bill = ModelUtilityClass.createBill(user, car);
    }

    @Test
    public void findAllShouldReturnBillList() {
        Bill anotherBill = new Bill(LocalDate.now(), BigDecimal.ZERO, user, car);
        List<Bill> bills = Arrays.asList(bill, anotherBill);
        bills.forEach(bill -> billRepository.save(bill));

        assertThat(billRepository.findAll().size()).isEqualTo(2);
        assertThat(billRepository.findAll().get(0).getDate()).isEqualTo(bills.get(0).getDate());
    }

    @Test
    public void saveShouldReturnBill() {
        billRepository.save(bill);

        assertThat(billRepository.findAll().size()).isEqualTo(1);
        assertThat(billRepository.existsById(bill.getId())).isTrue();
        assertThat(billRepository.findById(bill.getId())
                .orElse(Mockito.mock(Bill.class)).getDate())
                .isEqualTo(bill.getDate());
    }

    @Test
    public void existsByIdShouldReturnBill() {
        billRepository.save(bill);

        assertThat(billRepository.existsById(bill.getId())).isTrue();
    }

    @Test
    public void existsByIdShouldFail() {
        assertThat(billRepository.existsById(10000L)).isFalse();
    }

    @Test
    public void deleteByIdShouldDeleteBill() {
        billRepository.save(bill);
        assertThat(billRepository.existsById(bill.getId())).isTrue();

        billRepository.deleteById(bill.getId());
        assertThat(billRepository.existsById(bill.getId())).isFalse();
    }

    @Test
    public void deleteByIdShouldFail() {
        billRepository.save(bill);
        assertThat(billRepository.existsById(bill.getId())).isTrue();

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> billRepository.deleteById(10000L));
    }
}
