package com.epam.repository.datajpa;

import com.epam.config.WebConfig;
import com.epam.model.Account;
import com.epam.model.User;
import com.epam.repository.AccountRepository;
import com.epam.repository.UserRepository;
import com.epam.util.ModelUtilityClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@SqlGroup(@Sql(scripts = "classpath:db/clearDB.sql"))
public class DataJpaAccountRepositoryTest {
    private Account account;
    private User user;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(ModelUtilityClass.createUser());
        account = user.getAccount();
    }

    @Test
    public void findByIdShouldReturnAccount() {
        assertThat(accountRepository.findById(account.getId())
                .orElse(Mockito.mock(Account.class)))
                .isEqualTo(account);
    }

    @Test
    public void findByIdShouldReturnOptionalEmpty() {
        assertThat(accountRepository.findById(10000L)).isEmpty();
    }

    @Test
    public void findByUserShouldReturnAccount() {
        assertThat(accountRepository.findByUser(user))
                .isEqualTo(Optional.of(account));
    }

    @Test
    public void findByUserShouldReturnOptionalEmpty() {
        accountRepository.deleteById(account.getId());

        assertThat(accountRepository.existsById(account.getId())).isFalse();
        assertThat(accountRepository.findByUser(user)).isEmpty();
    }

    @Test
    public void saveShouldReturnAccount(){
        accountRepository.deleteById(account.getId());
        account = accountRepository.save(account);

        assertThat(accountRepository.existsById(account.getId())).isTrue();
        assertThat(accountRepository.save(account)).isEqualTo(account);
    }

    @Test
    public void deleteByIdShouldDelete(){
        assertThat(accountRepository.existsById(account.getId())).isTrue();
        accountRepository.deleteById(account.getId());
        assertThat(accountRepository.existsById(account.getId())).isFalse();
    }
}
