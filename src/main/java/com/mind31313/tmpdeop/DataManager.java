package com.mind31313.tmpdeop;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Set;

public class DataManager {

    private JSONObject data;

    public void loadFromFile(String path) throws IOException {
        FileReader reader;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            FileWriter writer = new FileWriter(path);
            writer.close();
            reader = new FileReader(path);
        }
        JSONParser parser = new JSONParser();
        try {
            data = (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            data = new JSONObject();
        }
    }

    public void saveToFile(String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(data.toJSONString());
        writer.close();
    }

    public Object getPlayer(String playerName) throws NullPointerException {
        return data.get(playerName);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getAllPlayers() {
        return data.keySet();
    }

    @SuppressWarnings("unchecked")
    public void addPlayer(String playerName) {
        data.put(playerName, Instant.now().getEpochSecond());
    }

    public void removePlayer(String playerName) {
        data.remove(playerName);
    }
}
