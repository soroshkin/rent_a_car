package com.epam.dao.jpa;

import com.epam.EntityManagerSetupExtension;
import com.epam.dao.PassportDAO;
import com.epam.dao.UserDAO;
import com.epam.dao.jpa.JpaPassportDAOImpl;
import com.epam.dao.jpa.JpaUserDAOImpl;
import com.epam.model.Passport;
import com.epam.model.User;
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
public class JpaPassportDAOTest {
    private PassportDAO passportDAO = new JpaPassportDAOImpl();
    private UserDAO userDAO = new JpaUserDAOImpl();
    private Passport passport;
    private User user;

    @BeforeEach
    public void setUp() {
        user = createUser();
        userDAO.save(user);
    }

    @Test
    public void savePassport() {
        passport = createPassport(user);
        assertThat(passportDAO.save(passport)).isSameAs(passport);
    }

    @Test
    public void deletePassport() {
        passport = createPassport(user);
        passportDAO.save(passport);
        assertThat(passportDAO.delete(passport.getId())).isTrue();
    }

    @Test
    public void getPassport() {
        passport = createPassport(user);
        passportDAO.save(passport);
        Passport mockPassport = Mockito.mock(Passport.class);
        System.out.println(passportDAO.getAll());
        assertThat(passportDAO.get(2L).isPresent()).isTrue();
        assertThat(passportDAO.get(2L).orElse(mockPassport)).isEqualTo(passport);
    }

    @Test
    public void getAllPassports() {
        String stringParameter = "some parameter";
        Passport passport = new Passport("1", stringParameter, stringParameter, stringParameter, user);
        Passport anotherPassport = new Passport("2", stringParameter, stringParameter, stringParameter, user);
        List<Passport> passports = new ArrayList<>();
        passports.add(passport);
        passports.add(anotherPassport);
        passportDAO.save(passport);
        passportDAO.save(anotherPassport);
        assertThat(passportDAO.getAll()).isEqualTo(passports);
    }
}
