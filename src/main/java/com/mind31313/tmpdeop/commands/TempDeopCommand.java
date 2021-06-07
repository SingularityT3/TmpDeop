package com.mind31313.tmpdeop.commands;

import com.mind31313.tmpdeop.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                sender.sendMessage("tmpdeop " + player.getName());
                target = player;
            } else {
                target = player.getServer().getPlayer(args[0]);
            }

            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player does not exist!");
            } else {
                dataManager.addPlayer(target.getUniqueId().toString());
                target.setOp(false);
                target.sendMessage("You have been temporarily de-opped. Use /reop to get op back");
            }
        }
        return true;
    }
}
