package com.mind31313.tmpdeop.commands;

import com.mind31313.tmpdeop.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class AdminCommand implements CommandExecutor {

    private final DataManager dataManager;

    public AdminCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tmpdeopadmin")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Not enough arguments! Usage: /tmpdeopadmin list | remove");
            } else if (args[0].equalsIgnoreCase("list")) {
                StringBuilder message = new StringBuilder("Temporarily de-opped players:\n");
                Set<String> players = dataManager.getAllPlayers();
                if (players.size() > 0) {
                    for (String playerUUID : players) {
                        Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
                        if (player != null)
                            message.append(player.getName()).append("\n");
                    }
                } else {
                    message.append("None\n");
                }
                sender.sendMessage(message.toString());
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "No player specified");
                } else {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null || !dataManager.getAllPlayers().contains(player.getUniqueId().toString())) {
                        sender.sendMessage(ChatColor.RED + "Player not found!");
                    } else {
                        dataManager.removePlayer(player.getUniqueId().toString());
                        sender.sendMessage("Removed " + args[1] + " from list of temporarily de-opped players");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid option!");
            }
        }
        return true;
    }
}
