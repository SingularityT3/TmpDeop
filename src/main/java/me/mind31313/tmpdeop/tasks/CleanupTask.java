package me.mind31313.tmpdeop.tasks;

import me.mind31313.tmpdeop.DataManager;
import me.mind31313.tmpdeop.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CleanupTask extends BukkitRunnable {

    private final DataManager dataManager;
    private long expirationTime;

    public CleanupTask(DataManager dataManager) {
        this.dataManager = dataManager;
        reload();
    }

    public void reload() {
        FileConfiguration config = Main.plugin.getConfig();
        expirationTime = config.getLong("expiration-time");
        if (expirationTime == 0) {
            expirationTime = Long.MAX_VALUE;
        }
    }

    @Override
    public void run() {
        for (String uuid : dataManager.getAllPlayers()) {
            List<Instant> data = dataManager.getPlayer(uuid);
            if (data.get(0) != null && Instant.now().isAfter(data.get(0).plusSeconds(expirationTime))) {
                data.set(0, null);
            }
            if (data.get(1) != null && data.get(1).isBefore(Instant.now())) {
                data.set(0, null);
                data.set(1, null);
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                if (player.isOnline() && player.isOp()) {
                    player.getPlayer().sendMessage(ChatColor.YELLOW + "Your op has expired!");
                }
                player.setOp(false);
            }
            if (data.get(0) == null && data.get(1) == null) {
                dataManager.removePlayer(uuid);
            }
        }
    }
}
