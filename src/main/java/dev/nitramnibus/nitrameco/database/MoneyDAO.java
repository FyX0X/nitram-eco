package dev.nitramnibus.nitrameco.database;

import com.zaxxer.hikari.HikariDataSource;
import dev.nitramnibus.nitrameco.NitramEco;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoneyDAO {

    private static final String UPSERT = "INSERT INTO money VALUES(?, ?) ON DUPLICATE KEY UPDATE money=?";
    private static final String SELECT = "SELECT * from money WHERE uuid=?";

    private final NitramEco plugin;
    private final DataSource dataSource;
    private final Executor executor;
    private final Logger logger;

    public MoneyDAO(NitramEco plugin, DataSource dataSource, Logger logger) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.logger = logger;
        this.executor = new BukkitAsyncExecutor();
    }

    public CompletableFuture<Void> setPlayerMoneyAsync(UUID uuid, long money) {
        return CompletableFuture.runAsync(() -> setPlayerMoney(uuid, money), executor);
    }

    public CompletableFuture<Long> getPlayerMoneyAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getPlayerMoney(uuid), executor);
    }

    /**
     * Set the player money to the database.
     * Warning : This is a BLOCKING Call and should only be called when plugin gets disabled.
     * @param uuid the UUID of the player.
     * @param money the balance to save.
     */
    public void setPlayerMoney(UUID uuid, long money) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPSERT)) {

            statement.setBytes(1, UuidConverter.toBytes(uuid));
            statement.setLong(2, money);
            statement.setLong(3, money);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Error when setting player money to database", e);
        }

    }

    public long getPlayerMoney(UUID uuid) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT)) {

            statement.setBytes(1, UuidConverter.toBytes(uuid));
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getLong("money");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error when setting player money to database", e);
        }
        logger.log(Level.WARNING, "Could not retrieve player money from database");
        return 0L;
    }


    private class BukkitAsyncExecutor implements Executor {

        @Override
        public void execute(@NonNull Runnable runnable) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }
}
