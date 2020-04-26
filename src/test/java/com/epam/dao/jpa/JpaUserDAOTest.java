package com.epam.dao.jpa;

import com.epam.EntityManagerSetupExtension;
import com.epam.dao.CarDAO;
import com.epam.dao.UserDAO;
import com.epam.dao.jpa.JpaCarDAOImpl;
import com.epam.dao.jpa.JpaUserDAOImpl;
import com.epam.model.Car;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.utils.EntityManagerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaUserDAOTest {
    UserDAO userDAO = new JpaUserDAOImpl();
    private User user;

    @BeforeEach
    public void setUp() {
        user = createUser();
    }

    @Test
    public void save() {
        assertThat(userDAO.save(user)).isEqualTo(user);
    }

    @Test
    public void getByEmail() {
        userDAO.save(user);
        User mockUser = Mockito.mock(User.class);
        assertThat(userDAO.getByEmail(user.getEmail()).orElse(mockUser)).isEqualTo(user);
    }

    @Test
    public void deleteIfHasNoBillsPassports() {
        userDAO.save(user);
        assertThat(userDAO.delete(user.getId())).isTrue();
    }

    @Test
    public void deleteIfHasBills() {
        Car car = createCar();
        CarDAO carDAO = new JpaCarDAOImpl();
        carDAO.save(car);
        createBill(user, car);
        userDAO.save(user);
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> userDAO.delete(user.getId()));
    }

    @Test
    public void deleteIfHasPassports() {
        user.addPassport(createPassport(user));
        userDAO.save(user);
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> userDAO.delete(user.getId()));
    }

    @Test
    public void get() {
        User mockUser = Mockito.mock(User.class);
        userDAO.save(user);
        assertThat(userDAO.get(user.getId()).isPresent()).isTrue();
        assertThat(userDAO.get(user.getId()).orElse(mockUser)).isEqualTo(user);
    }

    @Test
    public void getAll() {
        String email = "email@domain.com";
        LocalDate dateOfBirth = LocalDate.now().minus(1, ChronoUnit.YEARS);
        User user = new User(email, dateOfBirth);
        User anotherUser = new User(email + "2", dateOfBirth);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(anotherUser);
        userDAO.save(user);
        userDAO.save(anotherUser);
        assertThat(userDAO.getAll()).isEqualTo(users);
    }

    @Test
    public void removeOrphanPassports() {
        Passport passport = createPassport(user);
        user.addPassport(passport);
        userDAO.save(user);
        user.removePassport(passport);
        userDAO.save(user);
        int passportsNumber = EntityManagerUtil.executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(Passport.GET_ALL)
                .getResultList()
                .size());
        assertThat(passportsNumber).isEqualTo(0);
    }
}
