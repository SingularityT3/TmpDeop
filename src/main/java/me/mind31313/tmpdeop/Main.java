package me.mind31313.tmpdeop;

import me.mind31313.tmpdeop.commands.AdminCommand;
import me.mind31313.tmpdeop.commands.ReopCommand;
import me.mind31313.tmpdeop.commands.TempDeopCommand;
import me.mind31313.tmpdeop.commands.TempOpCommand;
import me.mind31313.tmpdeop.events.CommandBlocker;
import me.mind31313.tmpdeop.tasks.CleanupTask;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Main extends JavaPlugin {

    public static Main plugin;

    private DataManager dataManager;
    private CleanupTask cleanupTask = null;
    private CommandBlocker commandBlocker = null;

    public Main() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String path = getDataFolder().getPath() + "/playerdata";
        dataManager = new DataManager(path);

        try {
            dataManager.load();
        } catch (IOException | ClassNotFoundException e) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[TmpDeop] Unable to load player data! Creating new file");
        }

        AdminCommand adminCommand = new AdminCommand(dataManager);
        getCommand("tmpdeop").setExecutor(new TempDeopCommand(dataManager));
        getCommand("reop").setExecutor(new ReopCommand(dataManager));
        getCommand("tmpdeopadmin").setExecutor(adminCommand);
        getCommand("tmpdeopadmin").setTabCompleter(adminCommand);
        getCommand("tmpop").setExecutor(new TempOpCommand(dataManager));

        commandBlocker = new CommandBlocker(dataManager);
        getServer().getPluginManager().registerEvents(commandBlocker, this);

        cleanupTask = new CleanupTask(dataManager);
        cleanupTask.runTaskTimer(this, 100, 200);

        getServer().getConsoleSender().sendMessage("[TmpDeop] Plugin enabled");
    }

    @Override
    public void onDisable() {
        try {
            dataManager.save();
            getServer().getConsoleSender().sendMessage("[TmpDeop] Saved data");
        } catch (IOException e) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[TmpDeop] Unable to save player data to file!");
        }

        getServer().getConsoleSender().sendMessage("[TmpDeop] Plugin disabled");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        if (cleanupTask != null) {
            cleanupTask.reload();
        }
        if (commandBlocker != null) {
            commandBlocker.reload();
        }
    }
}
