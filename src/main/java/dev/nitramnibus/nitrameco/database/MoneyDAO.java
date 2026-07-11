package dev.nitramnibus.nitrameco.database;

import com.zaxxer.hikari.HikariDataSource;
import dev.nitramnibus.nitrameco.NitramEco;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MoneyDAO {

    private static final String UPSERT = "INSERT INTO money VALUES(?, ?) ON DUPLICATE KEY UPDATE money=?";
    private static final String SELECT = "SELECT money from money WHERE uuid=?";

    private final NitramEco plugin;
    private final HikariDataSource hikari;
    private final Executor executor;

    public MoneyDAO(NitramEco plugin, HikariDataSource hikari) {
        this.plugin = plugin;
        this.hikari = hikari;
        this.executor = new BukkitAsyncExecutor();
    }

    public CompletableFuture<Void> setPlayerMoneyAsync(UUID uuid, long money) {
        return CompletableFuture.runAsync(() -> setPlayerMoney(uuid, money), executor);
    }

    public CompletableFuture<Long> getPlayerMoneyAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getPlayerMoney(uuid), executor);
    }

    private void setPlayerMoney(UUID uuid, long money) {

        try (Connection connection = hikari.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPSERT)) {

            statement.setBytes(1, UuidConverter.toBytes(uuid));
            statement.setLong(2, money);
            statement.setLong(3, money);

        } catch (SQLException e) {
            throw new RuntimeException("Error when setting player money to database", e);
        }

    }

    private Long getPlayerMoney(UUID uuid) {

        try (Connection connection = hikari.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT)) {

            statement.setBytes(1, UuidConverter.toBytes(uuid));
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getLong("money");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error when setting player money to database", e);
        }

        return -1L;
    }


    private class BukkitAsyncExecutor implements Executor {

        @Override
        public void execute(@NonNull Runnable runnable) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }
}
