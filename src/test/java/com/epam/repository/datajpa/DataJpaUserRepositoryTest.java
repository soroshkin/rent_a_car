package com.epam.repository.datajpa;

import com.epam.config.WebConfig;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;
import com.epam.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class DataJpaUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassportRepository passportRepository;

    @Test
    public void deleteUser(){
        List<User> users = userRepository.findAll();
        int listSize = users.size();
        userRepository.deleteById(1L);
        users = userRepository.findAll();

        User user = new User("dfas@esdf", LocalDate.now().minus(1, ChronoUnit.YEARS));
        Passport passport = new Passport("1", "some address", "Semen", "Fedorov", user);
        user.addPassport(passport);

        userRepository.save(user);

        users = userRepository.findAll();
        userRepository.deleteById(user.getId());
        users = userRepository.findAll();

        assertThat(listSize - 1).isEqualTo(users.size());
    }
}
