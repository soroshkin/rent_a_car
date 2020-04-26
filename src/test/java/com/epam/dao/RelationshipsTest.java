package com.epam.dao;

import com.epam.EntityManagerSetupExtension;
import com.epam.dao.jpa.*;
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
    private UserDAO userDAO = new JpaUserDAOImpl();
    private PassportDAO passportDAO = new JpaPassportDAOImpl();
    private CarDAO carDAO = new JpaCarDAOImpl();
    private BillDAO billDAO = new JpaBillDAOImpl();
    private AccountDAO accountDAO = new JpaAccountDAOImpl();

    @BeforeEach
    void setUp() {
        user = createUser();
        passport = createPassport(user);
    }

    @Test
    public void tryAutoCreationOfAccounts() {
        userDAO.save(user);
        assertThat(accountDAO.getAll().size()).isEqualTo(1);
    }

    @Test
    public void oToMRelationshipUserPassport() {
        userDAO.save(user);
        Car car = createCar();
        carDAO.save(car);
        Bill bill = new Bill(LocalDate.now(), BigDecimal.valueOf(100), user, car);
        user.addPassport(passport);
        user.addBill(bill);
        billDAO.save(bill);
        assertThat(passportDAO.getAll().size()).isEqualTo(1);
    }

    @Test
    public void oToMRelationshipUserPassportOrphanRemove() {
        user.addPassport(passport);
        userDAO.save(user);
        Passport passport = executeOutsideTransaction(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Passport> criteriaQuery = criteriaBuilder.createQuery(Passport.class);
            Root<Passport> root = criteriaQuery.from(Passport.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get("user"), user));
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        });
        user.removePassport(passport);
        userDAO.save(user);
        assertThat(passportDAO.getAll().size()).isEqualTo(0);
    }

    @Test
    public void mToMRelationshipUserCars() {
        Car car = new Car("Tesla", "A343", LocalDate.of(1920, 1, 1), 1000);
        carDAO.save(car);
        user.addTripsByCar(car);
        user.addTripsByCar(car);
        userDAO.save(user);
        int tripsSize = executeOutsideTransaction(entityManager ->
                entityManager.createNativeQuery("SELECT * FROM trips")
                        .getResultList()
                        .size());
        assertThat(tripsSize).isEqualTo(1);
    }
}
