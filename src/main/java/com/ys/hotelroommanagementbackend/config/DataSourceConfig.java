package com.ys.hotelroommanagementbackend.config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.core.env.Environment;

/**
 * This Class is for running an SQL Query to initialize the Postgres Database
 * The SQL query is "CREATE EXTENSION IF NOT EXISTS CITEXT"
 * It creates the type CITEXT, which is Case Insensitive TEXT,
 * this way when setting a CITEXT field to unique, changes in
 * letter cases are took into account.
 * <br>
 * <br>
 * Example:
 * In the Role entity the name field is a string of type CITEXT in the database
 * and has a UNIQUE constraint, this way we can't have two roles: with names:
 * "Admin" and "admin"
 * The is done for the username and email fields for the User Entity
 *
 * @author Yassine Slaoui
 */
@Configuration
public class DataSourceConfig {

    private final Environment environment;

    final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));

        try (Connection connection = dataSource.getConnection()) {
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            if (databaseProductName.equalsIgnoreCase("PostgreSQL")) {
                logger.info("Database is PostgreSQL, will create CITEXT extension if it doesn't exist.");
                Statement statement = connection.createStatement();

                logger.info("Executing SQL query...");
                statement.execute("CREATE EXTENSION IF NOT EXISTS CITEXT");

                logger.info("Created CITEXT extension if it didn't exist.");
                System.setProperty("database.type", "postgres");
            } else if (databaseProductName.equalsIgnoreCase("MySQL")) {
                logger.info("Database is MySQL, string values are case insensitive by default in MySQL databases, nothing to do.");
                System.setProperty("database.type", "mysql");
            } else {
                logger.warn("Database is {}, not known, it better have case insensitive unique constraints on string values!", databaseProductName);
                System.setProperty("database.type", "mysql");
            }
        } catch (Exception e) {
            logger.error("There was an error with the database.", e);
        }

        return dataSource;
    }

    @Bean
    public CommandLineRunner initDatabase(DataSource dataSource) {
        return _ -> {
            String sql = "ALTER TABLE users ADD CONSTRAINT username_or_email_check CHECK (username IS NOT NULL OR email IS NOT NULL)";
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute(sql);
        };
    }
}