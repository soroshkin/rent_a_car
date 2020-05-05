package com.epam.repository.jpa;

import com.epam.model.User;
import com.epam.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaUserRepositoryImpl implements UserRepository {
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(User.class, id)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(User.GET_BY_EMAIL, User.class)
                .setParameter("email", email)
                .getResultList().get(0)));
    }

    @Override
    public List<User> findAll() {
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
    public void deleteById(Long id) {
        executeInTransaction(entityManager -> entityManager
                .createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public boolean existsById(Long id) {
        return executeOutsideTransaction(entityManager ->
                !entityManager.createQuery(User.EXISTS)
                        .setParameter("id", id)
                        .getResultList().isEmpty());
    }
}

