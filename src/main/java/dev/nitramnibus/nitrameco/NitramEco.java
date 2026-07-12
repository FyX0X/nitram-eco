package dev.nitramnibus.nitrameco;

import dev.nitramnibus.nitrameco.database.DatabaseManager;
import dev.nitramnibus.nitrameco.database.MoneyDAO;
import dev.nitramnibus.nitrameco.listeners.EconomyListener;
import dev.nitramnibus.nitrameco.systems.EconomySystem;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class NitramEco extends JavaPlugin {

    private final Logger logger = getLogger();
    private DatabaseManager database;
    private EconomySystem economySystem;

    @Override
    public void onEnable() {
        // Plugin startup logic

        database = new DatabaseManager(this, logger);

        MoneyDAO moneyDAO = new MoneyDAO(this, database.getDataSource());
        economySystem = new EconomySystem(moneyDAO, logger);

        Bukkit.getPluginManager().registerEvents(new EconomyListener(economySystem), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // ensure to save player data if no PlayerQuitEvent sent.
        economySystem.saveAllPlayers();

        database.closeConnection();
    }
}
