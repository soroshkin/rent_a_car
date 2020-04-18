package com.epam.dao;

import com.epam.DatabaseSetupExtension;
import com.epam.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static com.epam.ModelUtilityClass.*;

public class RelationshipsTest {
    @RegisterExtension
    DatabaseSetupExtension databaseSetupExtension = new DatabaseSetupExtension();

    private EntityManager entityManager;
    private User user = createUser();
    private Passport passport = createPassport();

    @BeforeEach
    public void setUp() {
        entityManager = databaseSetupExtension.getEntityManager();
        entityManager.getTransaction().begin();
    }

    @AfterEach
    public void tearDown(){
        entityManager.getTransaction().commit();
    }

    @Test
    public void try_auto_creation_of_accounts_table() {
        entityManager.persist(user);
        assertThat(entityManager.createNamedQuery(Account.GET_ALL, Account.class).getResultList().size()).isEqualTo(1);
    }

//    @Test
//    public void testMtoMRelationship() {
//
//    }

    @Test
    public void test_OtoM_relationship_user_passport() {
        Bill bill = new Bill(LocalDate.now(), BigDecimal.valueOf(100), user);
        user.addPassport(passport);
        user.addBill(bill);
        entityManager.persist(bill);
        entityManager.persist(user);
        assertThat(entityManager.createNamedQuery(Passport.GET_ALL, Passport.class).getResultList().size()).isEqualTo(1);
    }

    @Test
    public void test_OtoM_relationship_user_passport_orphan_remove() {
        user.addPassport(passport);
        entityManager.persist(user);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Passport> criteriaQuery = criteriaBuilder.createQuery(Passport.class);
        Root<Passport> root = criteriaQuery.from(Passport.class);
        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("user"), user));
        Passport passport = entityManager.createQuery(criteriaQuery).getSingleResult();
        user.removePassport(passport);
        assertThat(entityManager.createNamedQuery(Passport.GET_ALL).getResultList().size()).isEqualTo(0);
    }

    @Test
    public void test_MtM_Relationship_user_cars() {
        Car car = new Car("Tesla", LocalDate.of(1920, 1, 1), 1000);
        user.addTripsByCar(car);
        user.addTripsByCar(car);
        entityManager.persist(user);
    }
}
