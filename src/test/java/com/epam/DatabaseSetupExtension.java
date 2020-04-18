package com.epam;

import org.junit.jupiter.api.extension.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseSetupExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test");
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        entityManager.close();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

    }
}
