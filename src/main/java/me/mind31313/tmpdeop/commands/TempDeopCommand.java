package me.mind31313.tmpdeop.commands;

import me.mind31313.tmpdeop.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.List;

public class TempDeopCommand implements CommandExecutor {

    private final DataManager dataManager;

    public TempDeopCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + "Only players can use that command!");
        } else if (command.getName().equalsIgnoreCase("tmpdeop")) {
            Player player = (Player) sender;
            Player target;

            if (args.length < 1) {
                target = player;
            } else {
                List<Instant> data = dataManager.getPlayer(player);
                if (data != null && data.get(1) != null) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to deop others!");
                    return true;
                }
                target = player.getServer().getPlayer(args[0]);
            }

            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player does not exist!");
            } else {
                List<Instant> data = dataManager.getPlayer(target);
                Instant i = data != null ? data.get(1) : null;
                dataManager.updatePlayer(target, Instant.now(), i);
                target.setOp(false);
                target.sendMessage(ChatColor.YELLOW + "You have been temporarily de-opped. Use /reop to get op back");
            }
        }
        return true;
    }
}
