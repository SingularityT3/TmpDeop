package com.mind31313.tmpdeop;

import com.mind31313.tmpdeop.commands.AdminCommand;
import com.mind31313.tmpdeop.commands.ReopCommand;
import com.mind31313.tmpdeop.commands.TempDeopCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {

    private DataManager dataManager;
    private String path;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        path = getDataFolder().getPath() + "/playerdata.json";
        dataManager = new DataManager();

        try {
            dataManager.loadFromFile(path);
        } catch (IOException e) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[TmpDeop] Unable to load player data from file!");
        }

        getCommand("tmpdeop").setExecutor(new TempDeopCommand(dataManager));
        getCommand("reop").setExecutor(new ReopCommand(dataManager, getConfig().getLong("expiration-time", 3600)));
        getCommand("tmpdeopadmin").setExecutor(new AdminCommand(dataManager));

        getServer().getConsoleSender().sendMessage("[TmpDeop] Plugin enabled");
    }

    @Override
    public void onDisable() {
        try {
            getServer().getConsoleSender().sendMessage("[TmpDeop] Saving data...");
            dataManager.saveToFile(path);
            getServer().getConsoleSender().sendMessage("[TmpDeop] Saved data");
        } catch (IOException e) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[TmpDeop] Unable to save player data to file!");
        }

        getServer().getConsoleSender().sendMessage("[TmpDeop] Plugin disabled");
    }
}
