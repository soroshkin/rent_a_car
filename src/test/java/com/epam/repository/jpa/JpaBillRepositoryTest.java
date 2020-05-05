package com.epam.repository.jpa;

import com.epam.EntityManagerSetupExtension;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.epam.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaBillRepositoryTest {
    private BillRepository billRepository = new JpaBillRepositoryImpl();
    private UserRepository userRepository = new JpaUserRepositoryImpl();
    private CarRepository carRepository = new JpaCarRepositoryImpl();
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
    public void get() {
        billRepository.save(bill);
        Bill mockBill = Mockito.mock(Bill.class);
        assertThat(billRepository.findById(bill.getId())).isPresent();
        assertThat(billRepository.findById(bill.getId()).orElse(mockBill))
                .isEqualToIgnoringGivenFields(bill, "car", "user");
    }

    @Test
    public void getAll() {
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
    public void save() {
        assertThat(billRepository.save(bill)).isSameAs(bill);
    }

    @Test
    public void delete() {
        billRepository.save(bill);
        billRepository.deleteById(bill.getId());
        assertThat(billRepository.findById(bill.getId())).isEmpty();
    }
}