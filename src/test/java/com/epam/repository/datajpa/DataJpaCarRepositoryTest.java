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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;

import static com.epam.config.Profiles.SPRING_DATA_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ActiveProfiles(SPRING_DATA_PROFILE)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
@SqlGroup(@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/clearDB.sql"))
public class DataJpaCarRepositoryTest {
    private Car car;

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        car = ModelUtilityClass.createCar();
    }

    @Test
    public void findAllShouldReturnListWithCars() {
        Car anotherCar = ModelUtilityClass.createCar();
        anotherCar.setRegistrationNumber("33242");
        List<Car> carList = Arrays.asList(car, anotherCar);
        carList.forEach(car -> carRepository.save(car));

        assertThat(carRepository.findAll()).isEqualTo(carList);
    }

    @Test
    public void saveWithSameRegistrationNumbersShouldFail() {
        Car anotherCar = ModelUtilityClass.createCar();
        List<Car> carList = Arrays.asList(car, anotherCar);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> carList.forEach(car -> carRepository.save(car)));
    }

    @Test
    public void saveShouldReturnCar() {
        carRepository.save(car);

        assertThat(carRepository.findAll().size()).isEqualTo(1);
        assertThat(carRepository.existsById(car.getId())).isTrue();
        assertThat(carRepository.findById(car.getId())
                .orElse(Mockito.mock(Car.class)))
                .isEqualTo(car);
    }

    @Test
    public void existsByIdShouldReturnCar() {
        carRepository.save(car);

        assertThat(carRepository.existsById(car.getId())).isTrue();
    }

    @Test
    public void existsByIdShouldFail() {
        assertThat(carRepository.existsById(10000L)).isFalse();
    }


    @Test
    public void deleteByIdShouldDeleteCar() {
        carRepository.save(car);
        assertThat(carRepository.existsById(car.getId())).isTrue();

        carRepository.deleteById(car.getId());
        assertThat(carRepository.existsById(car.getId())).isFalse();
    }


    @Test
    public void deleteByIdShouldFail() {
        carRepository.save(car);
        assertThat(carRepository.existsById(car.getId())).isTrue();

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> carRepository.deleteById(10000L));
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillRepository billRepository;

    @Test
    public void deleteWithBillsCascadeAndUserNotDeleted() {
        User user = ModelUtilityClass.createUser();
        user = userRepository.save(user);
        Bill bill = ModelUtilityClass.createBill(user, car);

        car.addBill(bill);
        carRepository.save(car);

        assertThat(billRepository.existsById(bill.getId())).isTrue();

        carRepository.deleteById(car.getId());
        assertThat(billRepository.existsById(bill.getId())).isFalse();
        assertThat(userRepository.existsById(user.getId())).isTrue();
    }

}
