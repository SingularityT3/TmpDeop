package me.mind31313.tmpdeop.commands;

import me.mind31313.tmpdeop.DataManager;
import me.mind31313.tmpdeop.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AdminCommand implements TabExecutor {

    private final DataManager dataManager;

    public AdminCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tmpdeopadmin")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Not enough arguments! Usage: /tmpdeopadmin <list | remove | reload>");
            } else if (args[0].equalsIgnoreCase("list")) {
                StringBuilder message = new StringBuilder("Temporarily de-opped players:\n");
                Set<String> players = dataManager.getAllPlayers();
                List<String> deopPlayers = new ArrayList<>();
                List<String> opPlayers = new ArrayList<>();

                for (String playerUUID : players) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
                    List<Instant> data = dataManager.getPlayer(playerUUID);
                    String name = player.isOnline() ? player.getName() : playerUUID;
                    if (data.get(0) != null) {
                        deopPlayers.add(name);
                    } else if (data.get(1) != null) {
                        opPlayers.add(name);
                    }
                }

                if (deopPlayers.size() > 0) {
                    deopPlayers.forEach(p -> {
                        message.append(p);
                        message.append("\n");
                    });
                } else {
                    message.append("None\n");
                }

                message.append("Temporarily opped players:\n");
                if (opPlayers.size() > 0) {
                    opPlayers.forEach(p -> {
                        message.append(p);
                        message.append("\n");
                    });
                } else {
                    message.append("None\n");
                }
                sender.sendMessage(message.toString());
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "No player specified");
                } else {
                    Player player = Bukkit.getPlayer(args[1]);
                    if ((player == null || !dataManager.getAllPlayers().contains(player.getUniqueId().toString())) && !dataManager.getAllPlayers().contains(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Player not found!");
                    } else {
                        if (player != null) {
                            dataManager.removePlayer(player);
                        } else {
                            dataManager.removePlayer(args[1]);
                        }
                        sender.sendMessage(ChatColor.GREEN + "Removed " + args[1]);
                        if (player != null) {
                            if (player.isOp()) {
                                player.sendMessage(ChatColor.YELLOW + "You were de-opped");
                                player.setOp(false);
                            }
                        } else {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
                            offlinePlayer.setOp(false);
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                Main.plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid option!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (alias.equalsIgnoreCase("tmpdeopadmin")) {
            List<String> completions = new ArrayList<>();
            if (args.length == 1) {
                if ("list".startsWith(args[0])) {
                    completions.add("list");
                }
                if ("remove".startsWith(args[0])) {
                    completions.add("remove");
                }
                if ("reload".startsWith(args[0])) {
                    completions.add("reload");
                }
            } else if (args.length == 2) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().startsWith(args[1])) {
                        completions.add(player.getName());
                    }
                }
                for (String uuid : dataManager.getAllPlayers()) {
                    if (uuid.startsWith(args[1])) {
                        completions.add(uuid);
                    }
                }
            }
            return completions;
        }
        return null;
    }
}
