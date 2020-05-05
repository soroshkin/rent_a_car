package com.epam.repository.jpa;

import com.epam.model.Car;
import com.epam.repository.CarRepository;

import java.util.List;
import java.util.Optional;

import static com.epam.utils.EntityManagerUtil.executeInTransaction;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;

public class JpaCarRepositoryImpl implements CarRepository {
    @Override
    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(executeOutsideTransaction(entityManager -> entityManager.find(Car.class, id)));
    }

    @Override
    public List<Car> findAll() {
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
    public void deleteById(Long id) {
        executeInTransaction(entityManager -> entityManager.createNamedQuery(Car.DELETE)
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public boolean existsById(Long id) {
        return executeOutsideTransaction(entityManager ->
                !entityManager.createQuery(Car.EXISTS)
                        .setParameter("id", id)
                        .getResultList().isEmpty());
    }
}
