package com.epam.service;

import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import com.epam.repository.UserRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.epam.util.ModelUtilityClass.createPassport;
import static com.epam.util.ModelUtilityClass.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PassportServiceTest {
    private Passport passport;

    private final Comparator<Passport> passportComparator = Comparator
            .comparing(Passport::getSurname)
            .thenComparing(Passport::getName)
            .thenComparing(Passport::getPassportNumber);

    @Autowired
    private PassportService passportService;

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(createUser());
        passport = createPassport(user);
    }

    @Test
    @Transactional
    public void findAllShouldReturnSortedListOfPassports() {
        List<Passport> passports = passportRepository.findAll();
        assertThat(passports).isNotEmpty();

        passports.sort(passportComparator);
        assertThat(passportService.findAll()).isEqualTo(passports);
    }

    @Test
    @Transactional
    public void findAllByUserShouldReturnSortedList() throws NotFoundException {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("user not found"));
        List<Passport> passports = passportRepository.findAllByUser(user);
        assertThat(passports).isNotEmpty();

        passports.sort(passportComparator);
        assertThat(passportService.findAllByUser(user)).isEqualTo(passports);
    }

    @Test
    public void savePassportShouldSaveNewPassport() {
        assertThat(passportService.save(passport)).isEqualTo(passport);
    }

    @Test
    public void savePassportShouldThrowExceptionIfPassportNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> passportService.save(null));
    }

    @Test
    public void existsByIdShouldReturnTrueIfExists() {
        passportService.save(passport);
        assertThat(passportService.existsById(passport.getId())).isTrue();
    }

    @Test
    public void existsByIdShouldReturnFalseIfNotExists() {
        assertThat(passportService.existsById(10000L)).isFalse();
    }

    @Test
    public void deleteShouldDeletePassport() {
        Long passportId = 1L;
        passportService.deleteById(passportId);
        assertThat(passportService.findById(passportId)).isEmpty();
    }
}