package dev.nitramnibus.nitrameco.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.nitramnibus.nitrameco.NitramEco;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {

    private final NitramEco plugin;
    private final Logger logger;


    private final HikariDataSource hikari;


    public DatabaseManager(NitramEco plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;

        HikariConfig hikariConfig = new HikariConfig(getHikariProperties());
        hikari = new HikariDataSource(hikariConfig);

        initializeTable();

    }

    public DataSource getDataSource() {
        return hikari;
    }

    public void closeConnection() {
        hikari.close();
    }

    private void initializeTable() {

        try (Connection connection = hikari.getConnection();
             PreparedStatement statement = connection.prepareStatement("""
                     CREATE TABLE IF NOT EXISTS money
                     (
                         uuid BINARY(16) NOT NULL,
                         money BIGINT    NOT NULL,
                     
                         PRIMARY KEY (uuid)
                     );
                     """)
        ) {
            statement.execute();
            logger.log(Level.INFO, "Database Table initialized successfully.");

        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize database's table", e);
        }

    }

    private Properties getHikariProperties() {
        Properties props = new Properties();

        try (InputStream input = plugin.getClass()
                .getClassLoader()
                .getResourceAsStream("hikari.properties")) {

            if (input == null) {
                throw new IllegalStateException("hikari.properties not found in plugin jar");
            }
            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load hikari.properties", e);
        }

        return props;
    }


}
