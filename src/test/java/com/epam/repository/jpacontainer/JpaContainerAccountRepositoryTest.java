package com.epam.repository.jpacontainer;

import com.epam.config.WebConfig;
import com.epam.model.Account;
import com.epam.model.User;
import com.epam.repository.AccountRepository;
import com.epam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static com.epam.config.Profiles.JPA_PROFILE;
import static com.epam.util.ModelUtilityClass.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ActiveProfiles(JPA_PROFILE)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
@SqlGroup(@Sql(scripts = "classpath:db/clearDB.sql"))
public class JpaContainerAccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Account account;

    @BeforeEach
    public void setUp() {
        user = createUser();
        account = userRepository.save(user).getAccount();
    }

    @Test
    public void get() {
        Account mockAccount = Mockito.mock(Account.class);
        assertThat(accountRepository.findById(account.getId())).isPresent();
        assertThat(accountRepository.findById(account.getId()).orElse(mockAccount)).isEqualTo(account);
    }

    @Test
    public void getAll() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        User anotherUser = createUser("another@email.com");
        Account anotherAccount = anotherUser.getAccount();
        accounts.add(anotherAccount);
        accountRepository.save(anotherAccount);
        assertThat(accountRepository.findAll()).isEqualTo(accounts);
    }

    @Test
    public void save() {
        user.setAccount(null);
        accountRepository.deleteById(1L);
        assertThat(accountRepository.save(account)).isEqualTo(account);
    }

    @Test
    public void delete() {
        accountRepository.deleteById(account.getId());
        assertThat(accountRepository.findById(account.getId())).isEmpty();
    }

    @Test
    public void testTwoAccountToOneUserShouldFail() {
        Account anotherAccount = new Account(user);
        user.setAccount(anotherAccount);
        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(() ->
                userRepository.save(user));
    }

}
