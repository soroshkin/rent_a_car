package com.epam.dao;

import com.epam.DatabaseSetupExtension;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.epam.ModelUtilityClass.*;

public class JpaUserDAOTest {
    JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private User user;

    @RegisterExtension
    DatabaseSetupExtension databaseSetupExtension = new DatabaseSetupExtension();

    @BeforeEach
    public void setEntityManager() {
        jpaUserDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        user = createUser();
    }

    @Test
    public void save() {
        assertThat(jpaUserDAO.save(user)).isSameAs(user);
    }

    @Test
    public void delete() {
        jpaUserDAO.save(user);
        assertThat(jpaUserDAO.delete(user.getId())).isTrue();
    }

    @Test
    public void get() {
        User mockUser = Mockito.mock(User.class);
        jpaUserDAO.save(user);
        assertThat(jpaUserDAO.get(user.getId()).isPresent()).isTrue();
        assertThat(jpaUserDAO.get(user.getId()).orElse(mockUser)).isEqualTo(user);
    }

    @Test
    public void getAll() {
        String email = "email@domain.com";
        LocalDate dateOfBirth = LocalDate.now().minus(1, ChronoUnit.YEARS);
        User user = new User(email, dateOfBirth);
        User anotherUser = new User(email, dateOfBirth);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(anotherUser);
        jpaUserDAO.save(user);
        jpaUserDAO.save(anotherUser);
        assertThat(jpaUserDAO.getAll()).isEqualTo(users);
    }
}
