package dev.nitramnibus.nitrameco.systems;

import dev.nitramnibus.nitrameco.NitramEco;
import dev.nitramnibus.nitrameco.database.MoneyDAO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnegative;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


// represent the money of all online players .
// Used to not constantly query the database
public class EconomySystem {

    private static boolean instantiated = false;

    private final ConcurrentHashMap<UUID, Long> playerMoney;
    private final MoneyDAO moneyDAO;
    private final Logger logger;

    public EconomySystem(MoneyDAO moneyDAO, Logger logger) {
        if (instantiated) {
            throw new IllegalStateException("Cannot create multiple Economy System");
        }
        instantiated = true;
        this.playerMoney = new ConcurrentHashMap<>();
        this.moneyDAO = moneyDAO;
        this.logger = logger;
    }

    public void removePlayer(UUID uuid) {
        playerMoney.remove(uuid);
    }

    public void setMoney(UUID uuid, @Nonnegative long money) {
        playerMoney.put(uuid, money);
    }

    /**
     * Gets the money of a currently ONLINE player.
     * @param uuid the UUID of the player.
     * @return the balance of the player.
     */
    public long getMoney(UUID uuid) {
        Long money = playerMoney.get(uuid);
        if (money == null) {
            throw new IllegalStateException("No balance loaded for player " + uuid);
        }
        return money;
    }

    public void addMoney(UUID uuid, @Nonnegative long amount) {
        setMoney(uuid, getMoney(uuid) + amount);
    }

    public boolean takeMoney(UUID uuid, @Nonnegative long amount) {
        long balance = getMoney(uuid);
        if (balance < amount) {
            return false;
        }
        setMoney(uuid, balance - amount);
        return true;
    }

    public boolean transferMoney(UUID from, UUID to, @Nonnegative long amount) {
        if (! takeMoney(from, amount)) {
            return false;
        }
        addMoney(to, amount);
        return true;
    }

    public void loadPlayer(UUID uuid) {
        moneyDAO.getPlayerMoneyAsync(uuid)
                .thenAccept(money -> setMoney(uuid, money))
                .exceptionally(error -> {
                    logger.log(Level.SEVERE, "Could not load player money from database: ", error);
                    return null;
                });
    }

    public void savePlayer(UUID uuid) {
        moneyDAO.setPlayerMoneyAsync(uuid, getMoney(uuid))
                .exceptionally(error -> {
                    logger.log(Level.SEVERE, "Could not save player money to database: ", error);
                    return null;
                });
    }

    public void loadAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayer(player.getUniqueId());
        }
    }

    public void saveAllPlayers() {
        for (UUID uuid : playerMoney.keySet()) {
            savePlayer(uuid);
        }
    }

    public void saveAllPlayersBlocking() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            moneyDAO.setPlayerMoney(uuid, getMoney(uuid));
        }
    }

}
