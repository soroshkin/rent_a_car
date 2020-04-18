package com.epam.dao;

import com.epam.DatabaseSetupExtension;
import com.epam.model.Passport;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static com.epam.ModelUtilityClass.createPassport;
import static com.epam.ModelUtilityClass.createUser;
import static org.assertj.core.api.Assertions.assertThat;

public class JpaPassportDAOTest{
    private JpaPassportDAO jpaPassportDAO = new JpaPassportDAO();
    private Passport passport;
    private User user;

    @RegisterExtension
    DatabaseSetupExtension databaseSetupExtension = new DatabaseSetupExtension();

    @BeforeEach
    public void setEntityManager() {
        jpaPassportDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        passport = createPassport();
        user = createUser();
    }

    @Test
    public void save_passport_test() {
        assertThat(jpaPassportDAO.save(passport)).isSameAs(passport);
    }

    @Test
    public void delete_passport() {
        jpaPassportDAO.save(passport);
        assertThat(jpaPassportDAO.delete(passport.getId())).isTrue();
    }

    @Test
    public void get_passport(){
        Passport mockPassport = Mockito.mock(Passport.class);
        jpaPassportDAO.save(passport);
        assertThat(jpaPassportDAO.get(1L).isPresent()).isTrue();
        assertThat(jpaPassportDAO.get(1L).orElse(mockPassport)).isEqualTo(passport);
    }

    @Test
    public void getAll_passports(){
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
