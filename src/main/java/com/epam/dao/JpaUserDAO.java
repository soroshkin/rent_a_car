package com.epam.dao;

import com.epam.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JpaUserDAO implements DAO<User> {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> getAll() {
        return entityManager.createNamedQuery(User.GET_ALL, User.class).getResultList();
    }

    @Override
    public User save(User user) {
        return executeInsideTransaction(em -> {
            if (user.isNew()) {
                entityManager.persist(user);
                return user;
            } else {
                return entityManager.merge(user);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInsideTransaction(em -> em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0);
    }

    private <R> R executeInsideTransaction(Function<EntityManager, R> function) {
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

