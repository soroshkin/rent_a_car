package com.epam.dao;

import com.epam.EntityManagerSetupExtension;
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

import static com.epam.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaUserDAOTest {
    JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private User user;

    @BeforeEach
    public void setUp() {
        user = createUser();
    }

    @Test
    public void save() {
        jpaUserDAO.save(user);
        assertThat(jpaUserDAO.save(user)).isEqualTo(user);
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
        jpaCarDAO.save(car);
        createBill(user, car);
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
        User anotherUser = new User(email + "2", dateOfBirth);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(anotherUser);
        jpaUserDAO.save(user);
        jpaUserDAO.save(anotherUser);
        assertThat(jpaUserDAO.getAll()).isEqualTo(users);
    }

    @Test
    public void removeOrphanPassports() {
        Passport passport = createPassport(user);
        user.addPassport(passport);
        jpaUserDAO.save(user);
        user.removePassport(passport);
        jpaUserDAO.save(user);
        int passportsNumber = EntityManagerUtil.executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(Passport.GET_ALL)
                .getResultList()
                .size());
        assertThat(passportsNumber).isEqualTo(0);
    }
}
