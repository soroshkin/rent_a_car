package com.epam.repository.jpa;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaBillRepositoryImpl implements BillRepository {
    @Override
    public Optional<Bill> findById(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(Bill.class, id)));
    }

    @Override
    public List<Bill> findAll() {
        return executeOutsideTransaction(entityManager -> entityManager.createNamedQuery(Bill.GET_ALL, Bill.class)
                .getResultList());
    }

    @Override
    public List<Bill> findAllByUser(User user) {
        return executeOutsideTransaction(entityManager ->
                entityManager.createNamedQuery(Bill.GET_BY_USER, Bill.class)
                        .setParameter("user", user)
                        .getResultList());
    }

    @Override
    public List<Bill> findByCar(Car car) {
        return executeOutsideTransaction(entityManager ->
                entityManager.createNamedQuery(Bill.GET_BY_CAR, Bill.class)
                        .setParameter("car", car)
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
    public void deleteById(Long id) {
        executeInTransaction(entityManager -> entityManager.createNamedQuery(Bill.DELETE)
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public boolean existsById(Long id) {
        return executeOutsideTransaction(entityManager ->
                !entityManager.createQuery(Bill.EXISTS)
                        .setParameter("id", id)
                        .getResultList().isEmpty());
    }
}
