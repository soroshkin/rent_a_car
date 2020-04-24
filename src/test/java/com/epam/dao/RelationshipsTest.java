package com.epam.dao;

import com.epam.EntityManagerSetupExtension;
import com.epam.model.Bill;
import com.epam.model.Car;
import com.epam.model.Passport;
import com.epam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.epam.ModelUtilityClass.*;
import static com.epam.utils.EntityManagerUtil.executeOutsideTransaction;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(EntityManagerSetupExtension.class)
public class RelationshipsTest {
    private User user;
    private Passport passport;
    private JpaUserDAO jpaUserDAO = new JpaUserDAO();
    private JpaPassportDAO jpaPassportDAO = new JpaPassportDAO();
    private JpaCarDAO jpaCarDAO = new JpaCarDAO();
    private JpaBillDAO jpaBillDAO = new JpaBillDAO();
    private JpaAccountDAO jpaAccountDAO = new JpaAccountDAO();

    @BeforeEach
    void setUp() {
        user = createUser();
        passport = createPassport(user);
    }

    @Test
    public void tryAutoCreationOfAccounts() {
        jpaUserDAO.save(user);
        assertThat(jpaAccountDAO.getAll().size()).isEqualTo(1);
    }

    @Test
    public void oToMRelationshipUserPassport() {
        jpaUserDAO.save(user);
        Car car = createCar();
        jpaCarDAO.save(car);
        Bill bill = new Bill(LocalDate.now(), BigDecimal.valueOf(100), user, car);
        user.addPassport(passport);
        user.addBill(bill);
        jpaBillDAO.save(bill);
        assertThat(jpaPassportDAO.getAll().size()).isEqualTo(1);
    }

    @Test
    public void oToMRelationshipUserPassportOrphanRemove() {
        user.addPassport(passport);
        jpaUserDAO.save(user);
        Passport passport = executeOutsideTransaction(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Passport> criteriaQuery = criteriaBuilder.createQuery(Passport.class);
            Root<Passport> root = criteriaQuery.from(Passport.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get("user"), user));
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        });
        user.removePassport(passport);
        jpaUserDAO.save(user);
        assertThat(jpaPassportDAO.getAll().size()).isEqualTo(0);
    }

    @Test
    public void mToMRelationshipUserCars() {
        Car car = new Car("Tesla", "A343", LocalDate.of(1920, 1, 1), 1000);
        jpaCarDAO.save(car);
        user.addTripsByCar(car);
        user.addTripsByCar(car);
        jpaUserDAO.save(user);
        int tripsSize = executeOutsideTransaction(entityManager ->
                entityManager.createNativeQuery("SELECT * FROM trips")
                        .getResultList()
                        .size());
        assertThat(tripsSize).isEqualTo(1);
    }
}
