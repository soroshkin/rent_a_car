package com.epam.repository.jpa;

import com.epam.model.Bill;
import com.epam.model.Passport;
import com.epam.model.User;
import com.epam.repository.PassportRepository;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaPassportRepositoryImpl implements PassportRepository {
    @Override
    public Optional<Passport> findById(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(
                entityManager -> entityManager.find(Passport.class, id)));
    }

    @Override
    public List<Passport> findAll() {
        return executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(Passport.FIND_ALL, Passport.class)
                .getResultList());
    }

    @Override
    public List<Passport> findAllByUser(User user) {
        return executeOutsideTransaction(entityManager -> entityManager
                .createNamedQuery(Passport.FIND_BY_USER, Passport.class)
                .setParameter("userId", user.getId())
                .getResultList());
    }

    @Override
    public List<Passport> findByUser(User user) {
        return executeOutsideTransaction(entityManager ->
                entityManager.createNamedQuery(Bill.GET_BY_USER, Passport.class)
                        .setParameter("userId", user.getId())
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
    public void deleteById(Long id) {
        executeInTransaction(entityManager -> entityManager
                .createNamedQuery(Passport.DELETE)
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public boolean existsById(Long id) {
        return executeOutsideTransaction(entityManager ->
                !entityManager.createQuery(Passport.EXISTS)
                        .setParameter("id", id)
                        .getResultList().isEmpty());
    }
}
