package dev.nitramnibus.nitrameco;

import dev.nitramnibus.nitrameco.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NitramEco extends JavaPlugin {

    private final DatabaseManager database = new DatabaseManager(this);


    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        database.closeConnection();
    }
}
