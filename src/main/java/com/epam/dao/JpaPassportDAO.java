package com.epam.dao;

import com.epam.model.Passport;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaPassportDAO implements DAO<Passport> {
    @Override
    public Optional<Passport> get(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(Passport.class, id)));
    }

    @Override
    public List<Passport> getAll() {
        return executeOutsideTransaction(entityManager -> entityManager.createNamedQuery(Passport.GET_ALL, Passport.class)
                .getResultList());
    }

    @Override
    public Passport save(Passport passport) {
        return executeInTransaction(entityManager -> {
            if (passport.isNew()) {
                entityManager.persist(passport);
                return passport;
            } else {
                return entityManager.merge(passport);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInTransaction(entityManager -> entityManager.createNamedQuery(Passport.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0);
    }
}
