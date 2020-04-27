package com.epam.parsers;

import com.epam.EntityManagerSetupExtension;
import com.epam.ModelUtilityClass;
import com.epam.dao.PassportDAO;
import com.epam.dao.UserDAO;
import com.epam.dao.jpa.JpaPassportDAOImpl;
import com.epam.dao.jpa.JpaUserDAOImpl;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.utils.EntityManagerUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
class JacksonObjectMapperTest {
    private static User USER = ModelUtilityClass.createUser();
    private static final String FILE_PATH = "JSON_user.json";
    private UserDAO userDAO = new JpaUserDAOImpl();

    @Test
    void singleEntitySerializationDeserialization() throws IOException {
        userDAO.save(USER);
        JacksonObjectMapper<User> jacksonObjectMapper = new JacksonObjectMapper<>();
        jacksonObjectMapper.serialize(USER, FILE_PATH);
        User userFromJSON = jacksonObjectMapper.deserialize(new File(FILE_PATH), new TypeReference<User>() {
        });
        assertThat(userDAO.get(USER.getId()).orElse(Mockito.mock(User.class))).isEqualTo(userFromJSON);
    }

    @Test
    void saveSingleEntityInDatabaseFromFile() throws IOException {
        userDAO.save(USER);
        JacksonObjectMapper<User> jacksonObjectMapper = new JacksonObjectMapper<>();
        jacksonObjectMapper.serialize(USER, FILE_PATH);
        userDAO.delete(USER.getId());

        User userFromJSON = jacksonObjectMapper.deserialize(new File(FILE_PATH), new TypeReference<User>() {
        });
        userDAO.save(userFromJSON);

        assertThat(userDAO.getAll().size()).isEqualTo(1);
        assertThat(userDAO.getByEmail(USER.getEmail())
                .orElse(Mockito.mock(User.class)))
                .isEqualTo(userFromJSON);
    }

    @Test
    void listSerializationDeserialization() throws IOException {
        JacksonObjectMapper<User> jacksonObjectMapper = new JacksonObjectMapper<>();
        populatePassports();
        jacksonObjectMapper.serialize(USER, FILE_PATH);
        User userFromJSON = jacksonObjectMapper.deserialize(new File(FILE_PATH), new TypeReference<User>() {
        });
        assertThat(userDAO.get(USER.getId()).orElse(Mockito.mock(User.class))).isEqualTo(userFromJSON);
    }

    @Test
    void saveListInDatabaseFromFile() {
        populatePassports();

        PassportDAO passportDAO = new JpaPassportDAOImpl();
        JacksonObjectMapper<List<Passport>> jacksonObjectMapper = new JacksonObjectMapper<>();

        EntityManagerUtil.executeInTransaction(entityManager -> {
            List<Passport> passports = entityManager
                    .createNamedQuery(Passport.GET_ALL, Passport.class)
                    .getResultList();
            List<Passport> passportsFromFile = null;
            try {
                passports.forEach(passport -> passportDAO.delete(passport.getId()));
                jacksonObjectMapper.serialize(passports, FILE_PATH);
                passportsFromFile = jacksonObjectMapper.deserialize(new File(FILE_PATH), new TypeReference<List<Passport>>() {
                });
                passportsFromFile.forEach(passportDAO::save);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertThat(passportDAO.getAll().size()).isEqualTo(3);
            return assertThat(passportDAO.getAll()).isEqualTo(passportsFromFile);
        });
    }

    private void populatePassports() {
        Set<Passport> passportSet = new HashSet<>();
        passportSet.add(ModelUtilityClass.createPassport("1", USER));
        passportSet.add(ModelUtilityClass.createPassport("2", USER));
        passportSet.add(ModelUtilityClass.createPassport("3", USER));
        USER.setPassports(passportSet);
        userDAO.save(USER);
    }
}