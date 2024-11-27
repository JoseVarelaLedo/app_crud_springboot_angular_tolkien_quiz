package app.data;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase para crear la base de datos si no existe. Se anota con @Order para que el framework Spring la trate de acuerdo a su naturaleza.
 */
@Component
@Order(1)
public class DataInitializerBD {

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private static final String DATABASE_NAME = "tolkien_quiz";
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializerBD.class);

//    @PostConstruct
    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(BASE_URL, dbUser, dbPassword)) {
            String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createDbQuery);
                LOGGER.info("Base de datos '{}' creada o ya existe.", DATABASE_NAME);
            }
        } catch (SQLException e) {
            LOGGER.error("Error al crear la base de datos: ", e);
        }
    }
}
