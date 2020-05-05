package com.epam;

import com.epam.utils.EntityManagerUtil;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class EntityManagerSetupExtension implements BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        EntityManagerUtil.createEntityManagerFactory();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        EntityManagerUtil.destroyEntityManagerFactory();
    }

}
