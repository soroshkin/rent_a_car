package com.epam.dao;

import com.epam.model.Bill;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JpaBillDAO implements DAO<Bill> {
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Bill> get(Long id) {
        return Optional.ofNullable(entityManager.find(Bill.class, id));
    }

    @Override
    public List<Bill> getAll() {
        return entityManager.createNamedQuery(Bill.GET_ALL, Bill.class).getResultList();
    }

    @Override
    public Bill save(Bill bill) {
        return executeInTransaction(em -> {
            if (bill.isNew()) {
                em.persist(bill);
                return bill;
            } else {
                return em.merge(bill);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInTransaction(em -> em.createNamedQuery(Bill.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0);
    }

    public <R> R executeInTransaction(Function<EntityManager, R> function) {
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
