package com.epam.repository.jpa;

import com.epam.EntityManagerSetupExtension;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import com.epam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static com.epam.ModelUtilityClass.createPassport;
import static com.epam.ModelUtilityClass.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaPassportRepositoryTest {
    private PassportRepository passportRepository = new JpaPassportRepositoryImpl();
    private UserRepository userRepository = new JpaUserRepositoryImpl();
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
        passportRepository.save(passport);
        Passport mockPassport = Mockito.mock(Passport.class);
        System.out.println(passportRepository.findAll());
        assertThat(passportRepository.findById(1L).isPresent()).isTrue();
        assertThat(passportRepository.findById(1L).orElse(mockPassport)).isEqualTo(passport);
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
