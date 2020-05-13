package com.epam.repository.jpacontainer;

import com.epam.config.WebConfig;
import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
import com.epam.repository.CarRepository;
import com.epam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.epam.config.Profiles.JPA_PROFILE;
import static com.epam.util.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(JPA_PROFILE)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@SqlGroup(@Sql(scripts = "classpath:db/clearDB.sql"))
public class JpaContainerBillRepositoryTest {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    private Bill bill;
    private User user;
    private Car car;

    @BeforeEach
    public void setUp() {
        user = createUser();
        userRepository.save(user);
        car = createCar();
        carRepository.save(car);
        bill = createBill(user, car);
    }

    @Test
    public void findByIdShouldReturnBillWithGivenId() {
        billRepository.save(bill);
        Bill mockBill = Mockito.mock(Bill.class);
        assertThat(billRepository.findById(bill.getId())).isPresent();
        assertThat(billRepository.findById(bill.getId()).orElse(mockBill))
                .isEqualToIgnoringGivenFields(bill, "car", "user");
    }

    @Test
    public void findAllShouldReturnListOfBills() {
        billRepository.save(bill);
        carRepository.save(car);
        Bill anotherBill = new Bill(LocalDate.now(), BigDecimal.valueOf(1.23), user, car);
        billRepository.save(anotherBill);
        List<Bill> bills = new ArrayList<>();
        bills.add(bill);
        bills.add(anotherBill);
        assertThat(billRepository.findAll()).usingElementComparatorIgnoringFields("car", "user").isEqualTo(bills);
    }

    @Test
    public void saveBillShouldSaveBill() {
        assertThat(bill).isSameAs(bill);
    }

    @Test
    public void deleteShouldDeleteBill() {
        billRepository.save(bill);
        billRepository.deleteById(bill.getId());
        assertThat(billRepository.findById(bill.getId())).isEmpty();
    }
}