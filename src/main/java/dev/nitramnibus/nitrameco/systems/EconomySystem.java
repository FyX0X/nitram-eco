package dev.nitramnibus.nitrameco.systems;

import javax.annotation.Nonnegative;
import java.util.HashMap;
import java.util.UUID;



// represent the money of all online players .
// Used to not constantly query the database
public class EconomySystem {

    private static boolean instantiated = false;

    private final HashMap<UUID, Long> playerMoney;


    public EconomySystem() {
        if (instantiated) {
            throw new IllegalStateException("Cannot create multiple Economy System");
        }
        instantiated = true;
        playerMoney = new HashMap<>();
    }

    public void removePlayer(UUID uuid) {
        playerMoney.remove(uuid);
    }

    public void setMoney(UUID uuid, @Nonnegative long money) {
        playerMoney.put(uuid, money);
    }

    public long getMoney(UUID uuid) {
        return playerMoney.get(uuid);
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
}
