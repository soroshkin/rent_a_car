package com.epam.service;

import com.epam.config.WebConfig;
import com.epam.model.User;
import com.epam.repository.UserRepository;
import com.epam.util.ModelUtilityClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {
    private User user;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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