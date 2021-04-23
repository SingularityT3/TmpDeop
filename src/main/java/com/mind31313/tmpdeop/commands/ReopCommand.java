package com.mind31313.tmpdeop.commands;

import com.mind31313.tmpdeop.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;

public class ReopCommand implements CommandExecutor {

    private final DataManager dataManager;
    private final long expirationTime;

    public ReopCommand(DataManager dataManager, long expirationTime) {
        this.dataManager = dataManager;
        this.expirationTime = expirationTime == 0 ? Long.MAX_VALUE : expirationTime;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use that command!");
        } else if (command.getName().equalsIgnoreCase("reop")) {
            Player player = (Player) sender;
            Object deopTime = dataManager.getPlayer(player.getName());
            if (player.isOp()) {
                player.sendMessage(ChatColor.RED + "Nothing changed, you are already op!");
            } else if (deopTime == null) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
            } else if (Instant.now().getEpochSecond() - (long) deopTime > expirationTime) {
                long timeSinceExpire = Instant.now().getEpochSecond() - ((long) deopTime + expirationTime);
                player.sendMessage(ChatColor.RED + "Time expired " + timeSinceExpire + " seconds ago!");
            } else {
                player.setOp(true);
                player.sendMessage(ChatColor.GREEN + "You have been opped!");
            }
            dataManager.removePlayer(player.getName());
        }
        return true;
    }
}
