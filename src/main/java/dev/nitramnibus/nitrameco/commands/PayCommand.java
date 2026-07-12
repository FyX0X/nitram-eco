package dev.nitramnibus.nitrameco.commands;

import dev.nitramnibus.nitrameco.systems.EconomySystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.google.gson.internal.bind.TypeAdapters.UUID;

public class PayCommand implements CommandExecutor {

    private final EconomySystem economy;


    public PayCommand(EconomySystem economy) {
        this.economy = economy;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if ( !(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        if (args.length != 2) {
            return false;
        }

        Player to = Bukkit.getPlayer(args[0]);
        if (to == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }
        long amount = Long.parseLong(args[1]);
        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "Amount must be strictly positive.");
            return true;
        }

        if ( !economy.transferMoney(player.getUniqueId(), to.getUniqueId(), amount)) {
            player.sendMessage(ChatColor.RED + "You do not have enough money.");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "You transfered: " + ChatColor.BOLD + amount + "$"
                + ChatColor.YELLOW + " to " + to.getName());

        to.sendMessage(ChatColor.GREEN + "You received: " + ChatColor.BOLD + amount + "$"
                + ChatColor.GREEN + " from " + to.getName());


        return true;
    }
}
