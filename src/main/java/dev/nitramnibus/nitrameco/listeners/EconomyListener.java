package dev.nitramnibus.nitrameco.listeners;

import dev.nitramnibus.nitrameco.systems.EconomySystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EconomyListener implements Listener {

    private final EconomySystem economy;

    public EconomyListener(EconomySystem economy) {
        this.economy = economy;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        economy.loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        economy.savePlayer(event.getPlayer().getUniqueId());
    }
}
