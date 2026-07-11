package dev.nitramnibus.nitrameco;

import dev.nitramnibus.nitrameco.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class NitramEco extends JavaPlugin {

    private final Logger logger = getLogger();
    private DatabaseManager database;


    @Override
    public void onEnable() {
        // Plugin startup logic

        database = new DatabaseManager(this, logger);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        database.closeConnection();
    }
}
