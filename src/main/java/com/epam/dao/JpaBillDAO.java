package com.epam.dao;

import com.epam.model.Bill;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaBillDAO implements DAO<Bill> {
    @Override
    public Optional<Bill> get(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(Bill.class, id)));
    }

    @Override
    public List<Bill> getAll() {
        return executeOutsideTransaction(entityManager -> entityManager.createNamedQuery(Bill.GET_ALL, Bill.class)
                .getResultList());
    }

    @Override
    public Bill save(Bill bill) {
        return executeInTransaction(entityManager -> {
            if (bill.isNew()) {
                entityManager.persist(bill);
                return bill;
            } else {
                return entityManager.merge(bill);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInTransaction(entityManager -> entityManager.createNamedQuery(Bill.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0);
    }
}
