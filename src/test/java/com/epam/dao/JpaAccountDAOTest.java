package com.epam.dao;

import com.epam.DatabaseSetupExtension;
import com.epam.model.Account;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.ModelUtilityClass.createAccount;
import static com.epam.ModelUtilityClass.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JpaAccountDAOTest {
    private JpaAccountDAO jpaAccountDAO = new JpaAccountDAO();
    private JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private User user;
    private Account account;

    @RegisterExtension
    DatabaseSetupExtension databaseSetupExtension = new DatabaseSetupExtension();

    @BeforeEach
    public void setUp() {
        jpaAccountDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        jpaUserDAO.setEntityManager(databaseSetupExtension.getEntityManager());
        account = jpaUserDAO.save(createUser()).getAccount();
        user = createUser();
    }

    @Test
    public void get() {

        jpaAccountDAO.save(account);
        Account mockAccount = Mockito.mock(Account.class);
        assertThat(jpaAccountDAO.get(account.getId())).isPresent();
        assertThat(jpaAccountDAO.get(account.getId()).orElse(mockAccount)).isEqualTo(account);
    }

    @Test
    public void getAll() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(jpaUserDAO.save(createUser()).getAccount());
        assertThat(jpaAccountDAO.getAll()).isEqualTo(accounts);
    }

    @Test
    public void save() {
        assertThat(jpaAccountDAO.save(account)).isSameAs(account);
    }

    @Test
    public void delete() {
        jpaAccountDAO.save(account);
        assertThat(jpaAccountDAO.delete(account.getId())).isTrue();
    }

    @Test
    public void testTwoAccountToOneUserShouldFail() {
        jpaAccountDAO.save(user.getAccount());
        assertThatExceptionOfType(EntityExistsException.class).isThrownBy(() -> jpaAccountDAO.save(new Account(user)));
    }

}
