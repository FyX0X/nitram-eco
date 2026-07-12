package dev.nitramnibus.nitrameco.commands;

import dev.nitramnibus.nitrameco.systems.EconomySystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMoneyCommand implements CommandExecutor {

    private final EconomySystem economy;


    public SetMoneyCommand(EconomySystem economy) {
        this.economy = economy;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 2) {
            return false;
        }

        long amount;
        Player target;

        if (args.length == 1) {
            if (! (sender instanceof Player player)) {
                return false;
            }
            target = player;
            amount = Long.parseLong(args[0]);
        } else {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            amount = Long.parseLong(args[1]);
        }

        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "Amount must be strictly positive.");
            return true;
        }

        economy.setMoney(target.getUniqueId(), amount);


        target.sendMessage(ChatColor.GREEN + "Your money is now: " + ChatColor.BOLD + amount + "$");

        return true;
    }}
