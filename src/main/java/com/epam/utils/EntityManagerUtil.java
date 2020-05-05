package com.epam.utils;

import com.epam.AppSettings;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Function;

public class EntityManagerUtil {
    private static EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

    private EntityManagerUtil() {
    }

    public static <R> R executeInTransaction(Function<EntityManager, R> function) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            R r = function.apply(entityManager);
            transaction.commit();
            entityManager.close();
            return r;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }

    public static <R> R executeOutsideTransaction(Function<EntityManager, R> function) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        R r = function.apply(entityManager);
        entityManager.close();
        return r;
    }

    public static void destroyEntityManagerFactory() {
        entityManagerFactory = null;
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory(AppSettings.PERSISTENCE_UNIT.getSettingValue());
        return entityManagerFactory;
    }
}
