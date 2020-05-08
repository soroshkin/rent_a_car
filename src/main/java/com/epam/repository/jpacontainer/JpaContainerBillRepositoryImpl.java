package com.epam.repository.jpacontainer;

import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.User;
import com.epam.repository.BillRepository;
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
public class JpaContainerBillRepositoryImpl implements BillRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Bill> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Bill.class, id));
    }

    @Override
    public List<Bill> findAll() {
        return entityManager.createNamedQuery(Bill.GET_ALL, Bill.class)
                .getResultList();
    }

    @Override
    public List<Bill> findAllByUser(User user) {
        return entityManager.createNamedQuery(Bill.GET_BY_USER, Bill.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Bill> findByCar(Car car) {
        return entityManager.createNamedQuery(Bill.GET_BY_CAR, Bill.class)
                .setParameter("car", car)
                .getResultList();
    }

    @Override
    @Transactional
    public Bill save(Bill bill) {
        if (bill.isNew()) {
            entityManager.persist(bill);
            return bill;
        } else {
            return entityManager.merge(bill);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        entityManager.createNamedQuery(Bill.DELETE)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(Long id) {
        return !entityManager.createQuery(Bill.EXISTS)
                .setParameter("id", id)
                .getResultList().isEmpty();
    }
}
