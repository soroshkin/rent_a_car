package com.epam.dao;

import com.epam.EntityManagerSetupExtension;
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
    private JpaPassportDAO jpaPassportDAO = new JpaPassportDAO();
    private JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private Passport passport;
    private User user;

    @BeforeEach
    public void setUp() {
        user = createUser();
        jpaUserDAO.save(user);
    }

    @Test
    public void savePassport() {
        passport = createPassport(user);
        assertThat(jpaPassportDAO.save(passport)).isSameAs(passport);
    }

    @Test
    public void deletePassport() {
        passport = createPassport(user);
        jpaPassportDAO.save(passport);
        assertThat(jpaPassportDAO.delete(passport.getId())).isTrue();
    }

    @Test
    public void getPassport() {
        passport = createPassport(user);
        jpaPassportDAO.save(passport);
        Passport mockPassport = Mockito.mock(Passport.class);
        System.out.println(jpaPassportDAO.getAll());
        assertThat(jpaPassportDAO.get(2L).isPresent()).isTrue();
        assertThat(jpaPassportDAO.get(2L).orElse(mockPassport)).isEqualTo(passport);
    }

    @Test
    public void getAllPassports() {
        String stringParameter = "some parameter";
        Passport passport = new Passport("1", stringParameter, stringParameter, stringParameter, user);
        Passport anotherPassport = new Passport("2", stringParameter, stringParameter, stringParameter, user);
        List<Passport> passports = new ArrayList<>();
        passports.add(passport);
        passports.add(anotherPassport);
        jpaPassportDAO.save(passport);
        jpaPassportDAO.save(anotherPassport);
        assertThat(jpaPassportDAO.getAll()).isEqualTo(passports);
    }
}
