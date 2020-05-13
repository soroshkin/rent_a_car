package com.epam.repository.jpacontainer;

import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import com.epam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.ArrayList;
import java.util.List;

import static com.epam.config.Profiles.JPA_PROFILE;
import static com.epam.util.ModelUtilityClass.createPassport;
import static com.epam.util.ModelUtilityClass.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(JPA_PROFILE)
@SpringBootTest
@SqlGroup(@Sql(scripts = "classpath:db/clearDB.sql"))
public class JpaContainerPassportRepositoryTest {
    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private UserRepository userRepository;

    private Passport passport;
    private User user;

    @BeforeEach
    public void setUp() {
        user = createUser();
        userRepository.save(user);
    }

    @Test
    public void savePassport() {
        passport = createPassport(user);
        assertThat(passportRepository.save(passport)).isSameAs(passport);
    }

    @Test
    public void deletePassport() {
        passport = createPassport(user);
        passportRepository.save(passport);
        passportRepository.deleteById(passport.getId());
        assertThat(passportRepository.findById(passport.getId())).isEmpty();
    }

    @Test
    public void getPassport() {
        passport = createPassport(user);
        passport = passportRepository.save(passport);
        Passport mockPassport = Mockito.mock(Passport.class);
        assertThat(passportRepository.findById(passport.getId()).isPresent()).isTrue();
        assertThat(passportRepository.findById(passport.getId()).orElse(mockPassport)).isEqualTo(passport);
    }

    @Test
    public void getAllPassports() {
        String stringParameter = "some parameter";
        Passport passport = new Passport("1", stringParameter, stringParameter, stringParameter, user);
        Passport anotherPassport = new Passport("2", stringParameter, stringParameter, stringParameter, user);
        List<Passport> passports = new ArrayList<>();
        passports.add(passport);
        passports.add(anotherPassport);
        passportRepository.save(passport);
        passportRepository.save(anotherPassport);
        assertThat(passportRepository.findAll()).containsExactlyInAnyOrder(passports.toArray(new Passport[0]));
    }
}