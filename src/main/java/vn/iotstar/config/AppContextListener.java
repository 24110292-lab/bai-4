package vn.iotstar.config;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import vn.iotstar.service.ProfileImageStore;
import vn.iotstar.service.UserService;

public class AppContextListener implements ServletContextListener {
    private EntityManagerFactory entityManagerFactory;
    private ValidatorFactory validatorFactory;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        String username = environmentOrDefault("DB_USERNAME", "sa");
        String password = environmentOrDefault("DB_PASSWORD", "123");
        String jdbcUrl = environmentOrDefault(
                "DB_URL",
                "jdbc:sqlserver://localhost:1433;databaseName=Bai4DB;encrypt=true;trustServerCertificate=true");
        String databaseName = environmentOrDefault("DB_NAME", "Bai4DB");

        try {
            SqlServerDatabaseInitializer.createIfMissing(jdbcUrl, username, password, databaseName);
        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Cannot create or connect to database '" + databaseName
                            + "'. Check SQL Server availability and that the configured login has CREATE DATABASE permission.",
                    exception);
        }

        Map<String, Object> properties = new HashMap<>();
        putEnvironmentOverride(properties, "DB_USERNAME", "jakarta.persistence.jdbc.user");
        putEnvironmentOverride(properties, "DB_PASSWORD", "jakarta.persistence.jdbc.password");
        properties.put("jakarta.persistence.jdbc.url",
                SqlServerDatabaseInitializer.toDatabaseUrl(jdbcUrl, databaseName));

        entityManagerFactory = Persistence.createEntityManagerFactory("profilePU", properties);
        validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        String uploadDirectory = System.getenv("PROFILE_UPLOAD_DIR");
        Path directory = uploadDirectory == null || uploadDirectory.isBlank()
                ? Path.of(System.getProperty("user.home"), "Bai_4", "profile-images")
                : Path.of(uploadDirectory);
        ProfileImageStore imageStore;
        try {
            imageStore = new ProfileImageStore(directory);
        } catch (IOException exception) {
            closeResources();
            throw new IllegalStateException("Cannot initialize the profile image directory.", exception);
        }

        event.getServletContext().setAttribute("userService", new UserService(entityManagerFactory));
        event.getServletContext().setAttribute("validator", validator);
        event.getServletContext().setAttribute("profileImageStore", imageStore);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        closeResources();
    }

    private String environmentOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private void putEnvironmentOverride(Map<String, Object> properties, String environmentName, String propertyName) {
        String value = System.getenv(environmentName);
        if (value != null && !value.isBlank()) {
            properties.put(propertyName, value);
        }
    }

    private void closeResources() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
