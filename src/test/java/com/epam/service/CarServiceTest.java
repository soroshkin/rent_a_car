package com.epam.service;

import com.epam.model.Car;
import com.epam.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Comparator;
import java.util.List;

import static com.epam.util.ModelUtilityClass.createCar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CarServiceTest {
    private Car car;

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        car = createCar();
    }

    @Test
    public void findAllShouldReturnSortedListOfCars() {
        List<Car> cars = carRepository.findAll();
        assertThat(cars).isNotEmpty();

        cars.sort(Comparator.comparing(Car::getModel)
                .thenComparing(Car::getProductionDate)
                .thenComparing(Car::getRegistrationNumber));
        assertThat(carService.findAll()).isEqualTo(cars);
    }

    @Test
    public void saveCarShouldSaveNewCar() {
        assertThat(carService.save(car)).isEqualTo(car);
    }

    @Test
    public void saveCarShouldThrowExceptionIfCarNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> carService.save(null));
    }

    @Test
    public void existsByIdShouldReturnTrueIfExists() {
        carService.save(car);
        assertThat(carService.existsById(car.getId())).isTrue();
    }

    @Test
    public void existsByIdShouldReturnFalseIfNotExists() {
        assertThat(carService.existsById(10000L)).isFalse();
    }

    @Test
    public void deleteShouldDeleteCar() {
        carService.save(car);
        carService.deleteById(car.getId());
        assertThat(carService.findById(car.getId())).isEmpty();
    }
}