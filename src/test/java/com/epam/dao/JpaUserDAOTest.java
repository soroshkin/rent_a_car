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

public class JpaUserDAOTest {
    JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private User user;

    @RegisterExtension
    DatabaseSetupExtension databaseSetupExtension = new DatabaseSetupExtension();

    @BeforeEach
    public void setEntityManager() {
        jpaUserDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        user = createUser();
    }

    @Test
    public void save() {
        jpaUserDAO.save(user);
        assertThat(jpaUserDAO.save(user)).isSameAs(user);
    }

    @Test
    public void deleteIfHasNoBillsPassports() {
        jpaUserDAO.save(user);
        assertThat(jpaUserDAO.delete(user.getId())).isTrue();
    }

    @Test
    public void deleteIfHasBills() {
        Car car = createCar();
        JpaCarDAO jpaCarDAO = new JpaCarDAO();
        jpaCarDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        jpaCarDAO.save(car);
        Bill bill = createBill(user, car);
        user.addBill(bill);
        jpaUserDAO.save(user);
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> jpaUserDAO.delete(user.getId()));
    }

    @Test
    public void deleteIfHasPassports() {
        user.addPassport(createPassport(user));
        jpaUserDAO.save(user);
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> jpaUserDAO.delete(user.getId()));
    }

    @Test
    public void get() {
        User mockUser = Mockito.mock(User.class);
        jpaUserDAO.save(user);
        assertThat(jpaUserDAO.get(user.getId()).isPresent()).isTrue();
        assertThat(jpaUserDAO.get(user.getId()).orElse(mockUser)).isEqualTo(user);
    }

    @Test
    public void getAll() {
        String email = "email@domain.com";
        LocalDate dateOfBirth = LocalDate.now().minus(1, ChronoUnit.YEARS);
        User user = new User(email, dateOfBirth);
        User anotherUser = new User(email, dateOfBirth);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(anotherUser);
        jpaUserDAO.save(user);
        jpaUserDAO.save(anotherUser);
        assertThat(jpaUserDAO.getAll()).isEqualTo(users);
    }
}
