package com.epam.repository.datajpa;

import com.epam.config.WebConfig;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import com.epam.repository.UserRepository;
import com.epam.util.ModelUtilityClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ContextConfiguration(classes = WebConfig.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SqlGroup(@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/clearDB.sql"))
public class DataJpaPassportRepositoryTest {
    private User user;
    private Passport passport;
    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(ModelUtilityClass.createUser());
        passport = ModelUtilityClass.createPassport(user);
    }

    @Test
    public void saveShouldReturnSavedPassport() {
        assertThat(passportRepository.save(passport)).isEqualTo(passport);
    }

    @Test
    public void findByIdShouldReturnPassport() {
        passport = passportRepository.save(passport);
        assertThat(passportRepository.findById(passport.getId())
                .orElse(Mockito.mock(Passport.class)))
                .isEqualTo(passport);
    }

    @Test
    public void findAllShouldReturnAllPassports() {
        Passport anotherPassport = ModelUtilityClass.createPassport(user);
        anotherPassport.setPassportNumber("REE32121");
        List<Passport> passportList = Arrays.asList(passport, anotherPassport);
        passportRepository.save(passport);
        passportRepository.save(anotherPassport);

        assertThat(passportRepository.findAll().size()).isEqualTo(passportList.size());
    }

    @Test
    public void findAllByUserShouldReturnListOfPassports() {
        Passport anotherPassport = ModelUtilityClass.createPassport(user);
        anotherPassport.setPassportNumber("REE32121");
        List<Passport> passportList = Arrays.asList(passport, anotherPassport);
        passportRepository.save(passport);
        passportRepository.save(anotherPassport);

        assertThat(passportRepository.findAllByUser(user).size()).isEqualTo(passportList.size());
    }

    @Test
    public void findAllByUserShouldReturnEmptyList() {
        assertThat(passportRepository.findAllByUser(user)).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void existsByIdReturnTrue() {
        passportRepository.save(passport);
        assertThat(passportRepository.existsById(passport.getId())).isTrue();
    }

    @Test
    public void existsByIdReturnFalse() {
        assertThat(passportRepository.existsById(1L)).isFalse();
    }

    @Test
    public void existsByIdShouldFail() {
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> passportRepository.existsById(null));
    }

    @Test
    public void deleteByIdShouldDeleteAndReturnEmptyList() {
        passportRepository.save(passport);
        assertThat(passportRepository.findAll().size()).isEqualTo(1);

        passportRepository.deleteById(passport.getId());
        assertThat(passportRepository.findAll()).isEqualTo(Collections.EMPTY_LIST);
    }

}
