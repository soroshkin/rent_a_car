package com.epam.repository.jpacontainer;

import com.epam.model.Car;
import com.epam.repository.CarRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.epam.config.Profiles.JPA_PROFILE;

@Profile(JPA_PROFILE)
@Repository
@Transactional(readOnly = true)
public class JpaContainerCarRepositoryImpl implements CarRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Car.class, id));
    }

    @Override
    public List<Car> findAll() {
        return entityManager.createNamedQuery(Car.GET_ALL, Car.class).getResultList();
    }

    @Override
    @Transactional
    public Car save(Car car) {
        if (car.isNew()) {
            entityManager.persist(car);
            return car;
        } else {
            return entityManager.merge(car);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Car car = entityManager.find(Car.class, id);
        entityManager.remove(car);
    }

    @Override
    public boolean existsById(Long id) {
        return !entityManager.createNamedQuery(Car.EXISTS)
                .setParameter("id", id)
                .getResultList().isEmpty();
    }
}
