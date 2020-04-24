package com.epam.dao;

import com.epam.EntityManagerSetupExtension;
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
public class JpaAccountDAOTest {
    private JpaAccountDAO jpaAccountDAO = new JpaAccountDAO();
    private JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private User user;
    private Account account;

    @BeforeEach
    public void setUp() {
        user = createUser();
        account = jpaUserDAO.save(user).getAccount();
    }

    @Test
    public void get() {
        Account mockAccount = Mockito.mock(Account.class);
        assertThat(jpaAccountDAO.get(account.getId())).isPresent();
        assertThat(jpaAccountDAO.get(account.getId()).orElse(mockAccount)).isEqualTo(account);
    }

    @Test
    public void getAll() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        User anotherUser = createUser("another@email.com");
        Account anotherAccount = anotherUser.getAccount();
        accounts.add(anotherAccount);
        jpaAccountDAO.save(anotherAccount);
        assertThat(jpaAccountDAO.getAll()).isEqualTo(accounts);
    }

    @Test
    public void save() {
        user.setAccount(null);
        jpaAccountDAO.delete(1L);
        assertThat(jpaAccountDAO.save(account)).isEqualTo(account);
    }

    @Test
    public void delete() {
        assertThat(jpaAccountDAO.delete(account.getId())).isTrue();
    }

    @Test
    public void testTwoAccountToOneUserShouldFail() {
        Account anotherAccount = new Account(user);
        user.setAccount(anotherAccount);
        assertThatExceptionOfType(EntityExistsException.class).isThrownBy(() ->
                jpaUserDAO.save(user));
    }

}
