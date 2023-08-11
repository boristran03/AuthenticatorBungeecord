package com.minefh.authenticatorbungeecord.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minefh.authenticatorbungeecord.AuthenticatorBungeecord;

import java.io.*;

public class ConfigParser {

    private static ConfigParser __instance;

    private final AuthenticatorBungeecord plugin;
    private final Gson gson;
    private Config config;

    public ConfigParser(AuthenticatorBungeecord plugin) {
        __instance = this;

        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    public void init() {
        plugin.getDataFolder().mkdir();
        File file = new File(plugin.getDataFolder(), "config.json");
        FileReader reader = null;
        FileWriter writer = null;
        try {
            boolean created = file.createNewFile();
            if (created) {
                writer = new FileWriter(file);
                this.config = new Config();
                gson.toJson(config, writer);
            } else {
                reader = new FileReader(file);
                this.config = gson.fromJson(new FileReader(file), Config.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clean(reader, writer);
        }
    }

    public void reload() {

    }

    private void clean(Reader reader, Writer writer) {

        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigParser getInstance() {
        return __instance;
    }

    public Config getConfig() {
        return config;
    }
}
