package com.epam.repository.jpa;

import com.epam.extension.EntityManagerSetupExtension;
import com.epam.model.Car;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.CarRepository;
import com.epam.repository.UserRepository;
import com.epam.utils.EntityManagerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.epam.util.ModelUtilityClass.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaUserRepositoryTest {
    UserRepository userRepository = new JpaUserRepositoryImpl();
    private User user;

    @BeforeEach
    public void setUp() {
        user = createUser();
    }

    @Test
    public void save() {
        assertThat(userRepository.save(user)).isEqualTo(user);
    }

    @Test
    public void getByEmail() {
        userRepository.save(user);
        User mockUser = Mockito.mock(User.class);
        assertThat(userRepository.findByEmail(user.getEmail()).orElse(mockUser)).isEqualTo(user);
    }

    @Test
    public void deleteIfHasNoBillsPassports() {
        userRepository.save(user);
        userRepository.deleteById(user.getId());
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    public void deleteIfHasBills() {
        Car car = createCar();
        CarRepository carRepository = new JpaCarRepositoryImpl();
        carRepository.save(car);
        createBill(user, car);
        userRepository.save(user);
        userRepository.save(user);
        userRepository.deleteById(user.getId());
        assertThat(userRepository.existsById(user.getId())).isFalse();
    }

    @Test
    public void deleteIfHasPassports() {
        user.addPassport(createPassport(user));
        userRepository.save(user);
        userRepository.deleteById(user.getId());
        assertThat(userRepository.existsById(user.getId())).isFalse();
    }

    @Test
    public void get() {
        User mockUser = Mockito.mock(User.class);
        userRepository.save(user);
        assertThat(userRepository.findById(user.getId()).isPresent()).isTrue();
        assertThat(userRepository.findById(user.getId()).orElse(mockUser)).isEqualTo(user);
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
        userRepository.save(user);
        userRepository.save(anotherUser);
        assertThat(userRepository.findAll()).isEqualTo(users);
    }

    @Test
    public void removeOrphanPassports() {
        Passport passport = createPassport(user);
        user.addPassport(passport);
        userRepository.save(user);
        user.removePassport(passport);
        userRepository.save(user);
        int passportsNumber = EntityManagerUtil.executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(Passport.FIND_ALL)
                .getResultList()
                .size());
        assertThat(passportsNumber).isEqualTo(0);
    }

    @Test
    public void checkIfExistsById() {
        userRepository.save(user);
        assertThat(userRepository.existsById(user.getId())).isTrue();
    }
}
