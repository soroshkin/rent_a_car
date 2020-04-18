package com.epam.dao;

import com.epam.model.Passport;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JpaPassportDAO implements DAO<Passport> {
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Passport> get(Long id) {
        return Optional.ofNullable(entityManager.find(Passport.class, id));
    }

    @Override
    public List<Passport> getAll() {
        return entityManager.createNamedQuery(Passport.GET_ALL, Passport.class).getResultList();
    }

    @Override
    public Passport save(Passport passport) {
        return executeInTransaction(em -> {
            if (passport.isNew()) {
                em.persist(passport);
                return passport;
            } else {
                return em.merge(passport);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInTransaction(em -> em.createNamedQuery(Passport.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0);
    }

    private  <R> R executeInTransaction(Function<EntityManager, R> function) {
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
