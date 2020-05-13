package com.epam.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.Properties;

public class AppInitializer implements WebApplicationInitializer {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));
        Resource resource = new ClassPathResource("app.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            context.getEnvironment().setActiveProfiles(props.getProperty("spring.profiles.active"));
        } catch (IOException e) {
            LOGGER.error(String.format("application properties file not found. %s", e.getMessage()));
        }

        ServletRegistration.Dynamic dispatcher = servletContext
                .addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
