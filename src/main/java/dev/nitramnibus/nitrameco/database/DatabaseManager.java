package dev.nitramnibus.nitrameco.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.nitramnibus.nitrameco.NitramEco;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseManager {

    private final NitramEco plugin;
    private final HikariDataSource hikari;


    public DatabaseManager(NitramEco plugin) {
        this.plugin = plugin;

        HikariConfig hikariConfig = new HikariConfig(getHikariProperties());
        hikari = new HikariDataSource(hikariConfig);

    }

    public void closeConnection() {
        hikari.close();
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
