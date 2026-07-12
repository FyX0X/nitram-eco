package dev.nitramnibus.nitrameco;

import dev.nitramnibus.nitrameco.commands.BalanceCommand;
import dev.nitramnibus.nitrameco.commands.PayCommand;
import dev.nitramnibus.nitrameco.commands.SetMoneyCommand;
import dev.nitramnibus.nitrameco.database.DatabaseManager;
import dev.nitramnibus.nitrameco.database.MoneyDAO;
import dev.nitramnibus.nitrameco.listeners.EconomyListener;
import dev.nitramnibus.nitrameco.systems.EconomySystem;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.logging.Logger;

public class NitramEco extends JavaPlugin {

    private final Logger logger = getLogger();
    private DatabaseManager database;
    private EconomySystem economySystem;


    public NitramEco()
    {
        super();
    }

    // used for MockBukkit
    protected NitramEco(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        database = new DatabaseManager(this, logger);

        MoneyDAO moneyDAO = new MoneyDAO(this, database.getDataSource(), logger);
        economySystem = new EconomySystem(moneyDAO, logger);

        Bukkit.getPluginManager().registerEvents(new EconomyListener(economySystem), this);

        // register commands
        getCommand("balance").setExecutor(new BalanceCommand(economySystem));
        getCommand("pay").setExecutor(new PayCommand(economySystem));
        getCommand("setmoney").setExecutor(new SetMoneyCommand(economySystem));

        // ensure already present are loaded
        economySystem.loadAllPlayers();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // ensure to save player data if no PlayerQuitEvent sent.
        economySystem.saveAllPlayersBlocking();

        database.closeConnection();
    }

    /**
     * Prefer dependency injection, use this only in testing suite.
     * @return the plugin's economy system.
     */
    public EconomySystem getEconomySystem() {
        return economySystem;
    }
}
