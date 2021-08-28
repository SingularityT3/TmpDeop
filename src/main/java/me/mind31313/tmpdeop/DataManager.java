package me.mind31313.tmpdeop;

import org.bukkit.entity.Player;

import java.io.*;
import java.time.Instant;
import java.util.*;

public class DataManager {

    private final String path;
    private HashMap<String, List<Instant>> map;

    public DataManager(String path) {
        this.path = path;
    }

    public void save() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(map);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    @SuppressWarnings("unchecked")
    public void load() throws IOException, ClassNotFoundException {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            map = (HashMap<String, List<Instant>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            map = new HashMap<>();
            throw e;
        }
    }

    public List<Instant> getPlayer(String uuid) {
        return map.get(uuid);
    }

    public List<Instant> getPlayer(Player player) {
        return getPlayer(player.getUniqueId().toString());
    }

    public void updatePlayer(String uuid, Instant deopTime, Instant opExpireTime) {
        List<Instant> data = new ArrayList<>();
        Collections.addAll(data, deopTime, opExpireTime);
        map.put(uuid, data);
    }

    public void updatePlayer(Player player, Instant deopTime, Instant opExpireTime) {
        updatePlayer(player.getUniqueId().toString(), deopTime, opExpireTime);
    }

    public void removePlayer(String uuid) {
        map.remove(uuid);
    }

    public void removePlayer(Player player) {
        removePlayer(player.getUniqueId().toString());
    }

    public Set<String> getAllPlayers() {
        return map.keySet();
    }
}
