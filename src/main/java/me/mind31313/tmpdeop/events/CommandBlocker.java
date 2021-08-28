package me.mind31313.tmpdeop.events;

import me.mind31313.tmpdeop.DataManager;
import me.mind31313.tmpdeop.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.Instant;
import java.util.List;

public class CommandBlocker implements Listener {

    private final DataManager dataManager;
    private List<String> blockedCommands;

    public CommandBlocker(DataManager dataManager) {
        this.dataManager = dataManager;
        reload();
    }

    public void reload() {
        FileConfiguration config = Main.plugin.getConfig();
        blockedCommands = config.getStringList("blockedCommands");
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        List<Instant> data = dataManager.getPlayer(player);
        if (data == null || data.get(1) == null) return;
        boolean blocked = false;
        String command = event.getMessage().substring(1);
        for (String cmd : blockedCommands) {
            if (command.startsWith(cmd)) {
                blocked = true;
                break;
            }
        }
        if (blocked) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
        }
    }
}
