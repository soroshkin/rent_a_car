package com.epam.repository.jpa;

import com.epam.EntityManagerSetupExtension;
import com.epam.repository.AccountRepository;
import com.epam.repository.UserRepository;
import com.epam.model.Account;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.ModelUtilityClass.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(EntityManagerSetupExtension.class)
public class JpaAccountRepositoryTest {
    private AccountRepository accountRepository = new JpaAccountRepositoryImpl();
    private UserRepository userRepository = new JpaUserRepositoryImpl();
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
        assertThatExceptionOfType(EntityExistsException.class).isThrownBy(() ->
                userRepository.save(user));
    }

}
