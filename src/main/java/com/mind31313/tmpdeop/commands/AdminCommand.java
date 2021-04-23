package com.mind31313.tmpdeop.commands;

import com.mind31313.tmpdeop.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class AdminCommand implements CommandExecutor {

    private DataManager dataManager;

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
                    for (String playerName : players) {
                        message.append(playerName + "\n");
                    }
                } else {
                    message.append("None\n");
                }
                sender.sendMessage(message.toString());
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "No player specified");
                } else if (!dataManager.getAllPlayers().contains(args[1])) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                } else {
                    dataManager.removePlayer(args[1]);
                    sender.sendMessage("Removed " + args[1] + " from list of temporarily de-opped players");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid option!");
            }
        }
        return true;
    }
}
