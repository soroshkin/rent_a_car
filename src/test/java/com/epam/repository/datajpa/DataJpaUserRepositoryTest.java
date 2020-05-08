package com.epam.repository.datajpa;

import com.epam.config.WebConfig;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import com.epam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.epam.config.Profiles.SPRING_DATA_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(SPRING_DATA_PROFILE)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
@SqlGroup(@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/clearDB.sql"))
public class DataJpaUserRepositoryTest {
    private User user;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassportRepository passportRepository;

    @BeforeEach
    void setUp() {
        user = new User("dfas@esdf", LocalDate.now().minus(1, ChronoUnit.YEARS));
    }

    @Test
    public void saveAndDeleteUserShouldCreateAndDeleteUser() {
        user = userRepository.save(user);
        assertThat(userRepository.existsById(user.getId())).isTrue();

        userRepository.deleteById(user.getId());
        assertThat(userRepository.existsById(user.getId())).isFalse();
    }

    @Test
    public void deleteUserWithPassportsShouldRemoveCascade() {
        Passport passport = new Passport("1", "some address", "Semen", "Fedorov", user);
        user.addPassport(passport);

        userRepository.save(user);
        userRepository.deleteById(user.getId());

        assertThat(userRepository.existsById(user.getId())).isFalse();
        assertThat(passportRepository.findAllByUser(user).size()).isEqualTo(0);
    }

    @Test
    public void updateUserWithPassportsShouldReturnUpdatedUser() {
        user = userRepository.save(user);
        String anotherEmail = "another@email.ru";
        user.setEmail(anotherEmail);
        userRepository.save(user);

        assertThat(userRepository.existsById(user.getId())).isTrue();
        assertThat(userRepository.findById(user.getId())
                .orElse(Mockito.mock(User.class))
                .getEmail())
                .isEqualTo(anotherEmail);
    }
}
