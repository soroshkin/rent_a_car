package com.epam.dao;

import com.epam.model.User;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaUserDAO implements DAO<User> {

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(User.class, id)));
    }

    @Override
    public List<User> getAll() {
        return executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(User.GET_ALL, User.class)
                .getResultList());
    }

    @Override
    public User save(User user) {
        return executeInTransaction(entityManager -> {
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
        return executeInTransaction(entityManager -> entityManager
                .createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0);
    }
}

