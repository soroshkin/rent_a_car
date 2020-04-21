package com.epam.dao;

import com.epam.DatabaseSetupExtension;
import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.epam.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JpaCarDAOTest {
    private JpaCarDAO jpaCarDAO = new JpaCarDAO();
    private Car car;

    @RegisterExtension
    DatabaseSetupExtension databaseSetupExtension = new DatabaseSetupExtension();

    @BeforeEach
    public void setEntityManager() {
        jpaCarDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        car = createCar();
    }

    @Test
    public void saveCar() {
        assertThat(jpaCarDAO.save(car)).isSameAs(car);
    }

    @Test
    public void deleteCarIfHasBills() {
        JpaBillDAO jpaBillDAO = new JpaBillDAO();
        jpaBillDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        JpaUserDAO jpaUserDAO = new JpaUserDAO();
        jpaUserDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        jpaCarDAO.save(car);
        User user = createUser();
        jpaUserDAO.save(user);
        Bill bill = createBill(user, car);
        jpaBillDAO.save(bill);
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> jpaCarDAO.delete(car.getId()));
    }

    @Test
    public void deleteCar() {
        jpaCarDAO.save(car);
        assertThat(jpaCarDAO.delete(car.getId())).isTrue();
    }


    @Test
    public void getCar() {
        Car mockCar = Mockito.mock(Car.class);
        jpaCarDAO.save(car);
        assertThat(jpaCarDAO.get(1L).isPresent()).isTrue();
        assertThat(jpaCarDAO.get(1L).orElse(mockCar)).isEqualTo(car);
    }

    @Test
    public void getAllCars() {
        String model = "Ferrari";
        LocalDate productionDate = LocalDate.now().minus(1, ChronoUnit.YEARS);
        Car car = new Car(model, productionDate, 10000);
        Car anotherCar = new Car(model, productionDate, 1333);
        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(anotherCar);
        jpaCarDAO.save(car);
        jpaCarDAO.save(anotherCar);
        assertThat(jpaCarDAO.getAll()).isEqualTo(cars);
    }
}
