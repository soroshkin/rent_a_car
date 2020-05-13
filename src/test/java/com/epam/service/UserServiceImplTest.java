package com.epam.service;

import com.epam.model.User;
import com.epam.repository.UserRepository;
import com.epam.util.ModelUtilityClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {
    private User user;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        user = ModelUtilityClass.createUser();
    }

    @Test
    public void findAllShouldReturnSortedList() {
        User firstUser = new User("zz@email.com", LocalDate.now());
        User secondUser = new User("aa@email.com", LocalDate.now());
        List<User> users = Arrays.asList(firstUser, secondUser);

        when(userRepository.findAll()).thenReturn(users);
        List<User> usersSortedExplicitly = new ArrayList<>(users);

        usersSortedExplicitly.sort(Comparator.comparing(User::getEmail));
        assertThat(userService.findAll()).isEqualTo(usersSortedExplicitly);
    }

    @Test
    public void saveUserShouldCreateNewUser() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.save(user)).isEqualTo(user);
    }

    @Test
    public void saveUserShouldThrowExceptionIfUserNull() {
        when(userRepository.existsById(null)).thenThrow(new IllegalArgumentException());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userService.save(null));
    }

    @Test
    public void existsByIdShouldReturnTrueIfExists() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThat(userService.existsById(1L)).isTrue();
    }

    @Test
    public void existsByIdShouldReturnFalseIfNotExists() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThat(userService.existsById(1L)).isFalse();
    }
}