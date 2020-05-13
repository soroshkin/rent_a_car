package com.epam.service;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
import com.epam.repository.CarRepository;
import com.epam.repository.UserRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.epam.util.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BillServiceTest {
    private Bill bill;

    @Autowired
    private BillService billService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(createUser());
        Car car = carRepository.save(createCar());
        bill = createBill(user, car);
    }

    @Test
    @Transactional
    public void findAllShouldReturnSortedList() {
        List<Bill> bills = billRepository.findAll();
        assertThat(bills).isNotEmpty();

        bills.sort(Comparator.comparing(Bill::getDate)
                .thenComparing(bill -> bill.getUser().getEmail())
                .thenComparing(bill -> bill.getCar().getModel()));
        assertThat(billService.findAll()).isEqualTo(bills);
    }

    @Test
    @Transactional
    public void findAllByUserShouldReturnSortedList() throws NotFoundException {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("user not found"));
        List<Bill> bills = billRepository.findAllByUser(user);
        assertThat(bills).isNotEmpty();

        bills.sort(Comparator.comparing(Bill::getDate)
                .thenComparing(bill -> bill.getUser().getEmail())
                .thenComparing(bill -> bill.getCar().getModel()));
        assertThat(billService.findAllByUser(user)).isEqualTo(bills);
    }

    @Test
    @Transactional
    public void findAllByCarShouldReturnSortedList() throws NotFoundException {
        Car car = carRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("car not found"));
        List<Bill> bills = billRepository.findAllByCar(car);
        assertThat(bills).isNotEmpty();

        bills.sort(Comparator.comparing(Bill::getDate)
                .thenComparing(bill -> bill.getUser().getEmail())
                .thenComparing(bill -> bill.getCar().getModel()));
        assertThat(billService.findAllByCar(car)).isEqualTo(bills);
    }

    @Test
    public void saveBillShouldCreateNewBill() {
        assertThat(billService.save(bill)).isEqualTo(bill);
    }

    @Test
    public void saveBillShouldThrowExceptionIfBillNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> billService.save(null));
    }

    @Test
    public void existsByIdShouldReturnTrueIfExists() {
        billService.save(bill);
        assertThat(billService.existsById(bill.getId())).isTrue();
    }

    @Test
    public void existsByIdShouldReturnFalseIfNotExists() {
        assertThat(billService.existsById(10000L)).isFalse();
    }

    @Test
    public void deleteShouldDeleteBill() {
        billService.save(bill);
        billService.deleteById(bill.getId());
        assertThat(billService.findById(bill.getId())).isEmpty();
    }
}