package com.epam.dao.jpa;

import com.epam.EntityManagerSetupExtension;
import com.epam.dao.AccountDAO;
import com.epam.dao.UserDAO;
import com.epam.dao.jpa.JpaAccountDAOImpl;
import com.epam.dao.jpa.JpaUserDAOImpl;
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
    private AccountDAO accountDAO = new JpaAccountDAOImpl();
    private UserDAO userDAO = new JpaUserDAOImpl();
    private User user;
    private Account account;

    @BeforeEach
    public void setUp() {
        user = createUser();
        account = userDAO.save(user).getAccount();
    }

    @Test
    public void get() {
        Account mockAccount = Mockito.mock(Account.class);
        assertThat(accountDAO.get(account.getId())).isPresent();
        assertThat(accountDAO.get(account.getId()).orElse(mockAccount)).isEqualTo(account);
    }

    @Test
    public void getAll() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        User anotherUser = createUser("another@email.com");
        Account anotherAccount = anotherUser.getAccount();
        accounts.add(anotherAccount);
        accountDAO.save(anotherAccount);
        assertThat(accountDAO.getAll()).isEqualTo(accounts);
    }

    @Test
    public void save() {
        user.setAccount(null);
        accountDAO.delete(1L);
        assertThat(accountDAO.save(account)).isEqualTo(account);
    }

    @Test
    public void delete() {
        assertThat(accountDAO.delete(account.getId())).isTrue();
    }

    @Test
    public void testTwoAccountToOneUserShouldFail() {
        Account anotherAccount = new Account(user);
        user.setAccount(anotherAccount);
        assertThatExceptionOfType(EntityExistsException.class).isThrownBy(() ->
                userDAO.save(user));
    }

}
