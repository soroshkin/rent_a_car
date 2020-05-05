package com.epam.dao.jpa;

import com.epam.dao.CarDAO;
import com.epam.model.Car;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaCarDAOImpl implements CarDAO {
    @Override
    public Optional<Car> get(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(Car.class, id)));
    }

    @Override
    public List<Car> getAll() {
        return executeOutsideTransaction(entityManager -> entityManager.createNamedQuery(Car.GET_ALL, Car.class).getResultList());
    }

    @Override
    public Car save(Car car) {
        return executeInTransaction(entityManager -> {
            if (car.isNew()) {
                entityManager.persist(car);
                return car;
            } else {
                return entityManager.merge(car);
            }
        });
    }

    @Override
    public boolean delete(Long id) {
        return executeInTransaction(entityManager -> {
            Car car = entityManager.find(Car.class, id);
            if (car != null) {
                entityManager.remove(car);
                return true;
            } else {
                return false;
            }
        });
    }
}
