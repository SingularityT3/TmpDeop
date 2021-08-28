package me.mind31313.tmpdeop.commands;

import me.mind31313.tmpdeop.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.List;

public class ReopCommand implements CommandExecutor {

    private final DataManager dataManager;

    public ReopCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use that command!");
        } else if (label.equalsIgnoreCase("reop")) {
            Player player = (Player) sender;
            List<Instant> playerData = dataManager.getPlayer(player);
            if (player.isOp()) {
                player.sendMessage(ChatColor.YELLOW + "Nothing changed, you are already op!");
            } else if (playerData == null || playerData.get(0) == null) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use that command or your deop time has expired");
            } else {
                player.setOp(true);
                player.sendMessage(ChatColor.GREEN + "You have been opped!");
                dataManager.updatePlayer(player, null, playerData.get(1));
            }
        }
        return true;
    }
}
