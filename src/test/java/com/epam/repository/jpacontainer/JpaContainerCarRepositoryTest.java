package com.epam.repository.jpacontainer;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
import com.epam.repository.CarRepository;
import com.epam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.epam.config.Profiles.JPA_PROFILE;
import static com.epam.util.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(JPA_PROFILE)
@SpringBootTest
@SqlGroup(@Sql(scripts = "classpath:db/clearDB.sql"))
public class JpaContainerCarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    private Car car;

    @BeforeEach
    public void setUp() {
        car = createCar();
    }

    @Test
    public void saveCar() {
        assertThat(carRepository.save(car)).isSameAs(car);
    }

    @Test
    public void deleteCarIfHasBills() {

        carRepository.save(car);
        User user = createUser();
        userRepository.save(user);
        Bill bill = createBill(user, car);
        billRepository.save(bill);
        carRepository.deleteById(car.getId());
        assertThat(carRepository.existsById(car.getId())).isFalse();
    }

    @Test
    public void deleteCar() {
        carRepository.save(car);
        carRepository.deleteById(car.getId());
        assertThat(carRepository.existsById(car.getId())).isFalse();
    }


    @Test
    public void getCar() {
        Car mockCar = Mockito.mock(Car.class);
        car = carRepository.save(car);
        assertThat(carRepository.findById(car.getId()).isPresent()).isTrue();
        assertThat(carRepository.findById(car.getId()).orElse(mockCar)).isEqualTo(car);
    }

    @Test
    public void getAllCars() {
        String model = "Ferrari";
        LocalDate productionDate = LocalDate.now().minus(1, ChronoUnit.YEARS);
        Car car = new Car(model, "A232", productionDate, 10000);
        Car anotherCar = new Car(model, "D999", productionDate, 1333);
        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(anotherCar);
        carRepository.save(car);
        carRepository.save(anotherCar);
        assertThat(carRepository.findAll()).isEqualTo(cars);
    }
}