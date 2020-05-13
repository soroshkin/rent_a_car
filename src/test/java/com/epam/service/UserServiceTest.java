package com.epam.service;

import com.epam.model.User;
import com.epam.repository.UserRepository;
import com.epam.util.ModelUtilityClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {
    private User user;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = ModelUtilityClass.createUser();
    }

    @Test
    @Transactional
    public void findAllShouldReturnSortedList() {
        List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();

        users.sort(Comparator.comparing(User::getEmail));
        assertThat(userService.findAll()).isEqualTo(users);
    }

    @Test
    public void saveUserShouldCreateNewUser() {
        assertThat(userService.save(user)).isEqualTo(user);
    }

    @Test
    public void saveBillShouldThrowExceptionIfUserNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.save(null));
    }


    @Test
    public void saveUserShouldThrowExceptionIfUserNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.save(null));
    }

    @Test
    public void existsByIdShouldReturnTrueIfExists() {
        user = userService.save(user);
        assertThat(userService.existsById(user.getId())).isTrue();
    }

    @Test
    public void existsByIdShouldReturnFalseIfNotExists() {
        assertThat(userService.existsById(10000L)).isFalse();
    }

    @Test
    public void deleteShouldDeleteUserCascade() {
        Long userId = 1L;
        userService.deleteById(userId);
        assertThat(userService.findById(userId)).isEmpty();
    }
}