package com.epam;

import org.junit.jupiter.api.extension.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseSetupExtension implements BeforeEachCallback, AfterEachCallback {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test");
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void beforeEach(ExtensionContext context){
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void afterEach(ExtensionContext context){
        entityManager.close();
    }
}
