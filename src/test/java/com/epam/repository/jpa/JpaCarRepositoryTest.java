package com.epam.repository.jpa;

import com.epam.extension.EntityManagerSetupExtension;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.epam.util.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaCarRepositoryTest {
    private CarRepository carRepository = new JpaCarRepositoryImpl();
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
        BillRepository billRepository = new JpaBillRepositoryImpl();
        UserRepository userRepository = new JpaUserRepositoryImpl();
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
        carRepository.save(car);
        assertThat(carRepository.findById(1L).isPresent()).isTrue();
        assertThat(carRepository.findById(1L).orElse(mockCar)).isEqualTo(car);
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
