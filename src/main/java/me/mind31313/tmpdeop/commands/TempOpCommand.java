package me.mind31313.tmpdeop.commands;

import me.mind31313.tmpdeop.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.List;

public class TempOpCommand implements CommandExecutor {

    private final DataManager dataManager;

    public TempOpCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("tmpop")) {
            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /tmpop <player> <duration>");
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            List<Instant> data = dataManager.getPlayer(target);
            if (data != null || target.isOp()) {
                sender.sendMessage(ChatColor.RED + "Player already has op!");
                return true;
            }
            int duration;
            try {
                duration = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid duration!");
                return true;
            }
            Instant expireTime = Instant.now().plusSeconds(duration);
            dataManager.updatePlayer(target, null, expireTime);
            target.setOp(true);
            sender.sendMessage(ChatColor.YELLOW + args[0] + " has been opped for " + args[1] + " seconds");
            target.sendMessage(ChatColor.YELLOW + "You have been opped for " + args[1] + " seconds");
        }
        return true;
    }
}
