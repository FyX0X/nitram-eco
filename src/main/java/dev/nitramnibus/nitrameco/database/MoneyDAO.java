package dev.nitramnibus.nitrameco.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class MoneyDAO {

    private static final String UPSERT = "INSERT INTO money VALUES(?, ?) ON DUPLICATE KEY UPDATE money=?";
    private static final String SELECT = "SELECT money from money WHERE uuid=?";

    private final HikariDataSource hikari;


    public MoneyDAO(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    public void setPlayerMoney(UUID uuid, long money) {

        try (Connection connection = hikari.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPSERT)) {

            statement.setBytes(1, UuidConverter.toBytes(uuid));
            statement.setLong(2, money);
            statement.setLong(3, money);

        } catch (SQLException e) {
            throw new RuntimeException("Error when setting player money to database", e);
        }

    }

    public Optional<Long> getPlayerMoney(UUID uuid) {

        try (Connection connection = hikari.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT)) {

            statement.setBytes(1, UuidConverter.toBytes(uuid));
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return Optional.of(result.getLong("money"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error when setting player money to database", e);
        }

        return Optional.empty();
    }
}
