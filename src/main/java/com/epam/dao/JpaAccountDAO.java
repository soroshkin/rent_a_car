package com.epam.dao;

import com.epam.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JpaAccountDAO implements DAO<Account> {
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Account> get(Long id) {
        return Optional.ofNullable(entityManager.find(Account.class, id));
    }

    @Override
    public List<Account> getAll() {
        return entityManager.createNamedQuery(Account.GET_ALL, Account.class).getResultList();
    }

    @Override
    public Account save(Account account) {
        return executeInTransaction(em -> {
            if (account.isNew()) {
                em.persist(account);
                return account;
            } else {
                return em.merge(account);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInTransaction(em -> em.createNamedQuery(Account.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0);
    }

    private <R> R executeInTransaction(Function<EntityManager, R> function) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            return function.apply(entityManager);
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isActive()) {
                transaction.commit();
            }
        }
    }
}
