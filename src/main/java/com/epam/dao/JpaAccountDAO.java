package com.epam.dao;

import com.epam.model.Account;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaAccountDAO implements DAO<Account> {
    @Override
    public Optional<Account> get(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(Account.class, id)));
    }

    @Override
    public List<Account> getAll() {
        return executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(Account.GET_ALL, Account.class)
                .getResultList());
    }

    @Override
    public Account save(Account account) {
        return executeInTransaction(entityManager -> {
            if (account.isNew()) {
                entityManager.persist(account);
                return account;
            } else {
                return entityManager.merge(account);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInTransaction(entityManager -> entityManager.createNamedQuery(Account.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0);
    }

}
