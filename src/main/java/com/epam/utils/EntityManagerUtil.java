package com.epam.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

public abstract class EntityManagerUtil {
    private static EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
    private static final Logger LOGGER = LogManager.getLogger();

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
            LOGGER.error(e);
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
        if(entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            LOGGER.info("entityManagerFactory destroyed");
        }
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        try {
            Properties properties = PropertiesLoaderUtils
                    .loadProperties(new ClassPathResource("application.properties"));
            entityManagerFactory = Persistence
                    .createEntityManagerFactory(properties.getProperty("persistence.unit"));
        } catch (IOException e) {
            Objects.requireNonNull(LOGGER).error(
                    String.format("application properties file not found. %s", e.getMessage()));
        }

        return entityManagerFactory;
    }
}
