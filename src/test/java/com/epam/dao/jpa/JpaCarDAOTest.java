package com.epam.dao.jpa;

import com.epam.EntityManagerSetupExtension;
import com.epam.dao.BillDAO;
import com.epam.dao.CarDAO;
import com.epam.dao.UserDAO;
import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.epam.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaCarDAOTest {
    private CarDAO carDAO = new JpaCarDAOImpl();
    private Car car;

    @BeforeEach
    public void setUp() {
        car = createCar();
    }

    @Test
    public void saveCar() {
        assertThat(carDAO.save(car)).isSameAs(car);
    }

    @Test
    public void deleteCarIfHasBills() {
        BillDAO billDAO = new JpaBillDAOImpl();
        UserDAO userDAO = new JpaUserDAOImpl();
        carDAO.save(car);
        User user = createUser();
        userDAO.save(user);
        Bill bill = createBill(user, car);
        billDAO.save(bill);
        assertThat(carDAO.delete(car.getId())).isTrue();
    }

    @Test
    public void deleteCar() {
        carDAO.save(car);
        assertThat(carDAO.delete(car.getId())).isTrue();
    }


    @Test
    public void getCar() {
        Car mockCar = Mockito.mock(Car.class);
        carDAO.save(car);
        assertThat(carDAO.get(1L).isPresent()).isTrue();
        assertThat(carDAO.get(1L).orElse(mockCar)).isEqualTo(car);
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
        carDAO.save(car);
        carDAO.save(anotherCar);
        assertThat(carDAO.getAll()).isEqualTo(cars);
    }
}
