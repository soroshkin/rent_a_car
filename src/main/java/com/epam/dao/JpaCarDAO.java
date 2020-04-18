package com.epam.dao;

import com.epam.model.Car;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JpaCarDAO implements DAO<Car> {
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Car> get(Long id) {
        return Optional.ofNullable(entityManager.find(Car.class, id));
    }

    @Override
    public List<Car> getAll() {
        return entityManager.createNamedQuery(Car.GET_ALL, Car.class).getResultList();
    }

    @Override
    public Car save(Car car) {
        return executeInTransaction(em -> {
            if (car.isNew()) {
                em.persist(car);
                return car;
            } else {
                return em.merge(car);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInTransaction(em -> em.createNamedQuery(Car.DELETE)
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
