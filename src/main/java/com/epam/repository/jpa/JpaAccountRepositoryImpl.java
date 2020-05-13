package com.epam.repository.jpa;

import com.epam.model.Account;
import com.epam.model.User;
import com.epam.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaAccountRepositoryImpl implements AccountRepository {
    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(Account.class, id)));
    }

    @Override
    public List<Account> findAll() {
        return executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(Account.GET_ALL, Account.class)
                .getResultList());
    }

    @Override
    public Optional<Account> findByUser(User user) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager ->
                entityManager.createNamedQuery(Account.GET_BY_USER, Account.class)
                        .setParameter("user", user)
                        .getSingleResult()));
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
    public void deleteById(Long id) {
        executeInTransaction(entityManager -> entityManager.createNamedQuery(Account.DELETE)
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public boolean existsById(Long id) {
        return executeOutsideTransaction(entityManager ->
                !entityManager.createQuery(Account.EXISTS)
                        .setParameter("id", id)
                        .getResultList().isEmpty());
    }
}
