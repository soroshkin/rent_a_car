package com.epam.parsers;

import com.epam.extension.EntityManagerSetupExtension;
import com.epam.util.ModelUtilityClass;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import com.epam.repository.UserRepository;
import com.epam.repository.jpa.JpaPassportRepositoryImpl;
import com.epam.repository.jpa.JpaUserRepositoryImpl;
import com.epam.utils.EntityManagerUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
class JPAJacksonObjectMapperTest {
    private static User USER = ModelUtilityClass.createUser();
    private static final String FILE_PATH = "JSON_user.json";
    private UserRepository userRepository = new JpaUserRepositoryImpl();

    @Test
    void singleEntitySerializationDeserialization() throws IOException {
        userRepository.save(USER);
        JPAJacksonObjectMapper<User> JPAJacksonObjectMapper = new JPAJacksonObjectMapper<>();
        JPAJacksonObjectMapper.serializeToFile(USER, FILE_PATH);
        User userFromJSON = JPAJacksonObjectMapper.deserializeFromFile(new File(FILE_PATH), new TypeReference<User>() {
        });
        assertThat(userRepository.findById(USER.getId()).orElse(Mockito.mock(User.class))).isEqualTo(userFromJSON);
    }

    @Test
    void saveSingleEntityInDatabaseFromFile() throws IOException {
        userRepository.save(USER);
        JPAJacksonObjectMapper<User> JPAJacksonObjectMapper = new JPAJacksonObjectMapper<>();
        JPAJacksonObjectMapper.serializeToFile(USER, FILE_PATH);
        userRepository.deleteById(USER.getId());

        User userFromJSON = JPAJacksonObjectMapper.deserializeFromFile(new File(FILE_PATH), new TypeReference<User>() {
        });
        userRepository.save(userFromJSON);

        assertThat(userRepository.findAll().size()).isEqualTo(1);
        assertThat(userRepository.findByEmail(USER.getEmail())
                .orElse(Mockito.mock(User.class)))
                .isEqualTo(userFromJSON);
    }

    @Test
    void listSerializationDeserialization() throws IOException {
        JPAJacksonObjectMapper<User> JPAJacksonObjectMapper = new JPAJacksonObjectMapper<>();
        populatePassports();
        JPAJacksonObjectMapper.serializeToFile(USER, FILE_PATH);
        User userFromJSON = JPAJacksonObjectMapper.deserializeFromFile(new File(FILE_PATH), new TypeReference<User>() {
        });
        assertThat(userRepository.findById(USER.getId()).orElse(Mockito.mock(User.class))).isEqualTo(userFromJSON);
    }

    @Test
    void saveListInDatabaseFromFile() {
        populatePassports();

        PassportRepository passportRepository = new JpaPassportRepositoryImpl();
        JPAJacksonObjectMapper<List<Passport>> JPAJacksonObjectMapper = new JPAJacksonObjectMapper<>();

        EntityManagerUtil.executeInTransaction(entityManager -> {
            List<Passport> passports = entityManager
                    .createNamedQuery(Passport.FIND_ALL, Passport.class)
                    .getResultList();
            List<Passport> passportsFromFile = null;
            try {
                passports.forEach(passport -> passportRepository.deleteById(passport.getId()));
                JPAJacksonObjectMapper.serializeToFile(passports, FILE_PATH);
                passportsFromFile = JPAJacksonObjectMapper.deserializeFromFile(new File(FILE_PATH), new TypeReference<List<Passport>>() {
                });
                passportsFromFile.forEach(passportRepository::save);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertThat(passportRepository.findAll().size()).isEqualTo(3);
            return assertThat(passportRepository.findAll())
                    .containsExactlyInAnyOrder(
                            Objects.requireNonNull(passportsFromFile)
                                    .toArray(new Passport[0]));
        });
    }

    private void populatePassports() {
        Set<Passport> passportSet = new HashSet<>();
        passportSet.add(ModelUtilityClass.createPassport("1", USER));
        passportSet.add(ModelUtilityClass.createPassport("2", USER));
        passportSet.add(ModelUtilityClass.createPassport("3", USER));
        USER.setPassports(passportSet);
        userRepository.save(USER);
    }
}