package dev.nitramnibus.nitrameco.commands;

import dev.nitramnibus.nitrameco.systems.EconomySystem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    private final EconomySystem economy;


    public BalanceCommand(EconomySystem economy) {
        this.economy = economy;
    }


    // TODO: implement checking other players info
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if ( !(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        long money = economy.getMoney(player.getUniqueId());

        player.sendMessage(ChatColor.GREEN + "Your balance is: " + ChatColor.BOLD + money + "$");

        return true;
    }
}
